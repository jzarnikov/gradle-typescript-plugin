package at.irian.typescript.gradle.task

import org.gradle.api.tasks.TaskAction
import at.irian.typescript.gradle.TypeScriptPluginExtension

class CleanTestJsTask extends AlwaysReRunTypescriptPluginTask {

    @TaskAction
    void clean() {
        TypeScriptPluginExtension extension = TypeScriptPluginExtension.getInstance(project)
        File mainSourceDir = extension.getMainSourceCopyForTestDir()
        File testSourceDir = extension.getTestSourceCopyForTestDir()
        project.delete(mainSourceDir, testSourceDir)
    }
}
