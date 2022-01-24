package lumCode.folderScriptInterpreter.exceptions;

import lumCode.folderScriptInterpreter.handlers.arithmetic.ArithmeticType;

public class ArithmeticErrorException extends InterpreterException {
	private static final long serialVersionUID = -4604857103845504803L;

	public ArithmeticErrorException(String l, ArithmeticType a, String r, String m) {
		super("Arithmetic expression '" + l + " " + a.getChar() + " " + r + "' failed with the following message: "
				+ m);
	}
}
