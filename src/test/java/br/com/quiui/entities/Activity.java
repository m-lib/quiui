package br.com.quiui.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.HashSet;
import java.util.Calendar;

import java.io.Serializable;

public class Activity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter @Setter
	private long code;

	@Getter @Setter
	private String name;
	@Getter @Setter
	private double value;
	@Getter @Setter
	private Calendar begin;
	@Getter @Setter
	private Calendar finish;
	@Getter @Setter
	private String description;
	@Getter @Setter
	private Set<UserGroup> groups;

	public Activity() {
		groups = new HashSet();
	}

}
