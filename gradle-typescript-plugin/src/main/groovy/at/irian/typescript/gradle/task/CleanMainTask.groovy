package at.irian.typescript.gradle.task

import org.gradle.api.tasks.TaskAction
import at.irian.typescript.gradle.TypeScriptPluginExtension

class CleanMainTask extends AlwaysReRunTypescriptPluginTask {

    @TaskAction
    void clean() {
        project.delete(TypeScriptPluginExtension.getInstance(project).getGeneratedJsFiles().getFiles())
        project.delete(TypeScriptPluginExtension.getInstance(project).getGeneratedJsMapFiles().getFiles())
    }
}
