package org.typescriptlang.gradle.task

import org.gradle.api.tasks.TaskAction
import org.typescriptlang.gradle.TypeScriptPluginExtension
import org.typescriptlang.gradle.task.AlwaysReRunTypescriptPluginTask

class CleanMainTask extends AlwaysReRunTypescriptPluginTask {

    @TaskAction
    void clean() {
        project.delete(TypeScriptPluginExtension.getInstance(project).getGeneratedJsFiles().getFiles())
    }
}
