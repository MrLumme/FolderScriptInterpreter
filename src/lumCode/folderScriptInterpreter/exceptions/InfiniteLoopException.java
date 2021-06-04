package lumCode.folderScriptInterpreter.exceptions;

import lumCode.folderScriptInterpreter.variables.NumberVariable;

public class InfiniteLoopException extends InterpreterException {

	public InfiniteLoopException(NumberVariable v) {
		super("A loop with a rule value of '" + v.getVar() + "' would have caused an infinite loop.");
	}

}
