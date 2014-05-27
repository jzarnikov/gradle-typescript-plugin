package at.irian.typescript.gradle.task

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec
import at.irian.typescript.gradle.util.PathsUtil
import at.irian.typescript.gradle.TypeScriptPluginExtension
import at.irian.typescript.gradle.util.RunUtil

class RunTestInConsoleTask extends AlwaysReRunTypescriptPluginTask {

    @TaskAction
    void runTests() {
        TypeScriptPluginExtension extension = TypeScriptPluginExtension.getInstance(project)
        File phantomjsTestRunnerJsFile = new File(extension.getTestLibsDir(), "phantomjs-testrunner.js")
        File consoleTestsHtmlFile = new File(extension.getTestHtmlDir(), "console-test.html")
        String relativePhantomjsRunnerPath = PathsUtil.getRelativePath(phantomjsTestRunnerJsFile, project.getProjectDir())
        project.exec {ExecSpec execSpec ->
            execSpec.commandLine(RunUtil.getCommandLine([RunUtil.getPhantomjsCommand(), relativePhantomjsRunnerPath, consoleTestsHtmlFile.toURI()]));
        }
    }

}
