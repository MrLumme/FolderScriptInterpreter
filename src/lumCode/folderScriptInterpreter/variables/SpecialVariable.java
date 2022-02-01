package lumCode.folderScriptInterpreter.variables;

public class SpecialVariable extends Variable {
	public static SpecialVariable instance = null;

	private SpecialVariable() {
		super(VariableType.SPECIAL);
	}

	public static SpecialVariable getInstance() {
		if (instance == null) {
			instance = new SpecialVariable();
		}
		return instance;
	}

	@Override
	public String toString() {
		return "$";
	}

	@Override
	public SpecialVariable copy() {
		return getInstance();
	}
}
