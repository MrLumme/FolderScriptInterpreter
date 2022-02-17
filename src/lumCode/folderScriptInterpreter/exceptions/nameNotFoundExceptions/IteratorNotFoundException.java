package lumCode.folderScriptInterpreter.exceptions.nameNotFoundExceptions;

public class IteratorNotFoundException extends NotFoundException {
	private static final long serialVersionUID = 2177236432847737156L;

	public IteratorNotFoundException(int number) {
		super("iterator", number);
	}
}
