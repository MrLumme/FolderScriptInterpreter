package lumCode.folderScriptInterpreter.handlers.logic;

import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedLogicTypeException;
import lumCode.folderScriptInterpreter.exceptions.undefinedExceptions.UndefinedLogicException;

public class StringLogicHandler {

	@SuppressWarnings("incomplete-switch")
	protected static boolean Interpret(String left, LogicType operator, String right)
			throws UndefinedLogicException, UnsupportedLogicTypeException {
		switch (operator) {
		case EQUAL:
			return left.equals(right);
		case NOT:
			return !left.equals(right);
		}
		throw new UndefinedLogicException(left, operator, right);
	}
}
