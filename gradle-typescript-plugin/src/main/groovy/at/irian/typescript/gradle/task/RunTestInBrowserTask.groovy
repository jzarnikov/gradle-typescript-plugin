package at.irian.typescript.gradle.task

import org.gradle.api.tasks.TaskAction
import at.irian.typescript.gradle.TypeScriptPluginExtension

import java.awt.*

class RunTestInBrowserTask extends AlwaysReRunTypescriptPluginTask {

    @TaskAction
    void runTest() {
        File testHtmlDir = TypeScriptPluginExtension.getInstance(project).getTestHtmlDir()
        File htmlTestFile = new File(testHtmlDir, "browser-test.html")
        Desktop.desktop.browse(htmlTestFile.toURI())
    }
}
