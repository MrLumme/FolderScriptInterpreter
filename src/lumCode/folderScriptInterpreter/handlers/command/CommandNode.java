package lumCode.folderScriptInterpreter.handlers.command;

import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.variables.Variable;

public interface CommandNode extends Node {

	public Variable[] result();
}
