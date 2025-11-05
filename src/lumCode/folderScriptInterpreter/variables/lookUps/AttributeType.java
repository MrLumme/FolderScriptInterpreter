package lumCode.folderScriptInterpreter.variables.lookUps;

import lumCode.folderScriptInterpreter.exceptions.notFoundExceptions.AttributeNotFoundException;

import java.util.EnumSet;

public enum AttributeType {
	IS_HIDDEN(0), IS_LINK(1), LAST_MODIFIED_TIME(2), LAST_ACCESS_TIME(3), CREATION_TIME(4);

	private int id;

	AttributeType(int id) {
		this.id = id;
	}

	public static boolean isValid(int id) {
		for (AttributeType t : values()) {
			if (t.id == id) {
				return true;
			}
		}
		return false;
	}

	public static AttributeType get(int id) throws AttributeNotFoundException {
		for (AttributeType t : values()) {
			if (t.id == id) {
				return t;
			}
		}
		throw new AttributeNotFoundException(id);
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return name().toLowerCase().replace("_", " ");
	}
}
