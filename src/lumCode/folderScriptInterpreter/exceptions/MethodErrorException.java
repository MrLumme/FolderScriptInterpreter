package lumCode.folderScriptInterpreter.exceptions;

public class MethodErrorException extends InterpreterException {
	private static final long serialVersionUID = 2175366314405767927L;

	public MethodErrorException(String name, String message) {
		super("Method named '" + name + "' failed with the following message: " + message);
	}
}
