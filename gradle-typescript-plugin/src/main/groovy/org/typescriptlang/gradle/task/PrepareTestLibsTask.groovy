package org.typescriptlang.gradle.task

import org.gradle.api.tasks.TaskAction
import org.typescriptlang.gradle.util.ResourceUtil
import org.typescriptlang.gradle.TypeScriptPluginExtension

class PrepareTestLibsTask extends TypeScriptPluginTask {

    @Override
    void setupInputsAndOutputs(TypeScriptPluginExtension extension) {
        outputs.dir(extension.getTestLibsDir())
    }

    @TaskAction
    void prepareTest() {
        File testLibsDir = TypeScriptPluginExtension.getInstance(project).getTestLibsDir()
        testLibsDir.mkdirs()

        ResourceUtil.copyResource(testLibsDir, "/test-resources/jasmine/jasmine.js")
        ResourceUtil.copyResource(testLibsDir, "/test-resources/jasmine/es5-shim.js")
        ResourceUtil.copyResource(testLibsDir, "/test-resources/jasmine/jasmine-html.js")
        ResourceUtil.copyResource(testLibsDir, "/test-resources/jasmine/phantomjs-testrunner.js")
        ResourceUtil.copyResource(testLibsDir, "/test-resources/jasmine/jasmine.console_reporter.js")
        ResourceUtil.copyResource(testLibsDir, "/test-resources/jasmine/jasmine.terminal_reporter.js")
        ResourceUtil.copyResource(testLibsDir, "/test-resources/jasmine/jasmine.css")
        ResourceUtil.copyResource(testLibsDir, "/test-resources/requirejs/require-2.1.8.js")
    }


}
