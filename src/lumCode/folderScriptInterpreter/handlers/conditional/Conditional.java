package lumCode.folderScriptInterpreter.handlers.conditional;

import java.util.List;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.BooleanNode;
import lumCode.folderScriptInterpreter.handlers.Node;

public class Conditional implements Node {
	private final BooleanNode condition;
	private final List<Node> scriptT;
	private final List<Node> scriptF;

	public Conditional(BooleanNode condition, List<Node> script) {
		this.condition = condition;
		this.scriptT = script;
		this.scriptF = null;
	}

	public Conditional(BooleanNode condition, List<Node> scriptT, List<Node> scriptF) {
		this.condition = condition;
		this.scriptT = scriptT;
		this.scriptF = scriptF;
	}

	@Override
	public void action() throws InterpreterException {
		condition.action();
		if (condition.result().asBoolean()) {
			for (Node n : scriptT) {
				n.action();
			}
		} else if (scriptF != null) {
			for (Node n : scriptF) {
				n.action();
			}
		}
	}

	public BooleanNode getCondition() {
		return condition;
	}

	public List<Node> getScript() {
		return scriptT;
	}

	public List<Node> getScriptTrue() {
		return getScript();
	}

	public List<Node> getScriptFalse() {
		return scriptF;
	}

	@Override
	public String toString() {
		String s = "";
		for (Node n : scriptT) {
			s += n.toString() + ",";
		}
		s = s.substring(0, s.length() - 1);

		if (scriptF != null) {
			s += ':';
			for (Node n : scriptT) {
				s += n.toString() + ",";
			}
			s = s.substring(0, s.length() - 1);
		}

		return "?(" + condition.toString() + ")" + "{" + s + "}";
	}
}
