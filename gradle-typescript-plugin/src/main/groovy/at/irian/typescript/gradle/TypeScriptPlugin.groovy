package at.irian.typescript.gradle

import at.irian.typescript.gradle.task.*
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class TypeScriptPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        target.extensions.create("typescript", TypeScriptPluginExtension, target);

        this.createTask("compileTypeScript", "Compile TypeScript source code", CompileMainTask, target)
        this.createTask("compileTypeScriptTest", "Compile TypeScript test source code", CompileTestTask, target)


        this.createTask("initTypeScriptTestResources", "Initialize test resources (currently just jasmine.d.ts)", InitTestResourcesTask, target)
        this.createTask("prepareTypeScriptTestLibs", "Prepare libraries requried to run the tests", PrepareTestLibsTask, target)
        this.createTask("generateTypeScriptTestHtml", "Generate HTML required to run the tests", GenerateTestHtmlTask, target)

        this.createTopLevelTask("test", "Run the tests in console mode", RunTestInConsoleTask, target).dependsOn "generateTypeScriptTestHtml", "prepareTypeScriptTestLibs", "compileTypeScriptTest"

        this.createTask("runTypeScriptTestsInBrowser", "Run the tests in browser", RunTestInBrowserTask, target).dependsOn "generateTypeScriptTestHtml", "prepareTypeScriptTestLibs", "compileTypeScriptTest"

        this.createTask("cleanTypeScriptMain", "Delete compiled JavaScript", CleanMainTask, target)

        this.createTask("cleanTypeScriptTestHtml", "Delete generated HTML required to run the tests", CleanTestHtmlTask, target)
        this.createTask("cleanTypeScriptTestJs", "Delete compile test JavaScript", CleanTestJsTask, target)
        this.createTask("cleanTypeScriptTestLibs", "Delete libraries requried to run the tests", CleanTestLibsTask, target)

        this.createTask("cleanTypeScriptTest", "Delete all generated/compiled files required to run the tests", DefaultTask, target).dependsOn "cleanTypeScriptTestHtml", "cleanTypeScriptTestJs", "cleanTypeScriptTestLibs"

        this.createTopLevelTask("clean", "Delete all generated/compiled files", DefaultTask, target).dependsOn "cleanTypeScriptTest", "cleanTypeScriptMain"

        this.createTask("copyTypeScriptVendorJs", "Copy 3rd party libraries to the build directory", CopyVendorJsTask, target)
        this.createTask("copyTypeScriptRequirejsConfig", "Copy RequireJS config to the build directory", CopyRequirejsConfigTask, target)
        this.createTask("combineTypeScriptGeneratedJs", "Combine all JavaScript to a single file for better performance", CombineGeneratedJsTask, target).dependsOn "copyTypeScriptVendorJs", "copyTypeScriptRequirejsConfig", "compileTypeScript"
    }

    private <T extends DefaultTask> T createTask(String name, String description, Class<T> taskClass, Project project) {
        T task = (T) project.task(name, type: taskClass)
        task.description description
        if (task instanceof TypeScriptPluginTask) {
            task.setupInputsAndOutputs(TypeScriptPluginExtension.getInstance(project))
        }
        return task
    }
    
    private <T extends DefaultTask> T createTopLevelTask(String name, String description, Class<T> taskClass, Project project) {
        Task existingTask = project.tasks.findByName(name);
        if (existingTask != null) {
            name = getPrefixedTaskName(name);
        }
        T task = this.createTask(name, description, taskClass, project);
        if (existingTask != null) {
            existingTask.dependsOn(task);
        }
        return task;
    }

    private String getPrefixedTaskName(String name) {
        return "typeScript" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
