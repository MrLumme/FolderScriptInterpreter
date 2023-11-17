package lumCode.folderScriptInterpreter.handlers.method;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.ResultantNode;
import lumCode.folderScriptInterpreter.variables.Variable;

public class MethodOutput implements ResultantNode {
	private ResultantNode value;

	public void setOutput(ResultantNode value) {
		this.value = value;
	}

	@Override
	public void action() throws InterpreterException {
		// Do nothing
	}

	@Override
	public Variable result() {
		if (value == null) {
			return null;
		} else {
			return value.result();
		}
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
