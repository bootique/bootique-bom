/*
 * Licensed to ObjectStyle LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ObjectStyle LLC licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.bootique.bom.logback;

import io.bootique.BQCoreModule;
import io.bootique.BQRuntime;
import io.bootique.command.CommandOutcome;
import io.bootique.logback.LogbackModuleProvider;
import io.bootique.test.junit.BQTestFactory;
import io.bootique.test.junit.TestIO;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.*;

public class LogbackAppIT {

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    private BQTestFactory.Builder appBuilder(String... args) {
        return testFactory.app(args)
                .moduleProvider(new LogbackModuleProvider())
                .module(b -> BQCoreModule.extend(b).addCommand(LogbackTestCommand.class));
    }

    private File prepareLogFile(String name) {
        File logFile = new File(name);
        logFile.delete();
        assertFalse(logFile.exists());

        return logFile;
    }

    @Test
    public void testHelp() {

        TestIO io = TestIO.noTrace();

        CommandOutcome outcome = appBuilder("--help").bootLogger(io.getBootLogger()).run();
        assertEquals(0, outcome.getExitCode());

        assertTrue(io.getStdout().contains("--help"));
        assertTrue(io.getStdout().contains("--config"));
        assertTrue(io.getStdout().contains("--logback-test"));
    }

    @Test
    public void testLogToFile_Debug() throws IOException {
        File logFile = prepareLogFile("target/logback/testRun_Debug.log");

        BQRuntime runtime = appBuilder(
                "--config=src/test/resources/io/bootique/bom/logback/test-debug.yml",
                "--logback-test")
                .createRuntime();

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
    public void testLogToFile_Warn() throws IOException {
        File logFile = prepareLogFile("target/logback/testRun_Warn.log");

        BQRuntime runtime = appBuilder(
                "--config=src/test/resources/io/bootique/bom/logback/test-warn.yml",
                "--logback-test")
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
