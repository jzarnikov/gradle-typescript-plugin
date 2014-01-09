package at.irian.typescript.gradle.task

import org.gradle.api.tasks.TaskAction
import at.irian.typescript.gradle.TypeScriptPluginExtension

class CleanTestLibsTask extends AlwaysReRunTypescriptPluginTask {

    @TaskAction
    void clean() {
        File testJsLibsDir = TypeScriptPluginExtension.getInstance(project).getTestLibsDir()
        project.delete(testJsLibsDir)
    }
}
