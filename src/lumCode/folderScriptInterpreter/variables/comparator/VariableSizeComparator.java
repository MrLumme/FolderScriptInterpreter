package lumCode.folderScriptInterpreter.variables.comparator;

import java.io.FileNotFoundException;
import java.util.Comparator;

import lumCode.folderScriptInterpreter.Utilities;
import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedVariableTypeException;
import lumCode.folderScriptInterpreter.variables.Variable;

public class VariableSizeComparator<T extends Variable> implements Comparator<T> {

	@Override
	public int compare(T o1, T o2) {
		try {
			return (int) (Utilities.varSize(o1) - Utilities.varSize(o2));
		} catch (UnsupportedVariableTypeException | FileNotFoundException e) {
			return 0;
		}
	}

}
