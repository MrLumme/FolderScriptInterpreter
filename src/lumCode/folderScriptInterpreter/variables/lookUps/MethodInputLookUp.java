package lumCode.folderScriptInterpreter.variables.lookUps;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.NumberNode;
import lumCode.folderScriptInterpreter.handlers.method.MethodInput;

public class MethodInputLookUp extends LookUp {
	private final String name;
	private final NumberNode input;

	public MethodInputLookUp(String name, NumberNode input) {
		this.name = name;
		this.input = input;
	}

	@Override
	public void action() throws InterpreterException {
		input.action();
		MethodInput mi = Main.lookUpMethod(name).getInputs().get((int) input.result().getVar());
		mi.action();
		setResult(mi.result());
	}

	@Override
	public String toString() {
		return '@' + getName() + '[' + input.toString() + ']';
	}

	public String getName() {
		return name;
	}

}
