package lumCode.folderScriptInterpreter.variables;

public class DoubleVariable extends Variable {
	double var;

	protected DoubleVariable(double var) {
		super(VariableType.DOUBLE);
		this.var = var;
	}

	protected DoubleVariable(String name, double var) {
		super(VariableType.DOUBLE, name);
		this.var = var;
	}

	public double getVar() {
		return var;
	}

	public void setVar(double var) {
		this.var = var;
	}

	public int toInt() {
		return (int) Math.floor(var);
	}
}
