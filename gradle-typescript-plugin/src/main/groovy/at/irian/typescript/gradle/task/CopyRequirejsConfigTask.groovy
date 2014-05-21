package at.irian.typescript.gradle.task

import at.irian.typescript.gradle.TypeScriptPluginExtension
import org.gradle.api.file.CopySpec
import org.gradle.api.tasks.TaskAction

class CopyRequirejsConfigTask extends TypeScriptPluginTask {

    @Override
    void setupInputsAndOutputs(TypeScriptPluginExtension extension) {
        inputs.file(new File(extension.getSourceDir(), extension.requireJsConfig))
        outputs.file(new File(extension.getGeneratedJsDir(), extension.requireJsConfig))
    }

    @TaskAction
    void copy() {
        TypeScriptPluginExtension extension = TypeScriptPluginExtension.getInstance(project)
        File requireJsConfigFile = new File(extension.getSourceDir(), extension.requireJsConfig)
        if (requireJsConfigFile.exists()) {
            project.copy {CopySpec copySpec ->
                copySpec.from requireJsConfigFile
                copySpec.into extension.getGeneratedJsDir()
            }
        } else {
            throw new FileNotFoundException(requireJsConfigFile.absolutePath)
        }
    }
}
