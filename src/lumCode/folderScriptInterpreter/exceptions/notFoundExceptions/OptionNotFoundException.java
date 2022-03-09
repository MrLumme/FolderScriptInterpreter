package lumCode.folderScriptInterpreter.exceptions.notFoundExceptions;

public class OptionNotFoundException extends NotFoundException {
	private static final long serialVersionUID = -2085817719088771691L;

	public OptionNotFoundException(int number) {
		super("option", number);
	}
}
