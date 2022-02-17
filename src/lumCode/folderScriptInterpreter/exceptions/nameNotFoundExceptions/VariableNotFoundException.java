package lumCode.folderScriptInterpreter.exceptions.nameNotFoundExceptions;

public class VariableNotFoundException extends NotFoundException {
	private static final long serialVersionUID = -5823979350169533764L;

	public VariableNotFoundException(String var) {
		super("variable", var.substring(1));
	}

}
