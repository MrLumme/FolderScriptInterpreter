package lumCode.folderScriptInterpreter.variables;

public class StringVariable extends Variable {
	String var;

	public StringVariable(String var) {
		this.var = var;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

}
