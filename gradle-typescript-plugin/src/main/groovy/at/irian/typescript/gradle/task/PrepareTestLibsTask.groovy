package at.irian.typescript.gradle.task

import org.gradle.api.tasks.TaskAction
import at.irian.typescript.gradle.util.ResourceUtil
import at.irian.typescript.gradle.TypeScriptPluginExtension

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
        ResourceUtil.copyResource(testLibsDir, "/test-resources/requirejs/require-2.1.11-patched.js")
    }


}
