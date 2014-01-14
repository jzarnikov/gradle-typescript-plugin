package at.irian.typescript.gradle

import at.irian.typescript.gradle.task.CombineGeneratedJsTask
import at.irian.typescript.gradle.task.CopyRequirejsConfigTask
import at.irian.typescript.gradle.task.CopyVendorJsTask
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import at.irian.typescript.gradle.task.CleanMainTask
import at.irian.typescript.gradle.task.CleanTestHtmlTask
import at.irian.typescript.gradle.task.CleanTestJsTask
import at.irian.typescript.gradle.task.CleanTestLibsTask
import at.irian.typescript.gradle.task.CompileMainTask
import at.irian.typescript.gradle.task.CompileTestTask
import at.irian.typescript.gradle.task.GenerateTestHtmlTask
import at.irian.typescript.gradle.task.InitTestResourcesTask
import at.irian.typescript.gradle.task.PrepareTestLibsTask
import at.irian.typescript.gradle.task.RunTestInBrowserTask
import at.irian.typescript.gradle.task.RunTestInConsoleTask
import at.irian.typescript.gradle.task.TypeScriptCompilerCheckTask
import at.irian.typescript.gradle.task.TypeScriptPluginTask

class TypeScriptPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        target.extensions.create("typescript", TypeScriptPluginExtension, target);

        this.createTask("compileTypeScript", "Compile TypeScript source code", CompileMainTask, target)
        this.createTask("compileTestTypeScript", "Compile TypeScript test source code", CompileTestTask, target)


        this.createTask("initTestResources", "Initialize test resources (currently just jasmine.d.ts)", InitTestResourcesTask, target)
        this.createTask("prepareTestLibs", "Prepare libraries requried to run the tests", PrepareTestLibsTask, target)
        this.createTask("generateTestHtml", "Generate HTML required to run the tests", GenerateTestHtmlTask, target)

        this.createTask("test", "Run the tests in console mode", RunTestInConsoleTask, target).dependsOn "generateTestHtml", "prepareTestLibs", "compileTestTypeScript"

        this.createTask("testInBrowser", "Run the tests in browser", RunTestInBrowserTask, target).dependsOn "generateTestHtml", "prepareTestLibs", "compileTestTypeScript"

        this.createTask("cleanMain", "Delete compiled JavaScript", CleanMainTask, target)

        this.createTask("cleanTestHtml", "Delete generated HTML required to run the tests", CleanTestHtmlTask, target)
        this.createTask("cleanTestJs", "Delete compile test JavaScript", CleanTestJsTask, target)
        this.createTask("cleanTestLibs", "Delete libraries requried to run the tests", CleanTestLibsTask, target)

        this.createTask("cleanTest", "Delete all generated/compiled files required to run the tests", DefaultTask, target).dependsOn "cleanTestHtml", "cleanTestJs", "cleanTestLibs"

        this.createTask("clean", "Delete all generated/compiled files", DefaultTask, target).dependsOn "cleanTest", "cleanMain"

        this.createTask("tscCheck", "Check the installed version of the TypeScript compiler", TypeScriptCompilerCheckTask, target)

        this.createTask("copyVendorJs", "Copy 3rd party libraries to the build directory", CopyVendorJsTask, target)
        this.createTask("copyRequirejsConfig", "Copy RequireJS config to the build directory", CopyRequirejsConfigTask, target)
        this.createTask("combineGeneratedJs", "Combine all JavaScript to a single file for better performance", CombineGeneratedJsTask, target).dependsOn "copyVendorJs", "copyRequirejsConfig", "compileTypeScript"

    }

    private <T extends DefaultTask> T createTask(String name, String description, Class<T> taskClass, Project project) {
        T task = (T) project.task(name, type: taskClass)
        task.description description
        if (task instanceof TypeScriptPluginTask) {
            task.setupInputsAndOutputs(TypeScriptPluginExtension.getInstance(project))
        }
        return task
    }
}
