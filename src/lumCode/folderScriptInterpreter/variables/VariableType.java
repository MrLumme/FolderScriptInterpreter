package lumCode.folderScriptInterpreter.variables;

public enum VariableType {
	FILE, FOLDER, NUMBER, BOOLEAN, TEXT, SPECIAL, ARRAY;

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
