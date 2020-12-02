package lumCode.folderScriptInterpreter.variables;

import java.util.HashMap;

public class ArrayVariable extends Variable {
	private HashMap<Integer, Variable> vars;

	protected ArrayVariable(String name) {
		super(VariableType.ARRAY, name);
	}

	public ArrayVariable() {
		super(VariableType.ARRAY);
	}

	protected ArrayVariable(String name, HashMap<Integer, Variable> vars) {
		this(name);
		this.vars = vars;
	}

	protected ArrayVariable(HashMap<Integer, Variable> vars) {
		this();
		this.vars = vars;
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
