package lumCode.folderScriptInterpreter.variables;

public class IntVariable extends Variable {
	int var;

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
}
