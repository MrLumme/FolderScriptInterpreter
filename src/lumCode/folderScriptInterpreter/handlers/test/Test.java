package lumCode.folderScriptInterpreter.handlers.test;

import java.util.List;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.BooleanNode;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.variables.BooleanVariable;

public class Test implements BooleanNode {
	private final List<Node> script;
	private boolean result = false;

	public Test(List<Node> script) {
		this.script = script;
	}

	@Override
	public void action() {
		try {
			for (Node n : script) {
				n.action();
			}
			result = true;
		} catch (InterpreterException e) {
			result = false;
		}
	}

	@Override
	public BooleanVariable result() {
		return new BooleanVariable(result);
	}

	@Override
	public String toString() {
		String s = "";
		for (Node n : script) {
			s += n.toString() + ",";
		}
		s = s.substring(0, s.length() - 1);

		return "t{" + s + "}";
	}

}
