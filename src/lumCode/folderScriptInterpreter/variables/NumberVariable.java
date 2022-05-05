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

	public boolean asBoolean() throws LogicConversionException {
		if (var == 0) {
			return false;
		} else if (var == 1) {
			return true;
		}
		throw new LogicConversionException(var);
	}

	public boolean isBoolean() {
		try {
			asBoolean();
			return true;
		} catch (LogicConversionException e) {
			return false;
		}
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
