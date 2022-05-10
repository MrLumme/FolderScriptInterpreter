package lumCode.folderScriptInterpreter.variables.lookUps;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedVariableTypeException;
import lumCode.folderScriptInterpreter.handlers.NumberNode;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.Variable;

public class NumberVariableLookUp extends VariableLookUp implements NumberNode {

	public NumberVariableLookUp(String name) {
		super(name);
	}

	@Override
	public void action() throws InterpreterException {
		super.action();

		Variable v = super.result();
		if (!(v instanceof NumberVariable)) {
			throw new UnsupportedVariableTypeException(v.type);
		}
	}

	@Override
	public NumberVariable result() {
		return (NumberVariable) super.result();
	}
}
