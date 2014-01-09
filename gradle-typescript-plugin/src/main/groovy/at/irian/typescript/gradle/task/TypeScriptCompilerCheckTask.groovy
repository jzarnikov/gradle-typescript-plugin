package at.irian.typescript.gradle.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec
import at.irian.typescript.gradle.util.RunUtil

class TypeScriptCompilerCheckTask extends DefaultTask {

    @TaskAction
    void check() {
        project.exec {ExecSpec execSpec ->
            execSpec.commandLine(RunUtil.getCommandLine([RunUtil.getTscCommand(), "--version"]))
        }
    }
}
