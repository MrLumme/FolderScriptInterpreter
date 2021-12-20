package lumCode.folderScriptInterpreter.variables;

import lumCode.folderScriptInterpreter.exceptions.undefinedExceptions.LogicConversionException;

public class NumberVariable extends Variable {
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
}
