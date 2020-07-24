package lumCode.folderScriptInterpreter.handlers.DeclaringHandler;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.arithmetic.ArithmeticNode;
import lumCode.folderScriptInterpreter.handlers.logic.LogicNode;
import lumCode.folderScriptInterpreter.variables.Variable;

public class Declaring implements Node {
	private final Variable var;
	private final Variable other;
	private final Node action;
	private final DeclaringType type;

	public Declaring(Variable var, DeclaringType type, Node action) {
		this.var = var;
		this.action = action;
		this.type = type;
		other = null;
	}

	public Declaring(Variable var, DeclaringType type, Variable other) {
		this.var = var;
		this.other = other;
		this.type = type;
		action = null;
	}

	@Override
	public void action() throws InterpreterException {
		if (other != null) {
			if (type == DeclaringType.NEGATE) {
				// TODO
			} else {
				// TODO
			}
		} else if (action instanceof LogicNode) {
			// TODO
		} else if (action instanceof ArithmeticNode) {
			// TODO
		}
	}

	@Override
	public void explain() {

	}
}
