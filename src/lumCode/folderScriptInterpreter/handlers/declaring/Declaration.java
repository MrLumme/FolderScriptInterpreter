package lumCode.folderScriptInterpreter.handlers.declaringHandler;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.arithmetic.ArithmeticNode;
import lumCode.folderScriptInterpreter.handlers.logic.LogicNode;
import lumCode.folderScriptInterpreter.variables.Variable;

public class Declaring implements Node {
	private final String name;
	private Variable value;
	private final Node action;
	private final DeclaringType type;

	public Declaring(String name, DeclaringType type, Node action) {
		this.name = name;
		this.action = action;
		this.type = type;
		value = null;
	}

	public Declaring(String name, DeclaringType type, Variable value) {
		this.name = name;
		this.value = value;
		this.type = type;
		action = null;
	}

	@Override
	public void action() throws InterpreterException {
		if (value != null) {
			if (type == DeclaringType.NEGATE) {
				// TODO
			} else {
				// TODO
			}
		} else if (action instanceof LogicNode) {
			action.action();
		} else if (action instanceof ArithmeticNode) {
			// TODO
		}
	}

	@Override
	public void explain() {

	}
}
