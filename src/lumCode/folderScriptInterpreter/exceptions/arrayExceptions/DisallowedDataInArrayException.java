package lumCode.folderScriptInterpreter.exceptions.arrayExceptions;

import lumCode.folderScriptInterpreter.Options;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.variables.VariableType;

public class DisallowedDataInArrayException extends InterpreterException {

	private static final long serialVersionUID = 8044371888541162794L;

	public DisallowedDataInArrayException(VariableType allowed, VariableType attempted) {
		super("Data of type '" + attempted.toString() + "' is not permitted in arrays with data of type '"
				+ allowed.toString() + "' when the strict array data option (id: " + Options.STRICT_ARRAY_DATA.getId()
				+ ") is true ('1').");
	}

}
