package lumCode.folderScriptInterpreter.variables.lookUps;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.arrayExceptions.InvalidArrayPositionException;
import lumCode.folderScriptInterpreter.handlers.ResultantNode;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.Variable;

public class ArrayVariableLookUp extends VariableLookUp {
	private ResultantNode number;

	public ArrayVariableLookUp(String name, ResultantNode number) {
		super(name);
		this.number = number;
	}

	@Override
	public void action() throws InterpreterException {
		number.action();
		Variable num = number.result();
		if (num instanceof NumberVariable) {
			setResult(Variable.fetch(getName() + '[' + num.result() + ']'));
		} else {
			throw new InvalidArrayPositionException(num.toString());
		}
	}

	@Override
	public String toString() {
		return getName() + '[' + number.toString() + ']';
	}

}
