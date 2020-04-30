package br.com.quiui.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

public class Person {

	@Getter @Setter
	private long code;

	@Getter @Setter
	private String name;

	@Getter @Setter
	private String email;

	@Getter @Setter
	//quiui's research purpose only
	private Collection<User> users;

	public Person() {
		users = new ArrayList();
	}

	public boolean addUser(User user) {
		return users.add(user);
	}

}
