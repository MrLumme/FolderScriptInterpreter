package lumCode.folderScriptInterpreter.exceptions;

public class ArgumentNameNotFoundException extends NameNotFoundException {
	private static final long serialVersionUID = 2177236432847737156L;

	public ArgumentNameNotFoundException(int number) {
		super("argument", number);
	}
}
