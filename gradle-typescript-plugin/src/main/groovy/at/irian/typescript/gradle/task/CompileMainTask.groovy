package at.irian.typescript.gradle.task

import at.irian.typescript.gradle.TypeScriptPluginExtension
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.TaskAction

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

        CompilerRunner runner = new CompilerRunner();
        runner.addOptions("--module", "amd", "--outDir", outputDir.path);
        runner.addOptions(tscOptions);

        tsFilesTree.each {File tsFile -> runner.addFile(tsFile)}
        runner.run(project);
    }

}
