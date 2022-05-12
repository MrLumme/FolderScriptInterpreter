package lumCode.folderScriptInterpreter.variables;

public class TextVariable extends Variable {
	private String var;
	private boolean regex;

	public TextVariable(String var) {
		this(var, false);
	}

	private TextVariable(String var, boolean regex) {
		super(VariableType.TEXT);
		this.var = var;
		this.regex = regex;
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

	@Override
	public TextVariable copy() {
		return new TextVariable(var, regex);
	}
}
