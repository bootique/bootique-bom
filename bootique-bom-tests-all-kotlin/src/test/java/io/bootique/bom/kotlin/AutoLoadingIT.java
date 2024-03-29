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
package io.bootique.bom.kotlin;

import io.agrest.runtime.AgRuntime;
import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.junit5.BQApp;
import io.bootique.junit5.BQTest;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@BQTest
public class AutoLoadingIT {

    @BQApp(skipRun = true)
    static final BQRuntime app = Bootique.app("--help")
            .autoLoadModules()
            .createRuntime();

    @Test
    @DisplayName("App must run --help command without startup errors due to eager initialization in modules")
    public void services() {

        // check DI is in good shape
        app.getInstance(AgRuntime.class);
        app.getInstance(Server.class);

        // check we can run a command
        assertTrue(app.run().isSuccess());
    }
}
