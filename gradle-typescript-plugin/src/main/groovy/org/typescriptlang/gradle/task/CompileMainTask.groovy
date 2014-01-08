package org.typescriptlang.gradle.task

import org.gradle.api.file.FileTree
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec
import org.typescriptlang.gradle.TypeScriptPluginExtension
import org.typescriptlang.gradle.util.RunUtil

class CompileMainTask extends TypeScriptPluginTask {


    void setupInputsAndOutputs(TypeScriptPluginExtension extension) {
        inputs.files(extension.getFilesToCompile().files)
        outputs.files(extension.getGeneratedJsFiles().files)
    }

    @TaskAction
    void compile() {
        TypeScriptPluginExtension extension = TypeScriptPluginExtension.getInstance(project)
        File outputDir = extension.getGeneratedJsDir()
        String[] tscOptions = extension.tscOptions
        FileTree tsFilesTree = extension.getFilesToCompile()

        List<String> compileCommand = RunUtil.getCommandLine([RunUtil.getTscCommand(), "--module", "amd", "--outDir", outputDir.path])
        compileCommand.addAll(tscOptions);

        tsFilesTree.each {File tsFile -> compileCommand.add(tsFile.path)}
        project.exec {ExecSpec execSpec ->
            execSpec.commandLine(compileCommand);
        }
    }

}
