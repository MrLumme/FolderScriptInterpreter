package lumCode.folderScriptInterpreter.exceptions;

import lumCode.folderScriptInterpreter.handlers.arithmetic.ArithmeticType;

public class UndefinedArithmeticException extends UndefinedException {
	private static final long serialVersionUID = -675794863639635267L;

	public UndefinedArithmeticException(String l, ArithmeticType a, String r)
			throws UnsupportedArithmeticTypeException {
		super("arithmetic", l + " " + a.getChar() + " " + r);
	}

}
