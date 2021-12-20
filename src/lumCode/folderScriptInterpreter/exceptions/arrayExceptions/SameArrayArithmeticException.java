package lumCode.folderScriptInterpreter.exceptions.arrayExceptions;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;

public class SameArrayArithmeticException extends InterpreterException {
	private static final long serialVersionUID = -2940125109116873112L;

	public SameArrayArithmeticException() {
		super("Can not perform arithmetics on the same array.");
	}

}
