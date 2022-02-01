package lumCode.folderScriptInterpreter.variables.comparator;

import java.util.Comparator;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.variables.Variable;

public class VariableValueComparator<T extends Variable> implements Comparator<T> {

	@Override
	public int compare(T o1, T o2) {
		if (Main.caseSensitive) {
			return o1.toString().compareTo(o2.toString());
		} else {
			return o1.toString().compareToIgnoreCase(o2.toString());
		}
	}

}
