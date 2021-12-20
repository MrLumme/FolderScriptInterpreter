package lumCode.folderScriptInterpreter.exceptions.undefinedExceptions;

import lumCode.folderScriptInterpreter.handlers.declaring.DeclarationType;
import lumCode.folderScriptInterpreter.variables.Variable;

public class UndefinedDeclaringException extends UndefinedException {
	private static final long serialVersionUID = -675794863639635267L;

	public UndefinedDeclaringException(DeclarationType type, Variable var) {
		super("declaration", type.getChar() + " " + var.toString());
	}
}
