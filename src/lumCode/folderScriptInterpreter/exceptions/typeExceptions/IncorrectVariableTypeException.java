package lumCode.folderScriptInterpreter.exceptions.typeExceptions;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.command.CommandType;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.lookUps.AttributeType;

public class IncorrectVariableTypeException extends InterpreterException {
	private static final long serialVersionUID = 4295363097012846150L;

	public IncorrectVariableTypeException(AttributeType c, Variable v) {
		super("The attribute type '"+ c.name() +"' is not supported for variable '" + v.toString() + "' which resolves to type '" + v.type.toString().toLowerCase()
				+ "'.");
	}
}
