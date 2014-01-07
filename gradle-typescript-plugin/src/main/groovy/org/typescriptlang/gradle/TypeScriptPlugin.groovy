package org.typescriptlang.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.typescriptlang.gradle.task.CleanMainTask
import org.typescriptlang.gradle.task.CleanTestHtmlTask
import org.typescriptlang.gradle.task.CleanTestJsTask
import org.typescriptlang.gradle.task.CleanTestLibsTask
import org.typescriptlang.gradle.task.CompileMainTask
import org.typescriptlang.gradle.task.CompileTestTask
import org.typescriptlang.gradle.task.GenerateTestHtmlTask
import org.typescriptlang.gradle.task.InitTestResourcesTask
import org.typescriptlang.gradle.task.PrepareTestLibsTask
import org.typescriptlang.gradle.task.RunTestInBrowserTask
import org.typescriptlang.gradle.task.RunTestInConsoleTask
import org.typescriptlang.gradle.task.TypeScriptPluginTask

class TypeScriptPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        target.extensions.create("typescript", TypeScriptPluginExtension, target);

        this.createTask("compileTypeScript", CompileMainTask, target)
        this.createTask("compileTestTypeScript", CompileTestTask, target)


        this.createTask("initTestResources", InitTestResourcesTask, target)
        this.createTask("prepareTestJs", PrepareTestLibsTask, target)
        this.createTask("generateTestHtml", GenerateTestHtmlTask, target)

        target.task("test", type: RunTestInConsoleTask)
        target.tasks.getByName("test").dependsOn "generateTestHtml", "prepareTestJs", "compileTestTypeScript"

        RunTestInBrowserTask runTestInBrowserTask = target.task("testInBrowser", type: RunTestInBrowserTask)
        runTestInBrowserTask.dependsOn "generateTestHtml", "prepareTestJs", "compileTestTypeScript"

        this.createTask("cleanMain", CleanMainTask, target)

        target.task("cleanTestHtml", type: CleanTestHtmlTask)
        this.createTask("cleanTestJs", CleanTestJsTask, target)
        target.task("cleanTestLibs", type: CleanTestLibsTask)

        target.task("cleanTest", type: DefaultTask)
        target.tasks.getByName("cleanTest").dependsOn "cleanTestHtml", "cleanTestJs", "cleanTestLibs"

        target.task("clean", type: DefaultTask)
        target.tasks.getByName("clean").dependsOn "cleanTest", "cleanMain"

    }

    private <T> T createTask(String name, Class<T> taskClass, Project project) {
        T task = (T) project.task(name, type: taskClass)
        if (task instanceof TypeScriptPluginTask) {
            task.setupInputsAndOutputs(TypeScriptPluginExtension.getInstance(project))
        }
        return task
    }
}
