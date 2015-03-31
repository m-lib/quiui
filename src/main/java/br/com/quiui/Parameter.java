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
package br.com.quiui;

public class Parameter {
	
	private final String path;
	private Operation operation;
	private final String[] chain;
	private final Comparable<?> value;

	public Parameter(final String path, final Comparable<?> value) {
		chain = path.split("\\.");
		this.value = value;
		this.path = path;
	}
	
	public Comparable<?> getValue() {
		return value;
	}
	
	public String getPath(int index) {
		return chain[index];
	}

	public String getPath() {
		return path;
	}
	
	public int getChainSize() {
		return chain.length;
	}
	
	public String remove(String value) {
		return path.substring(path.indexOf(value) + value.length() + 1);
	}

	public String[] getChain() {
		return chain;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

}
