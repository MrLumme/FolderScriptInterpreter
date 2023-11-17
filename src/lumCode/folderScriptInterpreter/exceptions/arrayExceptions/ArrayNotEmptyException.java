package lumCode.folderScriptInterpreter.exceptions.arrayExceptions;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;

public class ArrayNotEmptyException extends InterpreterException {
	private static final long serialVersionUID = 5105577701690811797L;

	public ArrayNotEmptyException(String action) {
		super("Array is not empty; " + action + ".");
	}

}
