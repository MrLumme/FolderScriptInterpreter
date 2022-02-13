package lumCode.folderScriptInterpreter.handlers.conditional;

import java.util.List;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.logic.Logic;
import lumCode.folderScriptInterpreter.variables.NumberVariable;

public class Conditional implements Node {
	private final Logic condition;
	private final List<Node> scriptT;
	private final List<Node> scriptF;

	public Conditional(Logic condition, List<Node> script) {
		this.condition = condition;
		this.scriptT = script;
		this.scriptF = null;
	}

	public Conditional(Logic condition, List<Node> scriptT, List<Node> scriptF) {
		this.condition = condition;
		this.scriptT = scriptT;
		this.scriptF = scriptF;
	}

	@Override
	public void action() throws InterpreterException {
		condition.action();
		if (((NumberVariable) condition.result()).asBoolean()) {
			for (Node n : scriptT) {
				n.action();
			}
		} else if (scriptF != null) {
			for (Node n : scriptF) {
				n.action();
			}
		}
	}

	public Logic getCondition() {
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
