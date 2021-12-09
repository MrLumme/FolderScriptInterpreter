package lumCode.folderScriptInterpreter.variables;

public class SpecialVariable extends Variable {

	public SpecialVariable() {
		super(VariableType.SPECIAL);
	}

	@Override
	public String toString() {
		return "$";
	}
}
