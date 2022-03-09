package lumCode.folderScriptInterpreter.exceptions.notFoundExceptions;

public class EnvironmentNotFoundException extends NotFoundException {
	private static final long serialVersionUID = 3076784771931595692L;

	public EnvironmentNotFoundException(int number) {
		super("environment", number);
	}
}
