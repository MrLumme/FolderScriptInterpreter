package lumCode.folderScriptInterpreter.handlers.arithmetic;

import lumCode.folderScriptInterpreter.exceptions.UnsupportedArithmeticTypeException;

public class NumberArithmeticHandler {
	public static long interpret(long left, ArithmeticType operator, long right)
			throws UnsupportedArithmeticTypeException {
		switch (operator) {
		case ADDITION:
			return left + right;
		case DIVISION:
			return left / right;
		case MODULO:
			return left % right;
		case MULTIPLICATION:
			return left * right;
		case SUBTRACTION:
			return left - right;
		default:
			throw new UnsupportedArithmeticTypeException(operator);
		}
	}

	public static String interpret(long left, ArithmeticType operator, String right)
			throws UnsupportedArithmeticTypeException {
		switch (operator) {
		case ADDITION:
			return "" + left + right;
		default:
			throw new UnsupportedArithmeticTypeException(operator);
		}
	}
}
