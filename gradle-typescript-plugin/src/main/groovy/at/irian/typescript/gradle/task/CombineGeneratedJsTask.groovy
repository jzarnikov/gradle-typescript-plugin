package at.irian.typescript.gradle.task

import at.irian.typescript.gradle.TypeScriptPluginExtension
import at.irian.typescript.gradle.util.RunUtil
import org.gradle.api.DefaultTask
import org.gradle.api.file.CopySpec
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec

class CombineGeneratedJsTask extends DefaultTask {

    @TaskAction
    void combine() {
        TypeScriptPluginExtension extension = TypeScriptPluginExtension.getInstance(project)
        File requirejsOptimizerTemplateFile = File.createTempFile("optimizeConfig-template", "js")
//        requirejsOptimizerTemplateFile.deleteOnExit()
        requirejsOptimizerTemplateFile << CombineGeneratedJsTask.class.getResourceAsStream("/build-resources/optimizeConfig-generator-template.js")
        File requirejsConfigFile = new File(extension.getGeneratedJsDir(), extension.requireJsConfig)
        for(String module : extension.combineJsModules) {
            Map<String, Object> templateVariables = [
                requirejsConfigPath: requirejsConfigFile.path,
                moduleName: module,
                includeLibsInCombinedJs: extension.includeLibsInCombinedJs
            ]
            String optimizeConfigGeneratorFileName = module + "-optimizeConfig-generator.js";
            project.copy {CopySpec copySpec ->
                copySpec.from requirejsOptimizerTemplateFile
                copySpec.into extension.generatedJsDir
                copySpec.rename { String originalFilename ->
                    return optimizeConfigGeneratorFileName
                }
                copySpec.expand templateVariables
            }
            project.exec {ExecSpec execSpec ->
                execSpec.commandLine RunUtil.getCommandLine([RunUtil.getNodejsCommand(), optimizeConfigGeneratorFileName])
                execSpec.workingDir extension.generatedJsDir
            }

            project.exec {ExecSpec execSpec ->
                execSpec.commandLine RunUtil.getCommandLine([RunUtil.getRjsCommand(), "-o", module + "-optimizeConfig.js"])
                execSpec.workingDir extension.generatedJsDir
            }
        }
    }
}
