package at.irian.typescript.gradle.util

class PathsUtil {

    static String getRelativePath(File file, File baseDir) {
        return baseDir.toURI().relativize(file.toURI()).getPath();
    }
}
