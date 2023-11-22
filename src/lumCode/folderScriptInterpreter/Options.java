package lumCode.folderScriptInterpreter;

import java.util.HashMap;
import java.util.Map;

import lumCode.folderScriptInterpreter.exceptions.notFoundExceptions.OptionNotFoundException;

public enum Options {
	DEBUG(0), OVERWRITE(1), RETURN_FOLDERS(2), STRICT_ARRAY_DATA(3), WRITE_TO_LOG(4), OUTPUT_EXTERNAL_LOG(5);

	private int id;
	private boolean def;

	private Options(int id) {
		this.id = id;
		this.def = false;
	}

	public static Map<Integer, Boolean> constructMap() {
		HashMap<Integer, Boolean> map = new HashMap<>(values().length);
		for (Options o : values()) {
			map.put(o.id, o.def);
		}
		return map;
	}

	public static boolean isValid(int id) {
		for (Options o : values()) {
			if (o.id == id) {
				return true;
			}
		}
		return false;
	}

	public static Options get(int id) throws OptionNotFoundException {
		for (Options o : values()) {
			if (o.id == id) {
				return o;
			}
		}
		throw new OptionNotFoundException(id);
	}

	public int getId() {
		return id;
	}

	public boolean getDefaultValue() {
		return def;
	}

	@Override
	public String toString() {
		return name().toLowerCase().replace("_", " ");
	}
}
