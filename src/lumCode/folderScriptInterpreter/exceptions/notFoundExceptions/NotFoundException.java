package lumCode.folderScriptInterpreter.exceptions.notFoundExceptions;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;

public class NotFoundException extends InterpreterException {
	private static final long serialVersionUID = 965004424139859568L;

	public NotFoundException(String type, String name) {
		super("Could not find a " + type + " named '" + name + "'.");
	}

	public NotFoundException(String type, int number) {
		super("Could not find an " + type + " with the number '" + number + "'.");
	}

}
