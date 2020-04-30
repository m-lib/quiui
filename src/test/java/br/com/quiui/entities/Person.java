package br.com.quiui.entities;

import java.util.ArrayList;
import java.util.Collection;

public class Person {

	private long code;

	private String name;
	private String email;
	private Collection<User> users; //quiui's research purpose only

	public Person() {
		users = new ArrayList();
	}

	public boolean addUser(User user) {
		return users.add(user);
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Collection<User> getUsers() {
		return users;
	}

	public void setUsers(Collection<User> users) {
		this.users = users;
	}

}
