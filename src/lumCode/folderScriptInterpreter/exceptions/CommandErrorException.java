package lumCode.folderScriptInterpreter.exceptions;

public class CommandErrorException extends InterpreterException {
	private static final long serialVersionUID = -5730219498059348406L;

	public CommandErrorException(String message) {
		super("A command has failed with the message: \"" + message + "\".");
	}

}
