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

    static String getTscCommand() {
        if (System.getenv()['TSC_COMMAND'] != null) {
            return System.getenv()['TSC_COMMAND']
        } else {
            return "tsc"
        }
    }

    static String getPhantomjsCommand() {
        if (System.getenv()['PHANTOMJS_COMMAND'] != null) {
            return System.getenv()['PHANTOMJS_COMMAND']
        } else {
            return "phantomjs"
        }
    }

}


