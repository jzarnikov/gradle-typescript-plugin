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
import org.typescriptlang.gradle.task.TypeScriptCompilerCheckTask
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

        this.createTask("test", RunTestInConsoleTask, target).dependsOn "generateTestHtml", "prepareTestJs", "compileTestTypeScript"

        this.createTask("testInBrowser", RunTestInBrowserTask, target).dependsOn "generateTestHtml", "prepareTestJs", "compileTestTypeScript"

        this.createTask("cleanMain", CleanMainTask, target)

        this.createTask("cleanTestHtml", CleanTestHtmlTask, target)
        this.createTask("cleanTestJs", CleanTestJsTask, target)
        this.createTask("cleanTestLibs", CleanTestLibsTask, target)

        this.createTask("cleanTest", DefaultTask, target).dependsOn "cleanTestHtml", "cleanTestJs", "cleanTestLibs"

        this.createTask("clean", DefaultTask, target).dependsOn "cleanTest", "cleanMain"

        this.createTask("tscCheck", TypeScriptCompilerCheckTask, target)

    }

    private <T extends DefaultTask> T createTask(String name, Class<T> taskClass, Project project) {
        T task = (T) project.task(name, type: taskClass)
        if (task instanceof TypeScriptPluginTask) {
            task.setupInputsAndOutputs(TypeScriptPluginExtension.getInstance(project))
        }
        return task
    }
}
