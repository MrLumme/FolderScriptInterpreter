package lumCode.folderScriptInterpreter.variables;

public class DoubleVariable extends Variable {
	double var;

	public DoubleVariable(double var) {
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
