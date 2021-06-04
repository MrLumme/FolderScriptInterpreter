package lumCode.folderScriptInterpreter.exceptions;

public class VariableIsArrayException extends InterpreterException {
	private static final long serialVersionUID = 2254986892177882216L;

	public VariableIsArrayException(String name) {
		super("No non-array type variable of name '" + name + "' exists.");
	}

}
