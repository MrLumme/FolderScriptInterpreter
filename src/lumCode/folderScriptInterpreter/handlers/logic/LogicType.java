package lumCode.folderScriptInterpreter.handlers.logic;

import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedTypeException;

public enum LogicType {
	EQUAL('='), LESS('<'), GREATER('>'), NOT('!');

	private char symbol;

	private LogicType(char symbol) {
		this.symbol = symbol;
	}

	public static LogicType fromChar(char c) throws UnsupportedTypeException {
		for (LogicType t : LogicType.values()) {
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
		for (LogicType t : LogicType.values()) {
			if (t.symbol == c) {
				return true;
			}
		}
		return false;
	}
}
