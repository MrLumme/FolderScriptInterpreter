package lumCode.folderScriptInterpreter.exceptions;

public class VariableNameNotFoundException extends InterpreterException {
	private static final long serialVersionUID = -5823979350169533764L;

	public VariableNameNotFoundException(String var) {
		super("Could not found a variable named '" + var.substring(1) + "'.");
	}

}
