package lumCode.folderScriptInterpreter.handlers.arithmetic;

import lumCode.folderScriptInterpreter.exceptions.UndefinedArithmeticException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedArithmeticTypeException;

public class StringArithmeticHandler {
	public static String interpret(String left, ArithmeticType operator, String right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {
		switch (operator) {
		case ADDITION:
			return left + right;
		case SUBTRACTION:
			return left.replace(right, "");
		default:
			throw new UndefinedArithmeticException(left, operator, right);
		}
	}
}
