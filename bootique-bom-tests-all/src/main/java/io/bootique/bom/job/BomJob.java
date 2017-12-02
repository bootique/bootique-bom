package io.bootique.bom.job;

import io.bootique.job.BaseJob;
import io.bootique.job.JobMetadata;
import io.bootique.job.runnable.JobResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class BomJob extends BaseJob {

	private static final Logger LOGGER = LoggerFactory.getLogger(BomJob.class);

	public static final AtomicLong COUNTER = new AtomicLong();

	public BomJob() {
		super(JobMetadata.build(BomJob.class));
	}

	@Override
	public JobResult run(Map<String, Object> parameters) {
		LOGGER.info("Running job");
		BomJob.COUNTER.incrementAndGet();
		return JobResult.success(getMetadata());
	}
}
