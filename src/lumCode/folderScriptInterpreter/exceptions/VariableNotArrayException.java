package lumCode.folderScriptInterpreter.exceptions;

public class VariableNotArrayException extends InterpreterException {
	private static final long serialVersionUID = 2254986892177882216L;

	public VariableNotArrayException(String name) {
		super("No array type variable of name '" + name + "' exists.");
	}

}
