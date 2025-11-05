package lumCode.folderScriptInterpreter.exceptions.notFoundExceptions;

public class AttributeNotFoundException extends NotFoundException {
	private static final long serialVersionUID = 3076784771933595692L;

	public AttributeNotFoundException(int number) {
		super("attribute", number);
	}
}
