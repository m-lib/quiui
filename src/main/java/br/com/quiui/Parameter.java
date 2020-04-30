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
