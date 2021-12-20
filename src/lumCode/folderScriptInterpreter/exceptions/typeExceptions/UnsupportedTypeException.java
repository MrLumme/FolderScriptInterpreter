package lumCode.folderScriptInterpreter.exceptions.typeExceptions;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;

public class UnsupportedTypeException extends InterpreterException {
	private static final long serialVersionUID = -8540818204257617736L;

	public UnsupportedTypeException(String type, String value) {
		super("The " + type + " type '" + value + "' is not supported");
	}

	public UnsupportedTypeException(char value) {
		super("The operator '" + value + "' is not supported");
	}
}
