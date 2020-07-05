package lumCode.folderScriptInterpreter.variables;

public class StringVariable extends Variable {
	String var;

	public StringVariable(String var) {
		super(VariableType.STRING);
		this.var = var;
	}

	protected StringVariable(String name, String var) {
		super(VariableType.STRING, name);
		this.var = var;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	@Override
	public String toString() {
		return var;
	}
}
