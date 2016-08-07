package io.bootique.bom.job;

import com.google.inject.Binder;
import com.google.inject.Module;
import io.bootique.Bootique;
import io.bootique.bom.BomTestApp;
import io.bootique.job.runtime.JobModule;

public class JobApp extends BomTestApp implements Module {

	@Override
	protected void configure(Bootique bootique) {
		bootique.module(JobModule.class).module(this);
	}

	@Override
	public void configure(Binder binder) {
		JobModule.contributeJobs(binder).addBinding().to(BomJob.class);
		JobModule.contributeJobs(binder).addBinding().to(BomParameterizedJob.class);
	}
}
