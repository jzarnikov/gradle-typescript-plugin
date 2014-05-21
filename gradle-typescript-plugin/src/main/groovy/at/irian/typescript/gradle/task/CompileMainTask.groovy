package at.irian.typescript.gradle.task

import org.gradle.api.file.FileTree
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec
import at.irian.typescript.gradle.TypeScriptPluginExtension
import at.irian.typescript.gradle.util.RunUtil

class CompileMainTask extends CompileTypeScriptTask {


    void setupInputsAndOutputs(TypeScriptPluginExtension extension) {
        setupCompileInputsAndOutputs(extension, extension.getSourceDir(), extension.getGeneratedJsDir());
    }

    @TaskAction
    void compile() {
        TypeScriptPluginExtension extension = TypeScriptPluginExtension.getInstance(project)
        File outputDir = extension.getGeneratedJsDir()
        String[] tscOptions = extension.tscOptions
        FileTree tsFilesTree = extension.getFilesToCompile(extension.getSourceDir())
        if (!tsFilesTree.isEmpty()) {
            List<String> compileCommand = RunUtil.getCommandLine([RunUtil.getTscCommand(), "--module", "amd", "--outDir", outputDir.path])
            compileCommand.addAll(tscOptions);

            tsFilesTree.each {File tsFile -> compileCommand.add(tsFile.path)}
            project.exec {ExecSpec execSpec ->
                execSpec.commandLine(compileCommand);
            }
        }
    }

}
