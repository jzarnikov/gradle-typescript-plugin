package at.irian.typescript.gradle.task

import at.irian.typescript.gradle.TypeScriptPluginExtension

abstract class AlwaysReRunTypescriptPluginTask extends TypeScriptPluginTask {

    @Override
    void setupInputsAndOutputs(TypeScriptPluginExtension extension) {
        outputs.upToDateWhen {false}
    }
}
