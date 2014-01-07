package org.typescriptlang.gradle.task

import org.gradle.api.file.CopySpec
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec
import org.typescriptlang.gradle.TypeScriptPluginExtension

class CompileTestTask extends TypeScriptPluginTask {

    @Override
    void setupInputsAndOutputs(TypeScriptPluginExtension extension) {
        inputs.files(extension.getFilesToCompile().files)
        inputs.files(extension.getTestFilesToCompile().files)
        outputs.dir(extension.getMainSourceCopyForTestDir())
        outputs.dir(extension.getTestSourceCopyForTestDir())
    }

    @TaskAction
    void compileTest() {
        File sourceCopyDir =makeSourceCopyDir();
        File mainSourceDir = makeMainSourceDir();
        copyMainSourceFiles(mainSourceDir);
        File testSourceDir = makeTestSourceDir();
        copyTestSourceFiles(testSourceDir);
        compileTestSource(sourceCopyDir, testSourceDir);
    }

    private File makeSourceCopyDir() {
        File dir = TypeScriptPluginExtension.getInstance(project).getSourceCopyForTestDir();
        dir.mkdirs();
        return dir;
    }

    private File makeMainSourceDir() {
        File dir = TypeScriptPluginExtension.getInstance(project).getMainSourceCopyForTestDir()
        dir.mkdirs();
        return dir;
    }

    private void copyMainSourceFiles(File destination) {
        project.copy {CopySpec copySpec ->
            copySpec.into(destination)
            copySpec.from(TypeScriptPluginExtension.getInstance(project).getSourceDir())
            copySpec.include("**/*")
        }
    }

    private File makeTestSourceDir() {
        File dir = TypeScriptPluginExtension.getInstance(project).getTestSourceCopyForTestDir()
        dir.mkdirs();
        return dir;
    }

    private void copyTestSourceFiles(File destination) {
        project.copy {CopySpec copySpec ->
            copySpec.into(destination)
            copySpec.from(TypeScriptPluginExtension.getInstance(project).getTestSourceDir())
            copySpec.include("**/*")
        }
    }

    private void compileTestSource(File workingDir, File testSourceDir) {
        FileTree tsTestFilesTree = project.fileTree(testSourceDir).include('**/*.ts').exclude("**/*.d.ts");

        List<String> compileCommand = ["tsc", "--module", "amd"];

        tsTestFilesTree.each {File tsFile -> compileCommand.add(tsFile.path)}
        project.exec {ExecSpec execSpec ->
            execSpec.workingDir(workingDir)
            execSpec.commandLine(compileCommand)
        }
    }

}
