package io.bootique.bom.mvc.r1;

import com.nhl.bootique.mvc.AbstractView;

public class T1View extends AbstractView {

	private String name;

	public T1View(String name) {
		super("t1.mustache");
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
