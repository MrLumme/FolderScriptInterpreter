package lumCode.folderScriptInterpreter.handlers.arithmetic;

import lumCode.folderScriptInterpreter.exceptions.UnsupportedTypeException;

public enum ArithmeticType {
	ADDITION('+'), SUBTRACTION('-'), MULTIPLICATION('*'), DIVISION('/'), MODULO('%');

	private char symbol;

	private ArithmeticType(char symbol) {
		this.symbol = symbol;
	}

	public static ArithmeticType fromChar(char c) throws UnsupportedTypeException {
		for (ArithmeticType t : ArithmeticType.values()) {
			if (t.symbol == c) {
				return t;
			}
		}
		throw new UnsupportedTypeException(c);
	}

	public char getChar() {
		return symbol;
	}

	public static boolean valid(char c) {
		for (ArithmeticType t : ArithmeticType.values()) {
			if (t.symbol == c) {
				return true;
			}
		}
		return false;
	}
}
