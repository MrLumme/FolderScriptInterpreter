package lumCode.folderScriptInterpreter.variables;

import lumCode.folderScriptInterpreter.exceptions.LogicConversionException;

public class IntVariable extends Variable {
	private int var;

	public IntVariable(int var) {
		super(VariableType.INT);
		this.var = var;
	}

	protected IntVariable(String name, int var) {
		super(VariableType.INT, name);
		this.var = var;
	}

	public int getVar() {
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
}
