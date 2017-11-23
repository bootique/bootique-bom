package io.bootique.bom.jdbc;

import io.bootique.BQRuntime;
import io.bootique.metrics.health.HealthCheckOutcome;
import io.bootique.metrics.health.HealthCheckRegistry;
import io.bootique.metrics.health.HealthCheckStatus;
import io.bootique.test.junit.BQTestFactory;
import org.junit.Rule;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class DerbyHealthChecksIT {

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void testDerbyHealth() {

        BQRuntime runtime = testFactory.app("--config=classpath:db.yml").autoLoadModules().createRuntime();

        HealthCheckRegistry healthChecks = runtime.getInstance(HealthCheckRegistry.class);
        Map<String, HealthCheckOutcome> results = healthChecks.runHealthChecks();

        assertEquals(3, results.size());

        HealthCheckOutcome one = results.get("bq.jdbc.derby1.canConnect");
        assertNotNull(one);
        assertEquals(HealthCheckStatus.OK, one.getStatus());

        HealthCheckOutcome two = results.get("bq.jdbc.derby2.canConnect");
        assertNotNull(two);
        assertEquals(HealthCheckStatus.OK, two.getStatus());

        HealthCheckOutcome three = results.get("bq.jdbc.derby3.canConnect");
        assertNotNull(three);
        assertEquals(HealthCheckStatus.CRITICAL, three.getStatus());
    }
}
