package lumCode.folderScriptInterpreter.exceptions.arrayExceptions;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;

public class NotArrayException extends InterpreterException {
	private static final long serialVersionUID = 2254986892177882216L;

	public NotArrayException(String name) {
		super("No array type variable of name '" + name + "' exists.");
	}

}
