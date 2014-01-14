package at.irian.typescript.gradle.task

import at.irian.typescript.gradle.TypeScriptPluginExtension
import org.gradle.api.DefaultTask
import org.gradle.api.file.CopySpec
import org.gradle.api.tasks.TaskAction

class CopyVendorJsTask extends TypeScriptPluginTask {

    @Override
    void setupInputsAndOutputs(TypeScriptPluginExtension extension) {
        inputs.files(project.fileTree(new File(extension.getSourceDir(), extension.vendorPath)).include("**/*.js"))
        outputs.files(project.fileTree(new File(extension.getGeneratedJsDir(), extension.vendorPath)).include("**/*"))
    }

    @TaskAction
    void copy() {
        TypeScriptPluginExtension extension = TypeScriptPluginExtension.getInstance(project)
        project.copy {CopySpec copySpec ->
            from new File(extension.getSourceDir(), extension.vendorPath)
            include "**/*.js"
            into new File(extension.getGeneratedJsDir(), extension.vendorPath)
        }
    }
}
