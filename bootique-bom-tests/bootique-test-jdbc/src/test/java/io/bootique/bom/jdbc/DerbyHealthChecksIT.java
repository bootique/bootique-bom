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

import io.bootique.BQRuntime;
import io.bootique.jdbc.DataSourceFactory;
import io.bootique.jdbc.instrumented.tomcat.healthcheck.TomcatConnectivityCheck;
import io.bootique.junit5.BQTest;
import io.bootique.junit5.BQTestFactory;
import io.bootique.junit5.BQTestTool;
import io.bootique.metrics.health.HealthCheckOutcome;
import io.bootique.metrics.health.HealthCheckRegistry;
import io.bootique.metrics.health.HealthCheckStatus;
import org.junit.jupiter.api.Test;

import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@BQTest
public class DerbyHealthChecksIT {

    @BQTestTool
    final BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void testDerbyHealth() {

        BQRuntime runtime = testFactory.app("--config=classpath:db.yml").autoLoadModules().createRuntime();

        HealthCheckRegistry healthChecks = runtime.getInstance(HealthCheckRegistry.class);
        Map<String, HealthCheckOutcome> results = healthChecks.runHealthChecks();

        // check before DataSources are started
        HealthCheckOutcome one = results.get(TomcatConnectivityCheck.healthCheckName("derby1"));
        assertNull(one);

        HealthCheckOutcome two = results.get(TomcatConnectivityCheck.healthCheckName("derby2"));
        assertNull(two);

        HealthCheckOutcome three = results.get(TomcatConnectivityCheck.healthCheckName("derby3"));
        assertNull(three);

        // init DataSources and re-check
        runtime.getInstance(DataSourceFactory.class).forName("derby1");
        runtime.getInstance(DataSourceFactory.class).forName("derby2");
        runtime.getInstance(DataSourceFactory.class).forName("derby3");


        results = healthChecks.runHealthChecks();

        one = results.get(TomcatConnectivityCheck.healthCheckName("derby1"));
        assertNotNull(one);
        assertEquals(HealthCheckStatus.OK, one.getStatus());

        two = results.get(TomcatConnectivityCheck.healthCheckName("derby2"));
        assertNotNull(two);
        assertEquals(HealthCheckStatus.OK, two.getStatus());

        three = results.get(TomcatConnectivityCheck.healthCheckName("derby3"));
        assertNotNull(three);
        assertEquals(HealthCheckStatus.CRITICAL, three.getStatus());
    }
}
