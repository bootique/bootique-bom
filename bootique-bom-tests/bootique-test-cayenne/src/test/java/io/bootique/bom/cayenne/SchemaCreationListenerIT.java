package io.bootique.bom.cayenne;

import io.bootique.BQRuntime;
import io.bootique.jdbc.test.DatabaseChannel;
import io.bootique.jdbc.test.Table;
import io.bootique.test.junit.BQTestFactory;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

        assertEquals(0, t1.getRowCount());
        t1.insert(1, "one").insert(2, "two");
        assertEquals(2, t1.getRowCount());
    }
}
