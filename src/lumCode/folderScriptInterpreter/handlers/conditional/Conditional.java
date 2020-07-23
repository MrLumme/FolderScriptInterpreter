package lumCode.folderScriptInterpreter.handlers.conditional;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.logic.Logic;

public class Conditional implements Node {
	private final Logic condition;
	private final Node[] script;

	public Conditional(Logic condition, Node[] script) {
		this.condition = condition;
		this.script = script;
	}

	@Override
	public void action() throws InterpreterException {
		condition.action();
		if (condition.result()) {
			for (Node n : script) {
				n.action();
			}
		}
	}

	@Override
	public void explain() {
	}

	public Logic getCondition() {
		return condition;
	}

	public Node[] getScript() {
		return script;
	}
}
