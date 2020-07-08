package lumCode.folderScriptInterpreter.exceptions;

import lumCode.folderScriptInterpreter.handlers.logic.LogicType;

public class UnsupportedLogicTypeException extends UnsupportedTypeException {
	private static final long serialVersionUID = -3561187848358098229L;

	public UnsupportedLogicTypeException(LogicType l) {
		super("logic", l.toString());
	}

}
