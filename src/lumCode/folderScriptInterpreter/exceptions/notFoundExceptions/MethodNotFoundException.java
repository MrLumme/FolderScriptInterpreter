package lumCode.folderScriptInterpreter.exceptions.notFoundExceptions;

public class MethodNotFoundException extends NotFoundException {
	private static final long serialVersionUID = -5823979350169533764L;

	public MethodNotFoundException(String var) {
		super("method", var.substring(1));
	}

}
