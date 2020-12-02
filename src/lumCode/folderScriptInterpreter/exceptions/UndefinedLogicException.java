package lumCode.folderScriptInterpreter.exceptions;

import lumCode.folderScriptInterpreter.handlers.logic.LogicType;

public class UndefinedLogicException extends UndefinedException {
	private static final long serialVersionUID = 8317477507578124451L;

	public UndefinedLogicException(String left, LogicType l, String right) throws UnsupportedLogicTypeException {
		super("equation", left + l.getChar() + right);
	}

}
