package br.com.quiui.entities;

import java.util.Set;
import java.util.HashSet;
import java.util.Calendar;

import java.io.Serializable;

public class Activity implements Serializable {

	private static final long serialVersionUID = 1L;

	private long code;

	private String name;
	private double value;
	private Calendar begin;
	private Calendar finish;
	private String description;
	private Set<UserGroup> groups;

	public Activity() {
		groups = new HashSet();
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Calendar getBegin() {
		return begin;
	}

	public void setBegin(Calendar begin) {
		this.begin = begin;
	}

	public Calendar getFinish() {
		return finish;
	}

	public void setFinish(Calendar finish) {
		this.finish = finish;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<UserGroup> getGroups() {
		return groups;
	}

	public void setGroups(Set<UserGroup> groups) {
		this.groups = groups;
	}

}
