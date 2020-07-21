package lumCode.folderScriptInterpreter.handlers;

import lumCode.folderScriptInterpreter.variables.Variable;

public interface CommandNode extends Node {

	public Variable[] result();
}
