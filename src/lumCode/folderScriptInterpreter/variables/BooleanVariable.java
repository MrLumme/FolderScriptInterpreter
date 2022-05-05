package lumCode.folderScriptInterpreter.variables;

import lumCode.folderScriptInterpreter.handlers.BooleanNode;

public class BooleanVariable extends NumberVariable implements BooleanNode {

	public BooleanVariable(boolean var) {
		super(var ? 1 : 0);
	}

	public boolean asBoolean() {
		if (getVar() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public void flip() {
		setVar(!asBoolean());
	}

	@Override
	public void setVar(int var) {
		super.setVar(var != 0 ? 1 : 0);
	}

	public void setVar(boolean var) {
		super.setVar(var ? 1 : 0);
	}

	@Override
	public BooleanVariable copy() {
		return new BooleanVariable(asBoolean());
	}

	@Override
	public BooleanVariable result() {
		return this;
	}

}
