package lumCode.folderScriptInterpreter.handlers.declaringHandler;

import lumCode.folderScriptInterpreter.exceptions.UnsupportedTypeException;

public enum DeclaringType {
	EQUAL('='), NEGATE('!');

	private char symbol;

	private DeclaringType(char symbol) {
		this.symbol = symbol;
	}

	public static DeclaringType fromChar(char c) throws UnsupportedTypeException {
		for (DeclaringType t : DeclaringType.values()) {
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
		for (DeclaringType t : DeclaringType.values()) {
			if (t.symbol == c) {
				return true;
			}
		}
		return false;
	}
}
