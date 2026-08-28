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

import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.meta.module.ModuleMetadata;
import io.bootique.meta.module.ModulesMetadata;
import io.bootique.junit.BQApp;
import io.bootique.junit.BQTest;
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
    @DisplayName("Consul job lock module must auto-load without startup errors")
    public void autoLoaded() {

        // Not resolving LockHandler from DI on purpose: bootique-job-consul connects to a live Consul server as soon as its LockHandler is resolved
        boolean loaded = app.getInstance(ModulesMetadata.class).getModules().stream()
                .map(ModuleMetadata::getName)
                .anyMatch("ConsulJobModule"::equals);

        assertTrue(loaded, "ConsulJobModule was not auto-loaded");

        // check we can run a command
        assertTrue(app.run().isSuccess());
    }
}
