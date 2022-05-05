package lumCode.folderScriptInterpreter.handlers;

import lumCode.folderScriptInterpreter.variables.NumberVariable;

public interface NumberNode extends ResultantNode {

	@Override
	public NumberVariable result();
}
