package lumCode.folderScriptInterpreter.variables;

public class IntVariable extends Variable {
	int var;

	public IntVariable(int var) {
		this.var = var;
	}

	public int getVar() {
		return var;
	}

	public void setVar(int var) {
		this.var = var;
	}
}
