package at.irian.typescript.gradle.task

import org.gradle.api.file.CopySpec
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.TaskAction
import at.irian.typescript.gradle.util.PathsUtil
import at.irian.typescript.gradle.TypeScriptPluginExtension

import java.util.regex.Matcher
import java.util.regex.Pattern

class GenerateTestHtmlTask extends TypeScriptPluginTask {

    @Override
    void setupInputsAndOutputs(TypeScriptPluginExtension extension) {
        inputs.dir(extension.getFilesToCompile(extension.getSourceDir()))
        inputs.dir(extension.getFilesToCompile(extension.getTestSourceDir()))
        outputs.file(new File(extension.getSourceCopyForTestDir(), "console-test.html"))
        outputs.file(new File(extension.getSourceCopyForTestDir(), "browser-test.html"))
    }

    @TaskAction
    void generate() {
        TypeScriptPluginExtension extension = TypeScriptPluginExtension.getInstance(project)
        File testHtmlDir = extension.getTestHtmlDir()
        testHtmlDir.mkdirs()

        File consoleTestTemplate = File.createTempFile("console-test-template", "html");
        consoleTestTemplate.deleteOnExit();
        consoleTestTemplate << GenerateTestHtmlTask.class.getResourceAsStream("/test-resources/template/console-test.html.template")
        File browserTestTemplate = File.createTempFile("browser-test-template", "html");
        browserTestTemplate.deleteOnExit();
        browserTestTemplate << GenerateTestHtmlTask.class.getResourceAsStream("/test-resources/template/browser-test.html.template")

        File testSourceDir = extension.getTestSourceCopyForTestDir()
        File mainSourceDir = extension.getMainSourceCopyForTestDir()

        List<String> testPathsToCompile = extension.getTestFilePaths();
        FileTree tsTestFilesTree;
        if (testPathsToCompile.isEmpty()) {
            tsTestFilesTree = project.fileTree(testSourceDir).include('**/*.ts').exclude("**/*.d.ts")
        } else {
            tsTestFilesTree = project.fileTree(testSourceDir).include(testPathsToCompile);
        }
        def testTsFiles = []
        tsTestFilesTree.visit({
            fileVisitDetails -> if (!fileVisitDetails.isDirectory()) {
                testTsFiles.add(fileVisitDetails.getRelativePath())
            }
        })



        File requireJsConfigFile = extension.getRequireJsConfigFileInSourceCopyDir()
        if (!requireJsConfigFile.exists()) {
            throw new FileNotFoundException(requireJsConfigFile.absolutePath);
        }
        Map<String, Object> templateVariableValues = [
                testFiles: testTsFiles,
                testJsLibs: extension.getTestLibsDir().path,
                requireJsConfigFile: requireJsConfigFile.path,
                requireJsConfigFilePathRelativeFromBuild: PathsUtil.getRelativePath(requireJsConfigFile.parentFile, project.getBuildDir()),
                testSourcesRelativeToMainSources: PathsUtil.getRelativePath(testSourceDir, mainSourceDir),
                testSourcesUri: testSourceDir.toURI(),
                requireJsGlobalVarName: extension.requireJsGlobalVarName
        ]
        project.copy {CopySpec copySpec ->
            copySpec.into testHtmlDir
            from browserTestTemplate, consoleTestTemplate
            rename {
                String originalName ->
                    // rename temporary file with random name "console-test-template-4s5h7ds.html" to "console-test.html", same for browser-test-...
                    Matcher filenameMatcher = Pattern.compile("^([a-z]+-test)-template.*").matcher(originalName);
                    filenameMatcher.find();
                    return filenameMatcher.group(1) + ".html";
            }
            expand(templateVariableValues)
        }

    }
}
