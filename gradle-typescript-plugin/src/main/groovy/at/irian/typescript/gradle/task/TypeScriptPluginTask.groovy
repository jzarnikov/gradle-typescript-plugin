package at.irian.typescript.gradle.task

import org.gradle.api.DefaultTask
import at.irian.typescript.gradle.TypeScriptPluginExtension

abstract class TypeScriptPluginTask extends DefaultTask {

    abstract void setupInputsAndOutputs(TypeScriptPluginExtension extension);
}
