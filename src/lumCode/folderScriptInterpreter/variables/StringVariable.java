package lumCode.folderScriptInterpreter.variables;

public class StringVariable extends Variable {
	private String var;
	private boolean regex;

	protected StringVariable(String name, String var) {
		super(VariableType.STRING, name);
		this.var = var;
		this.regex = false;
	}

	public StringVariable(String var) {
		this(var, null);
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

	public boolean isRegex() {
		return regex;
	}

	public void setRegex(boolean regex) {
		this.regex = regex;
	}
}
