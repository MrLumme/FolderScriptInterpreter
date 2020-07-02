package lumCode.folderScriptInterpreter.handlers;

import lumCode.folderScriptInterpreter.exceptions.UnsupportedArithmeticTypeException;

public enum ArithmeticType {
	ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, MODULO;

	public static ArithmeticType fromChar(char c) throws UnsupportedArithmeticTypeException {
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
		throw new UnsupportedArithmeticTypeException("The operator '" + c + "' is not supported.");
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
		throw new UnsupportedArithmeticTypeException("The operator '" + c.toString() + "' is not supported.");
	}
}
