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

package io.bootique.bom.job;

import io.bootique.command.CommandOutcome;
import io.bootique.job.JobModule;
import io.bootique.junit5.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@BQTest
public class JobAppIT {

    @BQTestTool
    final BQTestFactory testFactory = new BQTestFactory();

    private TestRuntumeBuilder appBuilder(String... args) {
        return testFactory.app(args)
                .module(new JobModule())
                .module(b -> JobModule.extend(b).addJob(BomJob.class).addJob(BomParameterizedJob.class));
    }

    @Test
    public void run_Help() {

        TestIO io = TestIO.noTrace();
        CommandOutcome outcome = appBuilder("--help").bootLogger(io.getBootLogger()).run();
        assertEquals(0, outcome.getExitCode());

        String help = io.getStdout();

        assertTrue(help.contains("--exec"));
        assertTrue(help.contains("--list"));
        assertTrue(help.contains("--schedule"));
        assertTrue(help.contains("--help"));
        assertTrue(help.contains("--config"));
        assertTrue(help.contains("--job"));
    }

    @Test
    public void list() {
        TestIO io = TestIO.noTrace();
        CommandOutcome outcome = appBuilder("--list").bootLogger(io.getBootLogger()).run();

        assertEquals(0, outcome.getExitCode());
        assertTrue(io.getStdout().contains("- bom"));
    }

    @Test
    public void exec() {

        BomJob.COUNTER.set(0);

        CommandOutcome outcome = appBuilder("--exec", "--job=bom").run();
        assertEquals(0, outcome.getExitCode());
        assertEquals(1L, BomJob.COUNTER.get());
    }

    @Test
    public void schedule() throws InterruptedException {

        BomJob.COUNTER.set(0);
        BomParameterizedJob.COUNTER.set(0);

        CommandOutcome outcome = appBuilder("--schedule", "-c", "classpath:io/bootique/bom/job/test.yml").run();

        // wait for scheduler to run jobs...
        Thread.sleep(3000);

        assertEquals(0, outcome.getExitCode());

        assertTrue(BomJob.COUNTER.get() > 3, () -> "Unexpected job count: " + BomJob.COUNTER.get());
        assertTrue(BomParameterizedJob.COUNTER.get() > 17 * 3);
        assertEquals(0, BomParameterizedJob.COUNTER.get() % 17);
    }

}
