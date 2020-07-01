package lumCode.folderScriptInterpreter.variables;

public class SpecialVariable extends Variable {

	protected SpecialVariable() {
		super(VariableType.SPECIAL);
	}

	protected SpecialVariable(String name) {
		super(VariableType.SPECIAL, name);
	}
}
