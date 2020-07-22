package lumCode.folderScriptInterpreter.handlers.arithmetic;

import lumCode.folderScriptInterpreter.exceptions.UnsupportedArithmeticTypeException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedTypeException;

public enum ArithmeticType {
	ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, MODULO;

	public static ArithmeticType fromChar(char c) throws UnsupportedTypeException {
		switch (c) {
		case '+':
			return ADDITION;
		case '/':
			return DIVISION;
		case '%':
			return MODULO;
		case '*':
			return MULTIPLICATION;
		case '-':
			return SUBTRACTION;
		}
		throw new UnsupportedTypeException(c);
	}

	public static char toChar(ArithmeticType c) throws UnsupportedArithmeticTypeException {
		switch (c) {
		case ADDITION:
			return '+';
		case DIVISION:
			return '/';
		case MODULO:
			return '%';
		case MULTIPLICATION:
			return '*';
		case SUBTRACTION:
			return '-';
		}
		throw new UnsupportedArithmeticTypeException(c);
	}

	public static boolean valid(char c) {
		switch (c) {
		case '+':
		case '/':
		case '%':
		case '*':
		case '-':
			return true;
		default:
			return false;
		}
	}
}
