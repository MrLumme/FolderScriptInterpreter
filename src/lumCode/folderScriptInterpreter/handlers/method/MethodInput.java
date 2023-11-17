package lumCode.folderScriptInterpreter.handlers.method;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.ResultantNode;
import lumCode.folderScriptInterpreter.variables.Variable;

public class MethodInput implements ResultantNode {
	private ResultantNode value;

	public void setInput(ResultantNode value) {
		this.value = value;
	}

	@Override
	public void action() throws InterpreterException {
		value.action();
	}

	@Override
	public Variable result() {
		return value.result();
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
