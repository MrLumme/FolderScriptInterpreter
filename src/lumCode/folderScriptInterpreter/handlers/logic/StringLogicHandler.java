package lumCode.folderScriptInterpreter.handlers.logic;

import lumCode.folderScriptInterpreter.exceptions.UndefinedLogicException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedLogicTypeException;

public class StringLogicHandler {

	protected static boolean Interpret(String left, LogicType operator, String right)
			throws UndefinedLogicException, UnsupportedLogicTypeException {
		switch (operator) {
		case EQUAL:
			return left.equals(right);
		case NOT:
			return !left.equals(right);
		}
		throw new UndefinedLogicException(
				"The equation '" + left + LogicType.toChar(operator) + right + "' can not be validated.");
	}
}
