package lumCode.folderScriptInterpreter.handlers;

import lumCode.folderScriptInterpreter.variables.NumberVariable;

public interface BooleanNode extends ResultantNode {

	@Override
	public NumberVariable result();
}
