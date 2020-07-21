package lumCode.folderScriptInterpreter.exceptions;

import lumCode.folderScriptInterpreter.handlers.command.CommandType;
import lumCode.folderScriptInterpreter.variables.Variable;

public class UndefinedCommandException extends UndefinedException {
	private static final long serialVersionUID = 1663164287149103667L;

	public UndefinedCommandException(CommandType c, Variable[] vars) {
		super("command", c.toString() + "(" + vars + ")");
	}
}
