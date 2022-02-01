package lumCode.folderScriptInterpreter.variables.comparator;

import java.util.Comparator;

import lumCode.folderScriptInterpreter.variables.Variable;

public class VariableTypeComparator implements Comparator<Variable> {

	@Override
	public int compare(Variable o1, Variable o2) {
		return o1.type.name().compareToIgnoreCase(o2.type.name());
	}

}
