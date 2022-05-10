package lumCode.folderScriptInterpreter.variables.lookUps;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.variables.Variable;

public class VariableLookUp extends LookUp {
	private final String name;

	public VariableLookUp(String name) {
		this.name = name;
	}

	@Override
	public void action() throws InterpreterException {
		setResult(Variable.fetch(name));
	}

	@Override
	public String toString() {
		return getName();
	}

	public String getName() {
		return name;
	}

}
