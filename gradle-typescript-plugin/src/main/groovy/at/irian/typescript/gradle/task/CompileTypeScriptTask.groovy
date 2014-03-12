package at.irian.typescript.gradle.task

import org.gradle.api.file.FileTree
import at.irian.typescript.gradle.TypeScriptPluginExtension
import at.irian.typescript.gradle.util.PathsUtil;


abstract class CompileTypeScriptTask extends TypeScriptPluginTask {

    void setupCompileInputsAndOutputs(TypeScriptPluginExtension extension, File sourceDir, File targetDir) {
        FileTree filesToCompile = extension.getFilesToCompile(sourceDir)
        inputs.files(filesToCompile)
        filesToCompile.each {File tsFile ->
            String relativePath = PathsUtil.getRelativePath(tsFile, sourceDir);
            outputs.file(new File(targetDir, relativePath.replaceAll(".ts\$", ".js")));
        }
    }

}
