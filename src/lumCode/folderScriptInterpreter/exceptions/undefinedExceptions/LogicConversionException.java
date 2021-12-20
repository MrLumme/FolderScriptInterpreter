package lumCode.folderScriptInterpreter.exceptions.undefinedExceptions;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;

public class LogicConversionException extends InterpreterException {
	private static final long serialVersionUID = -644864876647259647L;

	public LogicConversionException(int var) {
		super("The value '" + var + "' can not be converted to boolean logic.");
	}

	public LogicConversionException(long var) {
		super("The value '" + var + "' can not be converted to boolean logic.");
	}
}
