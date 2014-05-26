package at.irian.typescript.gradle.task;

import at.irian.typescript.gradle.util.RunUtil;
import groovy.lang.Closure;
import org.gradle.api.Project;
import org.gradle.process.ExecSpec;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CompileTypeScriptTaskTest {

    @Test
    public void testCompileNoFiles() {
        CompileTypeScriptTask.CompilerRunner runner = new CompileTypeScriptTask.CompilerRunner();
        List<ExecSpecMockArguments> mockArguments = new ArrayList<>();
        runner.run(this.mockProject(mockArguments));
        Assert.assertTrue(mockArguments.isEmpty());
    }

    @Test
    public void testCompileSingleFile() {
        CompileTypeScriptTask.CompilerRunner runner = new CompileTypeScriptTask.CompilerRunner();
        runner.addFile(new File("test.ts"));
        List<ExecSpecMockArguments> mockArguments = new ArrayList<>();
        runner.run(this.mockProject(mockArguments));
        Assert.assertEquals(1, mockArguments.size());
        Assert.assertEquals(RunUtil.getCommandLine(Arrays.asList("tsc", "--module", "amd", "test.ts")), mockArguments.get(0).commandLine);
    }


    @Test
    public void testCompileSingleFileWithOptions() {
        CompileTypeScriptTask.CompilerRunner runner = new CompileTypeScriptTask.CompilerRunner();
        runner.addOptions("--sourcemap");
        runner.addFile(new File("test.ts"));
        List<ExecSpecMockArguments> mockArguments = new ArrayList<>();
        runner.run(this.mockProject(mockArguments));
        Assert.assertEquals(1, mockArguments.size());
        Assert.assertEquals(RunUtil.getCommandLine(Arrays.asList("tsc", "--module", "amd", "--sourcemap", "test.ts")), mockArguments.get(0).commandLine);
    }

    @Test
    public void testCompileSingleFileWithOptionsAddedAfterFile() {
        CompileTypeScriptTask.CompilerRunner runner = new CompileTypeScriptTask.CompilerRunner();
        runner.addFile(new File("test.ts"));
        runner.addOptions("--sourcemap");
        List<ExecSpecMockArguments> mockArguments = new ArrayList<>();
        runner.run(this.mockProject(mockArguments));
        Assert.assertEquals(1, mockArguments.size());
        Assert.assertEquals(RunUtil.getCommandLine(Arrays.asList("tsc", "--module", "amd", "--sourcemap", "test.ts")), mockArguments.get(0).commandLine);
    }

    @Test
    public void testCompileSingleFileWithinMaxCommandLineLengthLimit() {
        CompileTypeScriptTask.CompilerRunner runner = new CompileTypeScriptTask.CompilerRunner(100);
        runner.addFile(new File("test.ts"));
        List<ExecSpecMockArguments> mockArguments = new ArrayList<>();
        runner.run(this.mockProject(mockArguments));
        Assert.assertEquals(1, mockArguments.size());
        Assert.assertEquals(RunUtil.getCommandLine(Arrays.asList("tsc", "--module", "amd", "test.ts")), mockArguments.get(0).commandLine);
    }

    @Test
    public void testCompileTwoFilesOverMaxCommandLineLengthLimit() {
        CompileTypeScriptTask.CompilerRunner runner = new CompileTypeScriptTask.CompilerRunner("tsc --module amd test2.ts".length());
        runner.addFile(new File("test.ts"));
        runner.addFile(new File("test2.ts"));
        List<ExecSpecMockArguments> mockArguments = new ArrayList<>();
        runner.run(this.mockProject(mockArguments));
        Assert.assertEquals(2, mockArguments.size());
        Assert.assertEquals(RunUtil.getCommandLine(Arrays.asList("tsc", "--module", "amd", "test.ts")), mockArguments.get(0).commandLine);
        Assert.assertEquals(RunUtil.getCommandLine(Arrays.asList("tsc", "--module", "amd", "test2.ts")), mockArguments.get(1).commandLine);
    }

    @Test
    public void testSetWorkingDir() throws IOException {
        CompileTypeScriptTask.CompilerRunner runner = new CompileTypeScriptTask.CompilerRunner();
        runner.addFile(new File("test.ts"));
        File tempFile = File.createTempFile("gradle-typescript-plugin", "");
        runner.workingDir(tempFile.getParentFile());
        List<ExecSpecMockArguments> mockArguments = new ArrayList<>();
        runner.run(this.mockProject(mockArguments));
        Assert.assertEquals(1, mockArguments.size());
        Assert.assertEquals(tempFile.getParentFile(), mockArguments.get(0).workingDir);
        tempFile.delete();
    }

    private Project mockProject(final List<ExecSpecMockArguments> execSpecMockArgumentsList) {
        Project mock = Mockito.mock(Project.class);
        Mockito.when(mock.exec(Mockito.any(Closure.class))).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                final ExecSpecMockArguments execSpecMockArguments = new ExecSpecMockArguments();
                ExecSpec execSpecMock = Mockito.mock(ExecSpec.class);
                Mockito.when(execSpecMock.commandLine(Mockito.anyVararg())).thenAnswer(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock commandLineInvocation) throws Throwable {
                        execSpecMockArguments.commandLine = Arrays.<String>asList(((String[]) commandLineInvocation.getArguments()));
                        return null;
                    }
                });
                Mockito.when(execSpecMock.commandLine(Mockito.anyCollection())).thenAnswer(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock commandLineInvocation) throws Throwable {
                        execSpecMockArguments.commandLine = new ArrayList<String>((Collection<String>) commandLineInvocation.getArguments()[0]);
                        return null;
                    }
                });
                Mockito.when(execSpecMock.workingDir(Mockito.any())).thenAnswer(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        execSpecMockArguments.workingDir = (File) invocation.getArguments()[0];
                        return null;
                    }
                });

                Closure execSpecClosure = (Closure) invocation.getArguments()[0];
                execSpecClosure.call(execSpecMock);
                execSpecMockArgumentsList.add(execSpecMockArguments);
                return null;
            }
        });
        return mock;
    }

    private static class ExecSpecMockArguments {
        List<String> commandLine;
        File workingDir;
    }
}
