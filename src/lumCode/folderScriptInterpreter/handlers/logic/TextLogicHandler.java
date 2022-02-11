package lumCode.folderScriptInterpreter.handlers.logic;

import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedLogicTypeException;
import lumCode.folderScriptInterpreter.exceptions.undefinedExceptions.UndefinedLogicException;

public class TextLogicHandler {

	@SuppressWarnings("incomplete-switch")
	protected static boolean Interpret(String left, LogicType operator, String right)
			throws UndefinedLogicException, UnsupportedLogicTypeException {
		switch (operator) {
		case EQUAL:
			return left.equals(right);
		case NOT:
			return !left.equals(right);
		case LESS:
			return left.length() < right.length();
		case GREATER:
			return left.length() > right.length();
		}
		throw new UndefinedLogicException(left, operator, right);
	}
}
