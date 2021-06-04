package lumCode.folderScriptInterpreter.variables;

public class SpecialVariable extends Variable {

	protected SpecialVariable() {
		super(VariableType.SPECIAL);
	}

	@Override
	public String toString() {
		return "$";
	}
}
