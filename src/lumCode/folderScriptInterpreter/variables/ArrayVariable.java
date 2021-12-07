package lumCode.folderScriptInterpreter.variables;

import java.util.HashMap;

public class ArrayVariable extends Variable {
	private HashMap<Integer, Variable> vars;

	public ArrayVariable() {
		super(VariableType.ARRAY);
		vars = new HashMap<Integer, Variable>();
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
	}

	public Variable getVar(int numb) {
		return vars.get(numb);
	}

	public HashMap<Integer, Variable> getAll() {
		return vars;
	}
}
