package at.irian.typescript.gradle

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

        this.createTask("compileTypeScript", CompileMainTask, target)
        this.createTask("compileTestTypeScript", CompileTestTask, target)


        this.createTask("initTestResources", InitTestResourcesTask, target)
        this.createTask("prepareTestJs", PrepareTestLibsTask, target)
        this.createTask("generateTestHtml", GenerateTestHtmlTask, target)

        this.createTask("test", RunTestInConsoleTask, target).dependsOn "generateTestHtml", "prepareTestJs", "compileTestTypeScript"

        this.createTask("testInBrowser", RunTestInBrowserTask, target).dependsOn "generateTestHtml", "prepareTestJs", "compileTestTypeScript"

        this.createTask("cleanMain", CleanMainTask, target)

        this.createTask("cleanTestHtml", CleanTestHtmlTask, target)
        this.createTask("cleanTestJs", CleanTestJsTask, target)
        this.createTask("cleanTestLibs", CleanTestLibsTask, target)

        this.createTask("cleanTest", DefaultTask, target).dependsOn "cleanTestHtml", "cleanTestJs", "cleanTestLibs"

        this.createTask("clean", DefaultTask, target).dependsOn "cleanTest", "cleanMain"

        this.createTask("tscCheck", TypeScriptCompilerCheckTask, target)

        this.createTask("copyVendorJs", CopyVendorJsTask, target)

    }

    private <T extends DefaultTask> T createTask(String name, Class<T> taskClass, Project project) {
        T task = (T) project.task(name, type: taskClass)
        if (task instanceof TypeScriptPluginTask) {
            task.setupInputsAndOutputs(TypeScriptPluginExtension.getInstance(project))
        }
        return task
    }
}
