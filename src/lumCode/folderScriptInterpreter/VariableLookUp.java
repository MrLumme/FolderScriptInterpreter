package lumCode.folderScriptInterpreter;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.ResultantNode;
import lumCode.folderScriptInterpreter.variables.Variable;

public class VariableLookUp implements ResultantNode {
	private final String name;
	private Variable res;

	protected VariableLookUp(String name) {
		this.name = name;
	}

	@Override
	public void action() throws InterpreterException {
		res = Variable.fetch(name);
	}

	@Override
	public void explain() {
		// TODO Auto-generated method stub
	}

	@Override
	public Variable result() {
		return res;
	}

}
