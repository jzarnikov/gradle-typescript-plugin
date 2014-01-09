package at.irian.typescript.gradle.util

class ResourceUtil {

    static void copyResource(File destinationDir, String resourcePath) {
        String fileName = resourcePath.substring(resourcePath.lastIndexOf("/"))
        File destinationFile = new File(destinationDir, fileName)
        if (destinationFile.exists()) {
            destinationFile.delete()
        }
        InputStream resourceAsStream = ResourceUtil.class.getResourceAsStream(resourcePath)
        destinationFile << resourceAsStream.text
    }
}
