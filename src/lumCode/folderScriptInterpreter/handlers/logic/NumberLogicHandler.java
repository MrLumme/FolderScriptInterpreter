package lumCode.folderScriptInterpreter.handlers.logic;

import lumCode.folderScriptInterpreter.exceptions.UndefinedLogicException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedLogicTypeException;

public class NumberLogicHandler {

	protected static boolean Interpret(long left, LogicType operator, long right)
			throws UndefinedLogicException, UnsupportedLogicTypeException {
		switch (operator) {
		case EQUAL:
			return left == right;
		case GREATER:
			return left > right;
		case LESS:
			return left < right;
		case NOT:
			return left != right;
		}
		throw new UndefinedLogicException("" + left, operator, "" + right);
	}
}
