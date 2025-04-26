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

package io.bootique.bom.jdbc;

import io.bootique.BQCoreModule;
import io.bootique.command.CommandOutcome;
import io.bootique.jdbc.JdbcModule;
import io.bootique.jdbc.hikaricp.JdbcHikariCPModule;
import io.bootique.jdbc.junit5.derby.DerbyTester;
import io.bootique.junit5.BQTest;
import io.bootique.junit5.BQTestFactory;
import io.bootique.junit5.BQTestTool;
import io.bootique.junit5.TestIO;
import io.bootique.junit5.TestRuntumeBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@BQTest
public class JdbcAppIT {

	@BQTestTool
	final DerbyTester db = DerbyTester.db();

	@BQTestTool
	final BQTestFactory testFactory = new BQTestFactory();

	private TestRuntumeBuilder appBuilder(String... args) {
		return testFactory.app(args)
				.modules(JdbcHikariCPModule.class, JdbcModule.class)
				.module(db.moduleWithTestDataSource("test1"))
				.module(b -> BQCoreModule.extend(b).addCommand(RunSQLCommand.class));
	}

	@Test
    public void run_Help() {

		TestIO io = TestIO.noTrace();
		CommandOutcome outcome = appBuilder("--help").bootLogger(io.getBootLogger()).run();
		assertEquals(0, outcome.getExitCode());

		String help = io.getStdout();
		assertTrue(help.contains("--run-sql"));
		assertTrue(help.contains("--sql"));
	}

	@Test
    public void run_SQL_1() {

        TestIO io = TestIO.noTrace();
        CommandOutcome outcome = appBuilder(
                "--run-sql",
				"--sql=SELECT * FROM T1")
                .bootLogger(io.getBootLogger())
                .run();

		assertEquals(0, outcome.getExitCode());
		String data = io.getStdout();

		assertTrue(data.contains("1,aa"));
		assertTrue(data.contains("2,bb"));
	}

	@Test
    public void run_SQL_2() {

        TestIO io = TestIO.noTrace();

		CommandOutcome outcome = appBuilder(
                "--run-sql",
				"--sql=SELECT * FROM T1 WHERE ID = 2")
                .bootLogger(io.getBootLogger())
                .run();

		assertEquals(0, outcome.getExitCode());
		String data = io.getStdout();

		assertFalse(data.contains("1,aa"));
		assertTrue(data.contains("2,bb"));
	}
}
