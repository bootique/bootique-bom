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
import io.bootique.jdbc.tomcat.JdbcTomcatModuleProvider;
import io.bootique.test.junit.BQTestFactory;
import io.bootique.test.junit.TestIO;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class JdbcAppIT {

	@Rule
	public BQTestFactory testFactory = new BQTestFactory();

	private BQTestFactory.Builder appBuilder(String... args) {
		return testFactory.app(args)
				.moduleProvider(new JdbcTomcatModuleProvider())
				.module(b -> BQCoreModule.extend(b).addCommand(RunSQLCommand.class));
	}

	@Test
	public void testRun_Help() {

		TestIO io = TestIO.noTrace();
		CommandOutcome outcome = appBuilder("--help").bootLogger(io.getBootLogger()).run();
		assertEquals(0, outcome.getExitCode());

		String help = io.getStdout();
		assertTrue(help.contains("--run-sql"));
		assertTrue(help.contains("--sql"));
	}

	@Test
	public void testRun_SQL_1() {

        TestIO io = TestIO.noTrace();
        CommandOutcome outcome = appBuilder(
                "--config=src/test/resources/io/bootique/bom/jdbc/test.yml",
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
	public void testRun_SQL_2() {

        TestIO io = TestIO.noTrace();

		CommandOutcome outcome = appBuilder(
		        "--config=src/test/resources/io/bootique/bom/jdbc/test.yml",
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
