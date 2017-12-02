package io.bootique.bom.job;

import io.bootique.command.CommandOutcome;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JobAppIT {

    private JobApp app;

    @Before
    public void before() {
        this.app = new JobApp();
    }

    @Test
    public void testRun_Help() {
        CommandOutcome outcome = app.run("--help");
        assertEquals(0, outcome.getExitCode());

        String help = app.getStdout();

        assertTrue(help.contains("--exec"));
        assertTrue(help.contains("--list"));
        assertTrue(help.contains("--schedule"));
        assertTrue(help.contains("--help"));
        assertTrue(help.contains("--config"));
        assertTrue(help.contains("--job"));
    }

    @Test
    public void testList() {
        CommandOutcome outcome = app.run("--list");
        assertEquals(0, outcome.getExitCode());

        String stdout = app.getStdout();
        assertTrue(stdout.contains("- bom"));
    }

    @Test
    public void testExec() {

        BomJob.COUNTER.set(0);

        CommandOutcome outcome = app.run("--exec", "--job=bom");
        assertEquals(0, outcome.getExitCode());
        assertEquals(1l, BomJob.COUNTER.get());
    }

    @Test
    public void testSchedule() {

        BomJob.COUNTER.set(0);
        BomParameterizedJob.COUNTER.set(0);

        CommandOutcome outcome = app.run(r -> {
                    // wait for scheduler to run jobs...
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                },
                "--schedule", "-c", "classpath:io/bootique/bom/job/test.yml");

        assertEquals(0, outcome.getExitCode());

        assertTrue("Unexpected job count: " + BomJob.COUNTER.get(), BomJob.COUNTER.get() > 3);
        assertTrue(BomParameterizedJob.COUNTER.get() > 17 * 3);
        assertEquals(0, BomParameterizedJob.COUNTER.get() % 17);
    }

}
