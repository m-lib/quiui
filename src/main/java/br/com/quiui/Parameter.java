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

@SuppressWarnings("all")
public class Parameter<T> {

	private final T value;
	private final String path;
	private final String[] chain;
	private Operation<T> operation;

	public Parameter(final String path, final T value) {
		this.chain = path.split("\\.");
		this.value = value;
		this.path = path;
	}

	public T getValue() {
		return value;
	}

	public String getPath() {
		return path;
	}

	public int getChainSize() {
		return chain.length;
	}

	public String[] getChain() {
		return chain;
	}

	public Operation getOperation() {
		return operation;
	}

	public String getPath(int index) {
		return chain[index];
	}

	public String remove(String value) {
		return path.substring(path.indexOf(value) + value.length() + 1);
	}

	public void setOperation(Operation<T> operation) {
		this.operation = operation;
	}

	public boolean next(String param) {
		for (int index = 0; index < chain.length; index++) {
			if (param.equals(chain[index])) {
				return (index + 1) < chain.length;
			}
		}

		return false;
	}

	public boolean isMemberOperation() {
		return operation.getType().equals(OperationType.IS_MEMBER) || operation.getType().equals(OperationType.IS_MEMBER_OR_EMPTY);
	}

}
