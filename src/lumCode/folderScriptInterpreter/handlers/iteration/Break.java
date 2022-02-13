package lumCode.folderScriptInterpreter.handlers.iteration;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.Node;

public class Break implements Node {

	@Override
	public void action() throws InterpreterException {
		Iteration.callBreak();
	}

	@Override
	public String toString() {
		return "b";
	}

}
