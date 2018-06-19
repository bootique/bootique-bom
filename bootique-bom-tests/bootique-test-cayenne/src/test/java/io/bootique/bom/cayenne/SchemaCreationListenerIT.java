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

package io.bootique.bom.cayenne;

import io.bootique.BQRuntime;
import io.bootique.jdbc.test.DatabaseChannel;
import io.bootique.jdbc.test.Table;
import io.bootique.test.junit.BQTestFactory;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.junit.Rule;
import org.junit.Test;

public class SchemaCreationListenerIT {

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void testSchemaPresent1() {
        runWithFreshStack();
    }

    @Test
    public void testSchemaPresent2() {
        // checking that we can survive the previous test shutdown...
        runWithFreshStack();
    }

    private void runWithFreshStack() {
        BQRuntime runtime = testFactory.app("--config=classpath:db.yml").autoLoadModules().createRuntime();

        runtime.getInstance(ServerRuntime.class);

        // check that schema was created by Cayenne and the queries can run...

        Table t1 = DatabaseChannel.get(runtime).newTable("T1").columnNames("id", "name").build();

        t1.matcher().assertNoMatches();
        t1.insert(1, "one").insert(2, "two");
        t1.matcher().assertMatches(2);
    }
}
