package lumCode.folderScriptInterpreter.handlers.logic;

import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedLogicTypeException;
import lumCode.folderScriptInterpreter.exceptions.undefinedExceptions.UndefinedLogicException;

public class BooleanLogicHandler {

	protected static boolean Interpret(boolean left, LogicType operator, boolean right)
			throws UndefinedLogicException, UnsupportedLogicTypeException {
		switch (operator) {
		case EQUAL:
			return left == right;
		case NOT:
			return left != right;
		case GREATER:
			return left == true && right == false;
		case LESS:
			return left == false && right == true;
		case AND:
			return left && right;
		case OR:
			return left || right;
		}
		throw new UndefinedLogicException("" + left, operator, "" + right);
	}
}
