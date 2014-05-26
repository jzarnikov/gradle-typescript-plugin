package at.irian.typescript.gradle.task

import org.gradle.api.file.CopySpec
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec
import at.irian.typescript.gradle.TypeScriptPluginExtension
import at.irian.typescript.gradle.util.RunUtil

class CompileTestTask extends CompileTypeScriptTask {

    @Override
    void setupInputsAndOutputs(TypeScriptPluginExtension extension) {
        setupCompileInputsAndOutputs(extension, extension.getSourceDir(), extension.getMainSourceCopyForTestDir());
        setupCompileInputsAndOutputs(extension, extension.getTestSourceDir(), extension.getTestSourceCopyForTestDir());
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
        FileTree tsTestFilesTree = project.fileTree(testSourceDir).include('**/*.ts').exclude("**/*.d.ts")
        CompilerRunner runner = new CompilerRunner();
        runner.addOptions("--module", "amd");
        tsTestFilesTree.each {File tsFile -> runner.addFile(tsFile)}
        runner.workingDir(workingDir);
        runner.run(project);
    }

}
