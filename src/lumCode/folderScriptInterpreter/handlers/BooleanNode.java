package lumCode.folderScriptInterpreter.handlers;

import lumCode.folderScriptInterpreter.variables.BooleanVariable;

public interface BooleanNode extends NumberNode {

	@Override
	public BooleanVariable result();
}
