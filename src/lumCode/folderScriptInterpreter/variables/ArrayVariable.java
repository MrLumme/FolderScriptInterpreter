package lumCode.folderScriptInterpreter.variables;

import java.util.HashMap;

import lumCode.folderScriptInterpreter.exceptions.arrayExceptions.ArrayPositionEmptyException;

public class ArrayVariable extends Variable {
	private HashMap<Integer, Variable> vars;
	private int next;

	public ArrayVariable() {
		super(VariableType.ARRAY);
		vars = new HashMap<Integer, Variable>();
		next = 0;
	}

	protected ArrayVariable(HashMap<Integer, Variable> vars) {
		this();
		this.vars = vars;
	}

	@Override
	public String toString() {
		String str = "[";
		for (Integer key : vars.keySet()) {
			str += key + ":\"" + vars.get(key) + "\",";
		}
		str = str.substring(0, str.length() - 1) + "]";
		return str;
	}

	public void setVar(int numb, Variable var) {
		vars.put(numb, var);
		if (numb >= next) {
			next = numb + 1;
		}
	}

	public void setNextVar(Variable var) {
		vars.put(next, var);
		next++;
	}

	public Variable getVar(int numb) throws ArrayPositionEmptyException {
		Variable v = vars.get(numb);
		if (v != null) {
			return v;
		}
		throw new ArrayPositionEmptyException(numb);
	}

	public HashMap<Integer, Variable> getAll() {
		return vars;
	}
}
