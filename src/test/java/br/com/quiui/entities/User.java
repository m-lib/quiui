/*
 * Copyright 2015
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

	public User(UserGroup group) {
		this.group = group;
	}

}
