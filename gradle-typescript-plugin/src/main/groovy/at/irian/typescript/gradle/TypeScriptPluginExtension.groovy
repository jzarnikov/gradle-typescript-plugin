package at.irian.typescript.gradle

import org.gradle.api.Project
import org.gradle.api.file.FileTree

class TypeScriptPluginExtension {

    String sourcePath
    String testSourcePath
    String generatedJsPath
    String requireJsConfig
    String[] tscOptions
    String[] cleanMainExcludes
    String vendorPath

    private Project project
    
    TypeScriptPluginExtension(Project project) {
        this.project = project
        this.sourcePath = "src/main/typescript"
        this.testSourcePath = "src/test/typescript"
        this.generatedJsPath = "${project.buildDir}/generatedJs"
        this.requireJsConfig = "requirejsConfig.js"
        this.tscOptions = []
        this.cleanMainExcludes = []
        this.vendorPath = "vendor"
    }

    static TypeScriptPluginExtension getInstance(Project project) {
        return project.extensions.getByType(TypeScriptPluginExtension)
    }

    File getSourceDir() {
        return new File(this.project.getProjectDir(), this.sourcePath)
    }

    File getTestSourceDir() {
        return new File(this.project.getProjectDir(), this.testSourcePath)
    }

    FileTree getGeneratedJsFiles() {
        return this.project.fileTree(this.getGeneratedJsDir()).include("**/*.js").exclude(this.cleanMainExcludes)
    }

    File getGeneratedJsDir() {
        return new File(this.generatedJsPath)
    }

    FileTree getFilesToCompile() {
        return this.project.fileTree(this.getSourceDir()).include('**/*.ts').exclude("**/*.d.ts");
    }

    FileTree getTestFilesToCompile() {
        return this.project.fileTree(this.getTestSourceDir()).include('**/*.ts').exclude("**/*.d.ts");
    }

    File getSourceCopyForTestDir() {
        return this.project.getBuildDir();
    }

    File getMainSourceCopyForTestDir() {
        return new File(this.getSourceCopyForTestDir(), this.sourcePath)
    }

    File getTestSourceCopyForTestDir() {
        return new File(this.getSourceCopyForTestDir(), this.testSourcePath)
    }

    File getTestResourcesDir() {
        return new File(this.project.getProjectDir(), this.testSourcePath + File.separator + this.vendorPath + File.separator + "jasmine")
    }

    File getTestLibsDir() {
        return new File(this.project.getBuildDir(), "testJsLibs")
    }

    File getTestHtmlDir() {
        return this.project.getBuildDir();
    }

    File getRequireJsConfigFileInSourceCopyDir() {
        return new File(this.getMainSourceCopyForTestDir(), this.requireJsConfig)
    }

}