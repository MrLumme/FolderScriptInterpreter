package lumCode.folderScriptInterpreter.variables.lookUps;

import lumCode.folderScriptInterpreter.exceptions.notFoundExceptions.EnvironmentNotFoundException;

public enum EnvironmentType {
	EXECUTION_LOCATION(0), TEMP_LOCATION(1), LOG_FILE(2), SYSTEM_TIME(3), DATE_TIME(4);

	private int id;

	private EnvironmentType(int id) {
		this.id = id;
	}

	public static boolean isValid(int id) {
		for (EnvironmentType t : values()) {
			if (t.id == id) {
				return true;
			}
		}
		return false;
	}

	public static EnvironmentType get(int id) throws EnvironmentNotFoundException {
		for (EnvironmentType t : values()) {
			if (t.id == id) {
				return t;
			}
		}
		throw new EnvironmentNotFoundException(id);
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return name().toLowerCase().replace("_", " ");
	}
}
