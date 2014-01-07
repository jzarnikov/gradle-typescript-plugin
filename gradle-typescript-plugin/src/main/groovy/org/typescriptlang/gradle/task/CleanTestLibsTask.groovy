package org.typescriptlang.gradle.task

import org.gradle.api.tasks.TaskAction
import org.typescriptlang.gradle.TypeScriptPluginExtension

class CleanTestLibsTask extends AlwaysReRunTypescriptPluginTask {

    @TaskAction
    void clean() {
        File testJsLibsDir = TypeScriptPluginExtension.getInstance(project).getTestLibsDir()
        project.delete(testJsLibsDir)
    }
}
