package lumCode.folderScriptInterpreter.exceptions.arrayExceptions;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;

public class ArrayPositionEmptyException extends InterpreterException {
	private static final long serialVersionUID = -642813150259985493L;

	public ArrayPositionEmptyException(int numb) {
		super("No variable found at position '" + numb + "' in array.");
	}

}
