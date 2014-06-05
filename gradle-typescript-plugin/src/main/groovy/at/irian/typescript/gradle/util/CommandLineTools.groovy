package at.irian.typescript.gradle.util

import com.google.common.collect.Lists
import org.apache.commons.io.output.NullOutputStream
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.process.ExecSpec

import java.text.MessageFormat

public enum CommandLineTools {
    TSC("tsc", "TSC_COMMAND", "--version", "TypeScript compiler"),
    NODE("node", "NODE_COMMAND", "--version", "NodeJS"),
    PHANTOMJS("phantomjs", "PHANTOMJS_COMMAND", "--version", "PhantomJS"),
    RJS(Os.isFamily(Os.FAMILY_WINDOWS) ? "r.js.cmd" : "r.js", "RJS_COMMAND", "-v", "RequireJS Optimizer");

    private String defaultCommand;
    private String configEnvVariableName;
    private String availabilityCheckArgument;
    private String name;

    private static final MessageFormat AVAILABILITY_FAILED_MESSAGE =
            new MessageFormat(
                    "{0} was not found on your system. " +
                    "You need the command \"{1}\" in your PATH. " +
                    "Alternatively you can set the environment variable {2} to point to the executable of the {0}.");

    CommandLineTools(String defaultCommand, String configEnvVariableName, String availabilityCheckArgument, String name) {
        this.defaultCommand = defaultCommand
        this.configEnvVariableName = configEnvVariableName
        this.availabilityCheckArgument = availabilityCheckArgument
        this.name = name
    }

    public String getCommand() {
        if (System.getenv().get(this.configEnvVariableName) != null) {
            return System.getenv().get(this.configEnvVariableName);
        } else {
            return this.defaultCommand;
        }
    }

    private List<String> getCheckAvailabilityCommand() {
        return Lists.newArrayList(this.getCommand(), this.availabilityCheckArgument);
    }

    public void checkAvailability(Project project) {
        try {
            project.exec {ExecSpec execSpec ->
                List<String> commandLine = RunUtil.getCommandLine(this.getCheckAvailabilityCommand());
                execSpec.commandLine(commandLine);
                execSpec.setStandardOutput(new NullOutputStream());
                execSpec.setErrorOutput(new NullOutputStream());
            }
        } catch (Exception e) {
            throw new GradleException(AVAILABILITY_FAILED_MESSAGE.format([name, defaultCommand, configEnvVariableName] as String[]), e);
        }


    }
}