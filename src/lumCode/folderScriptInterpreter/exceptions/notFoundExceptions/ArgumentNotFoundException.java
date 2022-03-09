package lumCode.folderScriptInterpreter.exceptions.notFoundExceptions;

public class ArgumentNotFoundException extends NotFoundException {
	private static final long serialVersionUID = 2177236432847737156L;

	public ArgumentNotFoundException(int number) {
		super("argument", number);
	}
}
