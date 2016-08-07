package io.bootique.bom.job;

import io.bootique.job.BaseJob;
import io.bootique.job.JobMetadata;
import io.bootique.job.runnable.JobResult;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class BomJob extends BaseJob {

	public static final AtomicLong COUNTER = new AtomicLong();

	public BomJob() {
		super(JobMetadata.build(BomJob.class));
	}

	@Override
	public JobResult run(Map<String, Object> parameters) {
		BomJob.COUNTER.incrementAndGet();
		return JobResult.success(getMetadata());
	}
}
