package lumCode.folderScriptInterpreter.handlers.logic;

import lumCode.folderScriptInterpreter.exceptions.UnsupportedLogicTypeException;

public enum LogicType {
	EQUAL, LESS, GREATER, NOT;

	public static LogicType fromChar(char c) throws UnsupportedLogicTypeException {
		switch (c) {
		case '=':
			return EQUAL;
		case '>':
			return GREATER;
		case '<':
			return LESS;
		case '!':
			return NOT;
		}
		throw new UnsupportedLogicTypeException("The operator '" + c + "' is not supported.");
	}

	public static char toChar(LogicType c) throws UnsupportedLogicTypeException {
		switch (c) {
		case EQUAL:
			return '=';
		case GREATER:
			return '>';
		case LESS:
			return '<';
		case NOT:
			return '!';
		}
		throw new UnsupportedLogicTypeException("The operator '" + c.toString() + "' is not supported.");
	}
}
