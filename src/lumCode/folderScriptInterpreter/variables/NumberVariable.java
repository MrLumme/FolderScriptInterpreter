package lumCode.folderScriptInterpreter.variables;

import lumCode.folderScriptInterpreter.handlers.NumberNode;

public class NumberVariable extends Variable implements NumberNode {
	private long var;

	public NumberVariable(long var) {
		super(VariableType.NUMBER);
		this.var = var;
	}

	public long getVar() {
		return var;
	}

	public void setVar(int var) {
		this.var = var;
	}

	@Override
	public String toString() {
		return "" + var;
	}

	@Override
	public NumberVariable copy() {
		return new NumberVariable(var);
	}

	@Override
	public NumberVariable result() {
		return this;
	}
}
