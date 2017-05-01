package io.bootique.bom.logback;

import io.bootique.BQRuntime;
import io.bootique.command.CommandOutcome;
import io.bootique.test.TestIO;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.*;

public class LogbackAppIT {

    @Rule
    public LogbackApp app = new LogbackApp();

    private File prepareLogFile(String name) {
        File logFile = new File(name);
        logFile.delete();
        assertFalse(logFile.exists());

        return logFile;
    }

    @Test
    public void testRun_Help() {

        TestIO io = TestIO.noTrace();

        CommandOutcome outcome = app.app("--help").bootLogger(io.getBootLogger()).createRuntime().run();
        assertEquals(0, outcome.getExitCode());

        assertTrue(io.getStdout().contains("--help"));
        assertTrue(io.getStdout().contains("--config"));
    }

    @Test
    public void testRun_Debug() throws IOException {
        File logFile = prepareLogFile("target/logback/testRun_Debug.log");

        BQRuntime runtime = app
                .app("--config=src/test/resources/io/bootique/bom/logback/test-debug.yml").createRuntime();
        CommandOutcome outcome = runtime.run();

        // stopping runtime to ensure the logs are flushed before we start making assertions...
        runtime.shutdown();

        assertEquals(0, outcome.getExitCode());

        assertTrue(logFile.isFile());

        String logfileContents = Files.lines(logFile.toPath()).collect(joining("\n"));
        assertTrue(logfileContents.contains("i.b.b.l.LogbackTestCommand: logback-test-debug"));
        assertTrue(logfileContents.contains("i.b.b.l.LogbackTestCommand: logback-test-info"));
        assertTrue(logfileContents.contains("i.b.b.l.LogbackTestCommand: logback-test-warn"));
        assertTrue(logfileContents.contains("i.b.b.l.LogbackTestCommand: logback-test-error"));
    }

    @Test
    public void testRun_Warn() throws IOException {
        File logFile = prepareLogFile("target/logback/testRun_Warn.log");

        BQRuntime runtime = app.app("--config=src/test/resources/io/bootique/bom/logback/test-warn.yml")
                .createRuntime();
        CommandOutcome outcome = runtime.run();

        // stopping runtime to ensure the logs are flushed before we start making assertions...
        runtime.shutdown();

        assertEquals(0, outcome.getExitCode());

        assertTrue(logFile.isFile());

        String logfileContents = Files.lines(logFile.toPath()).collect(joining("\n"));
        assertFalse(logfileContents.contains("i.b.b.l.LogbackTestCommand: logback-test-debug"));
        assertFalse(logfileContents.contains("i.b.b.l.LogbackTestCommand: logback-test-info"));
        assertTrue("Logfile contents: " + logfileContents,
                logfileContents.contains("i.b.b.l.LogbackTestCommand: logback-test-warn"));
        assertTrue(logfileContents.contains("i.b.b.l.LogbackTestCommand: logback-test-error"));
    }

}
