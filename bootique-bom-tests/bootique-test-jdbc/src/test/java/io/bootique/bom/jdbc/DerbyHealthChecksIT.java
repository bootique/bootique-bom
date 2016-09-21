package io.bootique.bom.jdbc;

import com.codahale.metrics.health.HealthCheck;
import io.bootique.jdbc.test.junit.DerbyDatabase;
import io.bootique.metrics.healthcheck.HealthCheckRegistry;
import io.bootique.test.BQTestRuntime;
import io.bootique.test.junit.BQTestFactory;
import org.junit.Rule;
import org.junit.Test;

import java.util.SortedMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DerbyHealthChecksIT {

    @Rule
    public DerbyDatabase derby1 = new DerbyDatabase("derby1");

    @Rule
    public DerbyDatabase derby2 = new DerbyDatabase("derby2");

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void testDerbyHealth() {

        BQTestRuntime runtime = testFactory.app("--config=classpath:db.yml").autoLoadModules().createRuntime();

        HealthCheckRegistry healthchecks = runtime.getRuntime().getInstance(HealthCheckRegistry.class);
        SortedMap<String, HealthCheck.Result> results = healthchecks.runHealthChecks();

        assertEquals(3, results.size());

        HealthCheck.Result one = results.get("derby1");
        assertNotNull(one);
        assertTrue(one.isHealthy());

        HealthCheck.Result two = results.get("derby2");
        assertNotNull(two);
        assertTrue(two.isHealthy());

        HealthCheck.Result three = results.get("derby3");
        assertNotNull(three);
        assertFalse(three.isHealthy());
    }
}
