package lumCode.folderScriptInterpreter.exceptions;

import lumCode.folderScriptInterpreter.handlers.command.CommandType;

public class IncorrentParameterAmountException extends InterpreterException {
	private static final long serialVersionUID = 1909083068084599020L;

	public IncorrentParameterAmountException(CommandType c, int amount) throws UnsupportedCommandTypeException {
		super("The command '" + c.getChar() + "' expects " + c.getInput() + " input parameters, but has gotten "
				+ amount + ".");
	}
}
