package lumCode.folderScriptInterpreter.handlers.conditional;

import java.util.List;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.logic.Logic;
import lumCode.folderScriptInterpreter.variables.NumberVariable;

public class Conditional implements Node {
	private final Logic condition;
	private final List<Node> script;

	public Conditional(Logic condition, List<Node> script) {
		this.condition = condition;
		this.script = script;
	}

	@Override
	public void action() throws InterpreterException {
		condition.action();
		if (((NumberVariable) condition.result()).asBoolean()) {
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

	public List<Node> getScript() {
		return script;
	}

	@Override
	public String toString() {
		String s = "";
		for (Node n : script) {
			s += n.toString() + ",";
		}
		s = s.substring(0, s.length() - 1);

		return "?(" + condition.toString() + ")" + "{" + s + "}";
	}
}
