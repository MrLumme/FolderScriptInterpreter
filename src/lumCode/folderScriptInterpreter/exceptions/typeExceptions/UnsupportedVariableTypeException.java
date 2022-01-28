package lumCode.folderScriptInterpreter.exceptions.typeExceptions;

import lumCode.folderScriptInterpreter.variables.VariableType;

public class UnsupportedVariableTypeException extends UnsupportedTypeException {
	private static final long serialVersionUID = -3978484005101962941L;

	public UnsupportedVariableTypeException(VariableType v) {
		super("variable", v.toString());
	}

}
