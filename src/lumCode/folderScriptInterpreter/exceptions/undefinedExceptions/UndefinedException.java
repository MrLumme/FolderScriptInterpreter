package lumCode.folderScriptInterpreter.exceptions.undefinedExceptions;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;

public class UndefinedException extends InterpreterException {
	private static final long serialVersionUID = -6485336190190650214L;

	public UndefinedException(String type, String value) {
		super("No " + type + " have been defined for setup '" + value + "'.");
	}

}
