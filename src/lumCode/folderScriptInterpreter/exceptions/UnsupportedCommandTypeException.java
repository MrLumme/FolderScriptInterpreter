package lumCode.folderScriptInterpreter.exceptions;

import lumCode.folderScriptInterpreter.handlers.command.CommandType;

public class UnsupportedCommandTypeException extends UnsupportedTypeException {
	private static final long serialVersionUID = 865346406363054068L;

	public UnsupportedCommandTypeException(CommandType c) {
		super("command", c.toString());
	}
}
