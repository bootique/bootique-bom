package com.nhl.bootique.bom.linkrest;

import java.util.Arrays;

import com.nhl.link.rest.annotation.listener.SelectServerParamsApplied;
import com.nhl.link.rest.processor.BaseLinearProcessingStage;
import com.nhl.link.rest.runtime.processor.select.SelectContext;

public class PojoListener {

	// TODO: once we upgrade to LR 1.22 we can change the parameter type per
	// https://github.com/nhl/link-rest/issues/132

	@SelectServerParamsApplied
	public BaseLinearProcessingStage<SelectContext<ITEntity>, ITEntity> doSomethingWithTheFlow(SelectContext<ITEntity> context,
			BaseLinearProcessingStage<SelectContext<ITEntity>, ITEntity> next) {

		ITEntity e1 = new ITEntity();
		e1.setId(5);
		e1.setName("name5");

		ITEntity e2 = new ITEntity();
		e2.setId(6);
		e2.setName("name6");

		context.setObjects(Arrays.asList(e1, e2));

		// terminate further processing
		return null;
	}
}
