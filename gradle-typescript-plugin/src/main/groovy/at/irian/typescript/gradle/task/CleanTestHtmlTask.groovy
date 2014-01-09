package at.irian.typescript.gradle.task

import org.gradle.api.tasks.TaskAction
import at.irian.typescript.gradle.TypeScriptPluginExtension

class CleanTestHtmlTask extends AlwaysReRunTypescriptPluginTask {

    @TaskAction
    void clean() {
        File testHtmlDir = TypeScriptPluginExtension.getInstance(project).getTestHtmlDir()
        File browserTestHtml = new File (testHtmlDir, "browser-test.html");
        File consoleTestHtml = new File (testHtmlDir, "console-test.html");
        project.delete(browserTestHtml, consoleTestHtml)
    }
}
