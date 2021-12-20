package lumCode.folderScriptInterpreter.exceptions.nameNotFoundExceptions;

public class VariableNameNotFoundException extends NameNotFoundException {
	private static final long serialVersionUID = -5823979350169533764L;

	public VariableNameNotFoundException(String var) {
		super("variable", var.substring(1));
	}

}
