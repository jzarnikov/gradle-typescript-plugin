package at.irian.typescript.gradle.util

import org.apache.tools.ant.taskdefs.condition.Os

class RunUtil {

    static List<String> getCommandLine(List<String> commandAndArguments) {
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            List<String> commandLine = new ArrayList<>(["cmd", "/c"])
            commandLine.addAll(commandAndArguments);
            return commandLine
        } else {
            return new ArrayList<>(commandAndArguments);
        }
    }

    private static String getCommand(String defaultCommand, String envVarialbeName) {
        if (System.getenv()[envVarialbeName] != null) {
            return System.getenv()[envVarialbeName]
        } else {
            return defaultCommand
        }
    }

}


