package lumCode.folderScriptInterpreter.exceptions;

public class IteratorNameNotFoundException extends NameNotFoundException {
	private static final long serialVersionUID = 2177236432847737156L;

	public IteratorNameNotFoundException(int number) {
		super("iterator", number);
	}
}
