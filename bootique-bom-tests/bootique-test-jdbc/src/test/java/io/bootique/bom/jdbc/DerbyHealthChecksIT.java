package io.bootique.bom.jdbc;

import io.bootique.BQRuntime;
import io.bootique.jdbc.DataSourceFactory;
import io.bootique.jdbc.instrumented.tomcat.healthcheck.TomcatConnectivityCheck;
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

        // check before DataSources are started
        HealthCheckOutcome one = results.get(TomcatConnectivityCheck.healthCheckName("derby1"));
        assertNotNull(one);
        assertEquals(HealthCheckStatus.UNKNOWN, one.getStatus());

        HealthCheckOutcome two = results.get(TomcatConnectivityCheck.healthCheckName("derby2"));
        assertNotNull(two);
        assertEquals(HealthCheckStatus.UNKNOWN, two.getStatus());

        HealthCheckOutcome three = results.get(TomcatConnectivityCheck.healthCheckName("derby3"));
        assertNotNull(three);
        assertEquals(HealthCheckStatus.UNKNOWN, three.getStatus());

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
