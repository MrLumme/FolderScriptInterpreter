package lumCode.folderScriptInterpreter.exceptions.typeExceptions;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.command.CommandType;
import lumCode.folderScriptInterpreter.variables.Variable;

public class IncorrectParameterTypeException extends InterpreterException {
	private static final long serialVersionUID = 4295363097012846149L;

	public IncorrectParameterTypeException(CommandType c, Variable v) {
		super("The variable '" + v.toString() + "' resolves to the type '" + v.type.toString().toLowerCase()
				+ "' which is not supported for the command '" + c.getChar() + "'.");
	}
}
