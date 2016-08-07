package com.nhl.bootique.bom.job;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.nhl.bootique.job.BaseJob;
import com.nhl.bootique.job.JobMetadata;
import com.nhl.bootique.job.runnable.JobResult;

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
