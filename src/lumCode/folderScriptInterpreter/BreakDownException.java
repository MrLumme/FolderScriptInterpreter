package lumCode.folderScriptInterpreter;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;

public class BreakDownException extends InterpreterException {

	public BreakDownException(String script, int i, char c, String message) {
		super("A script interpretation error occured in the script '" + script + "' at position " + i
				+ ", the instruction '" + c + "' failed with the following message: " + message);
	}

}
