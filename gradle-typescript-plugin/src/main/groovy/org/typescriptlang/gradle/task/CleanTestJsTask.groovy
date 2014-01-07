package org.typescriptlang.gradle.task

import org.gradle.api.tasks.TaskAction
import org.typescriptlang.gradle.TypeScriptPluginExtension
import org.typescriptlang.gradle.task.AlwaysReRunTypescriptPluginTask

class CleanTestJsTask extends AlwaysReRunTypescriptPluginTask {

    @TaskAction
    void clean() {
        TypeScriptPluginExtension extension = TypeScriptPluginExtension.getInstance(project)
        File mainSourceDir = extension.getMainSourceCopyForTestDir()
        File testSourceDir = extension.getTestSourceCopyForTestDir()
        project.delete(mainSourceDir, testSourceDir)
    }
}
