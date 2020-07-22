package lumCode.folderScriptInterpreter.handlers.logic;

import lumCode.folderScriptInterpreter.exceptions.UnsupportedLogicTypeException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedTypeException;

public enum LogicType {
	EQUAL, LESS, GREATER, NOT;

	public static LogicType fromChar(char c) throws UnsupportedTypeException {
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
		throw new UnsupportedTypeException(c);
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
		throw new UnsupportedLogicTypeException(c);
	}

	public static boolean valid(char c) {
		switch (c) {
		case '=':
		case '>':
		case '<':
		case '!':
			return true;
		default:
			return false;
		}
	}
}
