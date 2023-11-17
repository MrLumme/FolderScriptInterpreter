package lumCode.folderScriptInterpreter.variables.lookUps;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.ResultantNode;
import lumCode.folderScriptInterpreter.handlers.method.MethodOutput;

public class MethodOutputLookUp extends LookUp {
	private final String name;
	private final ResultantNode output;

	public MethodOutputLookUp(String name, ResultantNode output) {
		this.name = name;
		this.output = output;
	}

	@Override
	public void action() throws InterpreterException {
		output.action();
		MethodOutput mi = Main.lookUpMethod(name).getOutput();
		mi.setOutput(output);
		mi.action();
		setResult(mi.result());
	}

	@Override
	public String toString() {
		return '@' + getName() + '=' + output.toString();
	}

	public String getName() {
		return name;
	}

}
