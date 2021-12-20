package lumCode.folderScriptInterpreter.handlers.declaring;

import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedTypeException;

public enum DeclarationType {
	EQUAL('='), NEGATE('!');

	private char symbol;

	private DeclarationType(char symbol) {
		this.symbol = symbol;
	}

	public static DeclarationType fromChar(char c) throws UnsupportedTypeException {
		for (DeclarationType t : DeclarationType.values()) {
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
		for (DeclarationType t : DeclarationType.values()) {
			if (t.symbol == c) {
				return true;
			}
		}
		return false;
	}
}
