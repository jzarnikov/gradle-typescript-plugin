package org.typescriptlang.gradle.task

import org.typescriptlang.gradle.TypeScriptPluginExtension

abstract class AlwaysReRunTypescriptPluginTask extends TypeScriptPluginTask {

    @Override
    void setupInputsAndOutputs(TypeScriptPluginExtension extension) {
        outputs.upToDateWhen {false}
    }
}
