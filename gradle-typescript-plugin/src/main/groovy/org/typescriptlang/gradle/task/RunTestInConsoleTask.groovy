package org.typescriptlang.gradle.task

import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec
import org.typescriptlang.gradle.util.PathsUtil
import org.typescriptlang.gradle.TypeScriptPluginExtension
import org.typescriptlang.gradle.util.RunUtil

class RunTestInConsoleTask extends AlwaysReRunTypescriptPluginTask {

    @TaskAction
    void runTests() {
        TypeScriptPluginExtension extension = TypeScriptPluginExtension.getInstance(project)
        File phantomjsTestRunnerJsFile = new File(extension.getTestLibsDir(), "phantomjs-testrunner.js")
        File consoleTestsHtmlFile = new File(extension.getTestHtmlDir(), "console-test.html")
        String relativePhantomjsRunnerPath = PathsUtil.getRelativePath(phantomjsTestRunnerJsFile, project.getProjectDir())
        project.exec {ExecSpec execSpec ->
            execSpec.commandLine(RunUtil.getCommandLine([RunUtil.getPhantomjsCommand(), relativePhantomjsRunnerPath, consoleTestsHtmlFile.path]));
        }
    }
}
