package at.irian.typescript.gradle;

import groovy.lang.MissingPropertyException;
import org.gradle.api.Project;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;

public class TypeScriptPluginExtensionTest {

    @Test
    public void testGetTestFilePathsWithPropertyNotSet() {
        TypeScriptPluginExtension extension = new TypeScriptPluginExtension(this.getProjectMock(null));
        final List<String> testFilePaths = extension.getTestFilePaths();
        Assert.assertNotNull(testFilePaths);
        Assert.assertTrue(testFilePaths.isEmpty());
    }

    @Test
    public void testGetTestFilePathsWithPropertyEmpty() {
        TypeScriptPluginExtension extension = new TypeScriptPluginExtension(this.getProjectMock(null));
        final List<String> testFilePaths = extension.getTestFilePaths();
        Assert.assertNotNull(testFilePaths);
        Assert.assertTrue(testFilePaths.isEmpty());
    }

    @Test
    public void testGetTestFilePathsWithPropertySetToSingleFileWithTsExtension() {
        TypeScriptPluginExtension extension = new TypeScriptPluginExtension(this.getProjectMock("myTest.ts"));
        final List<String> testFilePaths = extension.getTestFilePaths();
        Assert.assertNotNull(testFilePaths);
        Assert.assertEquals(1, testFilePaths.size());
        Assert.assertEquals("myTest.ts", testFilePaths.get(0));
    }

    @Test
    public void testGetTestFilePathsWithPropertySetToSingleFileWithoutTsExtension() {
        TypeScriptPluginExtension extension = new TypeScriptPluginExtension(this.getProjectMock("myTest"));
        final List<String> testFilePaths = extension.getTestFilePaths();
        Assert.assertNotNull(testFilePaths);
        Assert.assertEquals(1, testFilePaths.size());
        Assert.assertEquals("myTest.ts", testFilePaths.get(0));
    }

    @Test
    public void testGetTestFilePathsWithPropertySetToMultipleFileWithTsExtension() {
        TypeScriptPluginExtension extension = new TypeScriptPluginExtension(this.getProjectMock("myTest.ts,myOtherTest.ts"));
        final List<String> testFilePaths = extension.getTestFilePaths();
        Assert.assertNotNull(testFilePaths);
        Assert.assertEquals(2, testFilePaths.size());
        Assert.assertEquals("myTest.ts", testFilePaths.get(0));
        Assert.assertEquals("myOtherTest.ts", testFilePaths.get(1));
    }

    @Test
    public void testGetTestFilePathsWithPropertySetToMultipleFileWithoutTsExtension() {
        TypeScriptPluginExtension extension = new TypeScriptPluginExtension(this.getProjectMock("myTest,myOtherTest"));
        final List<String> testFilePaths = extension.getTestFilePaths();
        Assert.assertNotNull(testFilePaths);
        Assert.assertEquals(2, testFilePaths.size());
        Assert.assertEquals("myTest.ts", testFilePaths.get(0));
        Assert.assertEquals("myOtherTest.ts", testFilePaths.get(1));
    }

    @Test
    public void testGetTestFilePathsWithPropertySetToMultipleFileSomeWithSomeWithoutTsExtension() {
        TypeScriptPluginExtension extension = new TypeScriptPluginExtension(this.getProjectMock("myTest,myOtherTest.ts"));
        final List<String> testFilePaths = extension.getTestFilePaths();
        Assert.assertNotNull(testFilePaths);
        Assert.assertEquals(2, testFilePaths.size());
        Assert.assertEquals("myTest.ts", testFilePaths.get(0));
        Assert.assertEquals("myOtherTest.ts", testFilePaths.get(1));
    }

    private Project getProjectMock(final Object testPropertyValue) {
        Project mock = Mockito.mock(Project.class);
        Mockito.when(mock.property("at.irian.typescript.test")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if (testPropertyValue != null) {
                    return testPropertyValue;
                } else {
                    throw new MissingPropertyException("No Property at.irian.typescript.test");
                }
            }
        });
        return mock;
    }
}
