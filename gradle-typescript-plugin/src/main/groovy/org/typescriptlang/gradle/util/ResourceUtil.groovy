package org.typescriptlang.gradle.util

class ResourceUtil {

    static void copyResource(File destinationDir, String resourcePath) {
        String fileName = resourcePath.substring(resourcePath.lastIndexOf("/"))
        File destinationFile = new File(destinationDir, fileName)
        InputStream resourceAsStream = ResourceUtil.class.getResourceAsStream(resourcePath)
        destinationFile << resourceAsStream.text
    }
}
