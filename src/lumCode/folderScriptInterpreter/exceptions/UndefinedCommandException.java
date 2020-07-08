package lumCode.folderScriptInterpreter.exceptions;

import lumCode.folderScriptInterpreter.handlers.CommandType;

public class UndefinedCommandException extends UndefinedException {
	private static final long serialVersionUID = 1663164287149103667L;

	public UndefinedCommandException(CommandType c, String params) {
		super("command", c.toString() + "(" + params + ")");
	}
}
