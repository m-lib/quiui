package br.com.quiui.entities;

import lombok.Getter;
import lombok.Setter;

public class User {
	
	@Getter @Setter
	private long code;
	
	@Getter @Setter
	private String pass;
	
	@Getter @Setter
	private String login;
	
	@Getter @Setter
	private Person person;
	
	@Getter @Setter
	private UserGroup group;
	
	public User() {
		
	}

}
