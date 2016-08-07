package io.bootique.bom.linkrest;

import com.nhl.link.rest.annotation.LrAttribute;
import com.nhl.link.rest.annotation.LrId;

public class ITEntity {

	private int id;
	private String name;

	@LrAttribute
	public String getName() {
		return name;
	}

	@LrId
	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(int id) {
		this.id = id;
	}
}
