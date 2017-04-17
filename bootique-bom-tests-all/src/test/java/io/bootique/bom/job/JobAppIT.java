package io.bootique.bom.job;

import io.bootique.command.CommandOutcome;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JobAppIT {

	private JobApp app;
	private ExecutorService executor;

	@Before
	public void before() {
		this.app = new JobApp();
		this.executor = Executors.newSingleThreadExecutor();
	}

	@After
	public void after() throws InterruptedException {
		executor.shutdownNow();
		executor.awaitTermination(3, TimeUnit.SECONDS);
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
	public void testSchedule() throws InterruptedException, ExecutionException, TimeoutException {

		BomJob.COUNTER.set(0);
		BomParameterizedJob.COUNTER.set(0);

		// since ScheduleCommand main thread blocks, run the server in a
		// separate thread...

		Future<CommandOutcome> result = executor.submit(() -> {
			return app.run("--config=classpath:io/bootique/bom/job/test.yml", "--schedule");
		});

		// wait for scheduler to start and run some jobs...
		Thread.sleep(3000);

		// since we exited via interrupt, the result of the --schedule command
		// will look like a failure
		executor.shutdownNow();
		CommandOutcome outcome = result.get(3, TimeUnit.SECONDS);
		assertEquals(1, outcome.getExitCode());
		
		assertTrue(BomJob.COUNTER.get() > 3);
		assertTrue(BomParameterizedJob.COUNTER.get() > 17 * 3);
		assertEquals(0, BomParameterizedJob.COUNTER.get() % 17);
	}

}
