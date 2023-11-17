package lumCode.folderScriptInterpreter.variables.lookUps;

import java.util.List;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.ResultantNode;

public class MethodLookUp extends LookUp {
	private final String name;
	private final List<ResultantNode> inputs;

	public MethodLookUp(String name, List<ResultantNode> inputs) {
		this.name = name;
		this.inputs = inputs;
	}

	@Override
	public void action() throws InterpreterException {
		Main.lookUpMethod(name).setInputs(inputs);
		Main.lookUpMethod(name).action();
		setResult(Main.lookUpMethod(name).result());
	}

	@Override
	public String toString() {
		String str = '@' + getName() + '(';
		if (inputs.size() > 0) {
			for (ResultantNode in : inputs) {
				str += in.toString() + ',';
			}
			str = str.substring(0, str.length() - 1);
		}
		return str + ')';
	}

	public String getName() {
		return name;
	}

}
