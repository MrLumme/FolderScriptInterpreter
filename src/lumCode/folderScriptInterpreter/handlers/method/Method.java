package lumCode.folderScriptInterpreter.handlers.method;

import java.util.ArrayList;
import java.util.List;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.MethodErrorException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.ResultantNode;
import lumCode.folderScriptInterpreter.handlers.breaking.Break;
import lumCode.folderScriptInterpreter.variables.Variable;

public class Method implements ResultantNode {
	private final String name;
	private final List<Node> nodes;
	private final List<MethodInput> inputs;
	private final MethodOutput output;

	public Method(String name, List<Node> nodes, int inputCount, MethodOutput output) {
		this.name = name;
		this.nodes = nodes;
		this.inputs = new ArrayList<MethodInput>();
		for (int i = 0; i < inputCount; i++) {
			inputs.add(new MethodInput());
		}
		this.output = output;
	}

	public void setInputs(List<ResultantNode> params) throws MethodErrorException {
		if (params.size() != inputs.size()) {
			throw new MethodErrorException(name, "The amount of inputs in call does not match the required amount.");
		}
		for (int i = 0; i < params.size(); i++) {
			inputs.get(i).setInput(params.get(i));
		}
	}

	public String getName() {
		return name;
	}

	public List<MethodInput> getInputs() {
		return inputs;
	}

	public MethodOutput getOutput() {
		return output;
	}

	@Override
	public void action() throws InterpreterException {
		for (Node n : nodes) {
			if (!Break.isCalled()) {
				n.action();
			} else {
				break;
			}
		}
		output.action();
	}

	@Override
	public Variable result() {
		return output.result();
	}

	@Override
	public String toString() {
		String str = '@' + name + '(' + inputs.size() + ')' + '{';
		for (Node n : nodes) {
			str += n.toString();
		}
		str += '}';
		return str;
	}
}
