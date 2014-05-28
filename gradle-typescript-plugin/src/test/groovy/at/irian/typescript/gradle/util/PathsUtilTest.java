package at.irian.typescript.gradle.util;

import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class PathsUtilTest {

    @Test
    public void testRelativePathFromTheSameDir() throws IOException {
        File tmpFile = File.createTempFile("test", "tmp");
        String relativePath = PathsUtil.getRelativePath(tmpFile, tmpFile.getParentFile());
        Assert.assertEquals(tmpFile.getName(), relativePath);
        tmpFile.delete();
    }

    @Test
    public void testRelativePathFromParentDir() throws IOException {
        File parentDir = Files.createTempDir();
        File subDir = new File(parentDir, "subdir");
        subDir.mkdir();
        File tmpFile = new File(subDir, "file");
        String relativePath = PathsUtil.getRelativePath(tmpFile, parentDir);
        Assert.assertEquals("subdir/file", relativePath);
    }

    @Test
    public void testRelativePathFromChild() throws IOException {
        File parentDir = Files.createTempDir();
        File subDir = new File(parentDir, "subdir");
        subDir.mkdir();
        File tmpFile = new File(parentDir, "file");
        String relativePath = PathsUtil.getRelativePath(tmpFile, subDir);
        Assert.assertEquals("../file", relativePath);
    }

    @Test
    public void testRelativePathFromSiblingDir() throws IOException {
        File parentDir = Files.createTempDir();
        File subDir = new File(parentDir, "subdir");
        subDir.mkdir();
        File siblingDir = new File(parentDir, "siblingDir");
        siblingDir.mkdir();
        File tmpFile = new File(subDir, "file");
        String relativePath = PathsUtil.getRelativePath(tmpFile, siblingDir);
        Assert.assertEquals("../subdir/file", relativePath);
    }

    @Test
    public void testRelativePathFromDeepSiblingDir() throws IOException {
        File root = Files.createTempDir();
        File mainDir = new File(root, "main");
        File mainTypeScriptDir = new File(mainDir, "typescript");
        mainTypeScriptDir.mkdirs();
        File testDir = new File(root, "test");
        File testTypeScriptDir = new File(testDir, "typescript");
        testTypeScriptDir.mkdirs();
        File testFile = new File(testTypeScriptDir, "file.ts");
        String relativePath = PathsUtil.getRelativePath(testFile, mainTypeScriptDir);
        Assert.assertEquals("../../test/typescript/file.ts", relativePath);
    }

    @Test
    public void testIsFileInDir() throws IOException {
        File tmpFile = File.createTempFile("file", "tmp");
        Assert.assertTrue(PathsUtil.isFileInDirRecursive(tmpFile, tmpFile.getParentFile()));
    }

    @Test
    public void testIsFileInSubDir() throws IOException {
        File subDir = Files.createTempDir();
        File tmpFile = new File(subDir, "file");
        Assert.assertTrue(PathsUtil.isFileInDirRecursive(tmpFile, subDir.getParentFile()));
    }

    @Test
    public void testIsFileInSiblingDir() throws IOException {
        File parentDir = Files.createTempDir();
        File subDir = new File(parentDir, "subdir");
        subDir.mkdir();
        File tmpFile = new File(parentDir, "file");
        Assert.assertFalse(PathsUtil.isFileInDirRecursive(tmpFile, subDir));
    }

}
