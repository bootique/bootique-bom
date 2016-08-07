package io.bootique.bom.job;

import io.bootique.job.BaseJob;
import io.bootique.job.JobMetadata;
import io.bootique.job.runnable.JobResult;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class BomParameterizedJob extends BaseJob {

	private static final String LONG_PARAMETER = "pl";
	private static final String DATE_PARAMETER = "pd";

	public static final AtomicLong COUNTER = new AtomicLong();

	public BomParameterizedJob() {
		super(JobMetadata.builder(BomParameterizedJob.class).longParam(LONG_PARAMETER).dateParam(DATE_PARAMETER).build());
	}

	@Override
	public JobResult run(Map<String, Object> parameters) {

		Long inc = (Long) parameters.get(LONG_PARAMETER);

		BomParameterizedJob.COUNTER.addAndGet(inc.longValue());
		return JobResult.success(getMetadata());
	}
}
