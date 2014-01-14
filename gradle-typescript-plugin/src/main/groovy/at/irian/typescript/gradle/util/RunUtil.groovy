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

    static String getTscCommand() {
        return getCommand("tsc", "TSC_COMMAND")
    }

    static String getPhantomjsCommand() {
        return getCommand("phantomjs", "PHANTOMJS_COMMAND")
    }

    static String getNodejsCommand() {
        return getCommand("nodejs", "NODEJS_COMMAND")
    }

    static String getRjsCommand() {
        return getCommand("r.js", "RJS_COMMAND")
    }

}


