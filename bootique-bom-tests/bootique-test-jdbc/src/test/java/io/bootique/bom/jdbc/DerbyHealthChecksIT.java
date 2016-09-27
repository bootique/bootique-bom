package io.bootique.bom.jdbc;

import io.bootique.metrics.health.HealthCheckOutcome;
import io.bootique.metrics.health.HealthCheckRegistry;
import io.bootique.test.BQTestRuntime;
import io.bootique.test.junit.BQTestFactory;
import org.junit.Rule;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DerbyHealthChecksIT {

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void testDerbyHealth() {

        BQTestRuntime runtime = testFactory.app("--config=classpath:db.yml").autoLoadModules().createRuntime();

        HealthCheckRegistry healthchecks = runtime.getRuntime().getInstance(HealthCheckRegistry.class);
        Map<String, HealthCheckOutcome> results = healthchecks.runHealthChecks();

        assertEquals(3, results.size());

        HealthCheckOutcome one = results.get("derby1");
        assertNotNull(one);
        assertTrue(one.isHealthy());

        HealthCheckOutcome two = results.get("derby2");
        assertNotNull(two);
        assertTrue(two.isHealthy());

        HealthCheckOutcome three = results.get("derby3");
        assertNotNull(three);
        assertFalse(three.isHealthy());
    }
}
