package lumCode.folderScriptInterpreter.variables.lookUps;

import lumCode.folderScriptInterpreter.handlers.ResultantNode;
import lumCode.folderScriptInterpreter.variables.Variable;

public abstract class LookUp implements ResultantNode {
	private Variable result;

	@Override
	public Variable result() {
		return result;
	}

	protected void setResult(Variable result) {
		this.result = result;
	}

}
