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
        File requirejsOptimizerTemplateFile = File.createTempFile("optimizeConfig-genearator-template", "js")
        requirejsOptimizerTemplateFile << CombineGeneratedJsTask.class.getResourceAsStream("/build-resources/optimizeConfig-generator-template.js")
        for(String module : extension.combineJsModules) {
            Map<String, Object> templateVariables = [
                requirejsConfigPath: extension.requireJsConfig,
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
                execSpec.commandLine RunUtil.getCommandLine([RunUtil.getNodeCommand(), optimizeConfigGeneratorFileName])
                execSpec.workingDir extension.generatedJsDir
            }
            String optimizeConfigFileName = module + "-optimizeConfig.js";
            project.exec {ExecSpec execSpec ->
                execSpec.commandLine RunUtil.getCommandLine([RunUtil.getRjsCommand(), "-o", optimizeConfigFileName])
                execSpec.workingDir extension.generatedJsDir
            }

            project.delete(new File(extension.getGeneratedJsDir(), optimizeConfigGeneratorFileName))
            project.delete(new File(extension.getGeneratedJsDir(), optimizeConfigFileName))
        }
        project.delete(requirejsOptimizerTemplateFile)
    }
}
