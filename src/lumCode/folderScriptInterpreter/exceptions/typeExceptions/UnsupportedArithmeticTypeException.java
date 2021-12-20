package lumCode.folderScriptInterpreter.exceptions.typeExceptions;

import lumCode.folderScriptInterpreter.handlers.arithmetic.ArithmeticType;

public class UnsupportedArithmeticTypeException extends UnsupportedTypeException {
	private static final long serialVersionUID = -5806879396163411451L;

	public UnsupportedArithmeticTypeException(ArithmeticType a) {
		super("arithmetic", a.toString());
	}

}
