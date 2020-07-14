package lumCode.folderScriptInterpreter.exceptions;

import lumCode.folderScriptInterpreter.variables.Variable;

public class IteratorTypeException extends InterpreterException {
	private static final long serialVersionUID = 6329403366744003250L;

	public IteratorTypeException(Variable v) {
		super("The variable '" + v.toString() + "' resolves to the type '" + v.type.toString().toLowerCase()
				+ "' which is not supported iteration.");
	}
}
