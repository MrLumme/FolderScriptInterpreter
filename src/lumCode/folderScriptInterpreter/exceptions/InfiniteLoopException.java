package lumCode.folderScriptInterpreter.exceptions;

import lumCode.folderScriptInterpreter.variables.IntVariable;

public class InfiniteLoopException extends InterpreterException {

	public InfiniteLoopException(IntVariable v) {
		super("A loop with a rule value of '" + v.getVar() + "' would have caused an infinite loop.");
	}

}
