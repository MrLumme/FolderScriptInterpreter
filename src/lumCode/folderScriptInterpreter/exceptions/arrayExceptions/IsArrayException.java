package lumCode.folderScriptInterpreter.exceptions.arrayExceptions;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;

public class IsArrayException extends InterpreterException {
	private static final long serialVersionUID = 2254986892177882216L;

	public IsArrayException(String name) {
		super("No non-array type variable of name '" + name + "' exists.");
	}

}
