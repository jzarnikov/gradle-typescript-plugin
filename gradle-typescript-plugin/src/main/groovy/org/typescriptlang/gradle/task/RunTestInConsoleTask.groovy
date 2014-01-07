package org.typescriptlang.gradle.task

import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec
import org.typescriptlang.gradle.util.PathsUtil
import org.typescriptlang.gradle.TypeScriptPluginExtension

class RunTestInConsoleTask extends AlwaysReRunTypescriptPluginTask {

    @TaskAction
    void runTests() {
        TypeScriptPluginExtension extension = TypeScriptPluginExtension.getInstance(project)
        File phantomjsTestRunnerJsFile = new File(extension.getTestLibsDir(), "phantomjs-testrunner.js");
        File consoleTestsHtmlFile = new File(extension.getTestHtmlDir(), "console-test.html");
        project.exec {ExecSpec execSpec ->
            execSpec.commandLine("phantomjs", PathsUtil.getRelativePath(phantomjsTestRunnerJsFile, project.getProjectDir()), consoleTestsHtmlFile.absolutePath);
        }
    }
}
