package lumCode.folderScriptInterpreter.handlers.declaring;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.UndefinedDeclaringException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.ResultantNode;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.Variable;

public class Declaration implements Node {
	private final String name;
	private Variable value;
	private final Node action;
	private final DeclarationType type;

	public Declaration(String name, DeclarationType type, Node action) {
		this.name = name;
		this.action = action;
		this.type = type;
		value = null;
		Main.setVariable(name, "0");
	}

	public Declaration(String name, DeclarationType type, Variable value) {
		this.name = name;
		this.value = value;
		this.type = type;
		action = null;
		Main.setVariable(name, "0");
	}

	@Override
	public void action() throws InterpreterException {
		Variable var = value;
		if (action instanceof ResultantNode) {
			action.action();
			var = Variable.fromString("" + ((ResultantNode) action).result());
		}

		if (type == DeclarationType.NEGATE) {
			if (var.toString().equals("1")) {
				var = new NumberVariable(0);
			} else if (var.toString().equals("0")) {
				var = new NumberVariable(1);
			} else {
				throw new UndefinedDeclaringException(type, value);
			}
		}
		Main.setVariable(name, var.toString());
	}

	@Override
	public void explain() {

	}

	@Override
	public String toString() {
		String res = value.toString();
		if (action != null) {
			res = action.toString();
		}
		return name + type.getChar() + res;
	}
}
