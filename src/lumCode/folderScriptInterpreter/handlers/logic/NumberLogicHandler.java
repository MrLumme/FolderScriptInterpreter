package lumCode.folderScriptInterpreter.handlers.logic;

import lumCode.folderScriptInterpreter.exceptions.UndefinedLogicException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedLogicTypeException;

public class NumberLogicHandler {

	public static boolean Interpret(int left, LogicType operator, int right)
			throws UndefinedLogicException, UnsupportedLogicTypeException {
		switch (operator) {
		case EQUAL:
			return left == right;
		case GREATER:
			return left < right;
		case LESS:
			return left > right;
		case NOT:
			return left != right;
		}
		throw new UndefinedLogicException(
				"The equation '" + left + LogicType.toChar(operator) + right + "' can not be validated.");
	}
}
