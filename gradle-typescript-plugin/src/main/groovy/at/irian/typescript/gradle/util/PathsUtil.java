package at.irian.typescript.gradle.util;

import java.io.File;

public class PathsUtil {
    public static String getRelativePath(File file, File baseDir) {
        if (isFileInDirRecursive(file, baseDir)) {
            return baseDir.toURI().relativize(file.toURI()).getPath();
        } else {
            return getRelativePathRecursive(file, baseDir, "");
        }

    }

    private static String getRelativePathRecursive(File file, File baseDir, String prefix) {
        if (isFileInDirRecursive(file, baseDir)) {
            return prefix + baseDir.toURI().relativize(file.toURI()).getPath();
        } else if (baseDir.getParentFile() != null) {
            return prefix + getRelativePathRecursive(file, baseDir.getParentFile(), "../");
        } else {
            return file.toURI().toString();
        }

    }

    protected static boolean isFileInDirRecursive(File file, File dir) {
        if (file.getParentFile() != null) {
            return file.getParentFile().equals(dir) || isFileInDirRecursive(file.getParentFile(), dir);
        }
        return false;
    }

}
