package at.irian.typescript.gradle

import org.gradle.api.Project
import org.gradle.api.file.FileTree

class TypeScriptPluginExtension {

    String sourcePath
    String testSourcePath
    String generatedJsPath
    String requireJsConfig
    String requireConfigGlobalVarName
    String[] tscOptions
    String[] cleanMainExcludes
    String vendorPath
    String[] combineJsModules
    boolean includeLibsInCombinedJs

    private Project project
    
    TypeScriptPluginExtension(Project project) {
        this.project = project
        this.sourcePath = "src/main/typescript"
        this.testSourcePath = "src/test/typescript"
        this.generatedJsPath = "${project.buildDir}/generatedJs"
        this.requireJsConfig = "requirejsConfig.js"
        this.requireConfigGlobalVarName = "requireConfig"
        this.tscOptions = []
        this.cleanMainExcludes = []
        this.vendorPath = "vendor"
        this.combineJsModules = []
        this.includeLibsInCombinedJs = true
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

    FileTree getGeneratedJsMapFiles() {
        return this.project.fileTree(this.getGeneratedJsDir()).include("**/*.js.map").exclude(this.cleanMainExcludes);
    }

    File getGeneratedJsDir() {
        return new File(this.generatedJsPath)
    }

    FileTree getFilesToCompile(File sourceDir) {
        return this.project.fileTree(sourceDir).include('**/*.ts').exclude("**/*.d.ts");
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

    List<String> getTestFilePaths() {
        try {
            String testsToProcessPropertyValue = project.property("at.irian.typescript.test");
            List<String> paths = new ArrayList<String>();
            if (testsToProcessPropertyValue != null) {
                String[] testList = testsToProcessPropertyValue.split(",");
                for(String test : testList) {
                    paths.add(test.endsWith(".ts") ? test : test+".ts");
                }
            }
            return paths;
        } catch (MissingPropertyException e) {
            return Collections.emptyList();
        }

    }

}