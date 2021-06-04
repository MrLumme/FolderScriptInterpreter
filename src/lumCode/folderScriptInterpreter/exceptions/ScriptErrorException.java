package lumCode.folderScriptInterpreter.exceptions;

public class ScriptErrorException extends InterpreterException {
	private static final long serialVersionUID = -4150143592800703716L;

	public ScriptErrorException(String script, String message) {
		super("The script '" + script + "' encountered the following error: " + message);
	}
}
