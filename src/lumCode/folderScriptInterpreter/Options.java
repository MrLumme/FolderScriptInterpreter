package lumCode.folderScriptInterpreter;

import java.util.HashMap;
import java.util.Map;

public enum Options {
	DEBUG(0, false), OVERWRITE(1, false), RETURN_FOLDERS(2, false), STRICT_ARRAY_DATA(3, false);

	private int id;
	private boolean def;

	private Options(int id, boolean def) {
		this.id = id;
		this.def = def;
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

	public static Options getOption(int id) {
		for (Options o : values()) {
			if (o.id == id) {
				return o;
			}
		}
		return null;
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
