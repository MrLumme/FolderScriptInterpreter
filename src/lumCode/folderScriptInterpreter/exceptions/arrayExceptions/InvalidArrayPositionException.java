package lumCode.folderScriptInterpreter.exceptions.arrayExceptions;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;

public class InvalidArrayPositionException extends InterpreterException {
	private static final long serialVersionUID = -3194901585775580137L;

	public InvalidArrayPositionException(String numb) {
		super("The array position '" + numb + "' is invalid.");
	}

}
