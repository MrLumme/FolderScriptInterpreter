package lumCode.folderScriptInterpreter.exceptions.nameNotFoundExceptions;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;

public class NameNotFoundException extends InterpreterException {
	private static final long serialVersionUID = 965004424139859568L;

	public NameNotFoundException(String type, String name) {
		super("Could not found a " + type + " named '" + name + "'.");
	}

	public NameNotFoundException(String type, int number) {
		super("Could not found a " + type + " with the number '" + number + "'.");
	}

}
