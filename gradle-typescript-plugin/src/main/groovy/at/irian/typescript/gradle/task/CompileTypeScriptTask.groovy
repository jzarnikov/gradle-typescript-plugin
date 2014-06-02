package at.irian.typescript.gradle.task

import at.irian.typescript.gradle.TypeScriptPluginExtension
import at.irian.typescript.gradle.util.PathsUtil
import at.irian.typescript.gradle.util.RunUtil
import com.google.common.base.Joiner
import com.google.common.collect.Lists
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Project
import org.gradle.api.file.FileTree
import org.gradle.process.ExecSpec

abstract class CompileTypeScriptTask extends TypeScriptPluginTask {

    void setupCompileInputsAndOutputs(TypeScriptPluginExtension extension, File sourceDir, File targetDir) {
        FileTree filesToCompile = extension.getFilesToCompile(sourceDir)
        inputs.files(filesToCompile)
        filesToCompile.each {File tsFile ->
            String relativePath = PathsUtil.getRelativePath(tsFile, sourceDir);
            outputs.file(new File(targetDir, relativePath.replaceAll(".ts\$", ".js")));
        }
    }

    static class CompilerRunner {

        private int maxCommandLineLength;

        private List<String> commandLine;
        private List<File> filesToCompile
        private File workingDir;

        public CompilerRunner(int maxCommandLineLength) {
            this.maxCommandLineLength = maxCommandLineLength;
            this.commandLine = RunUtil.getCommandLine(Lists.asList(RunUtil.getTscCommand()));
            this.filesToCompile = new ArrayList<String>();
        }

        public CompilerRunner() {
            // stupid windows: http://support.microsoft.com/kb/830473
            // supported max length of windows of 8191 - 10% for misc purposes
            this(Os.isFamily(Os.FAMILY_WINDOWS) ? 8191*0.9 : Integer.MAX_VALUE);
        }

        void addFile(File file) {
            this.filesToCompile.add(file);
        }

        void addOptions(String... options) {
            this.commandLine.addAll(options);
        }

        void workingDir(File workingDir) {
            this.workingDir = workingDir;
        }

        void run(Project project) {
            if (!this.filesToCompile.isEmpty()) {
                List<String> partialCommandLine = this.createPartialCommandLine();
                Iterator<File> filesToCompileIterator = this.filesToCompile.iterator();
                while(filesToCompileIterator.hasNext()) {
                    File fileToCompile = filesToCompileIterator.next();
                    if (Joiner.on(" ").join(partialCommandLine).length() < (this.maxCommandLineLength - fileToCompile.path.length() - 1)) {
                        partialCommandLine.add(fileToCompile.path);
                    } else {
                        this.execPartialCommandLine(partialCommandLine, project);
                        partialCommandLine = this.createPartialCommandLine();
                        partialCommandLine.add(fileToCompile.path);
                    }
                }
                this.execPartialCommandLine(partialCommandLine, project);
            }
        }

        private void execPartialCommandLine(List<String> commandLine, Project project) {
            project.exec {ExecSpec execSpec ->
                execSpec.commandLine(commandLine)
                if (this.workingDir != null) {
                    execSpec.workingDir(this.workingDir);
                }
            }
        }

        private List<String> createPartialCommandLine() {
            return new ArrayList<>(this.commandLine);
        }
    }

}
