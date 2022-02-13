package lumCode.folderScriptInterpreter.handlers;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;

public interface Node {

	public void action() throws InterpreterException;
}
