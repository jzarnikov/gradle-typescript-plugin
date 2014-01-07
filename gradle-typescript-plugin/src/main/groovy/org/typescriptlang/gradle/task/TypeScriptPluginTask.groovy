package org.typescriptlang.gradle.task

import org.gradle.api.DefaultTask
import org.typescriptlang.gradle.TypeScriptPluginExtension

abstract class TypeScriptPluginTask extends DefaultTask {

    abstract void setupInputsAndOutputs(TypeScriptPluginExtension extension);
}
