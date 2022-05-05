package lumCode.folderScriptInterpreter.handlers.declaring;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.undefinedExceptions.UndefinedDeclaringException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.NumberNode;
import lumCode.folderScriptInterpreter.handlers.ResultantNode;
import lumCode.folderScriptInterpreter.variables.ArrayVariable;
import lumCode.folderScriptInterpreter.variables.BooleanVariable;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.Variable;

public class Declaration implements Node {
	private final String name;
	private final NumberNode number;
	private final ResultantNode action;
	private final DeclarationType type;

	public Declaration(String name, DeclarationType type, ResultantNode action) {
		this.name = name;
		this.number = null;
		this.action = action;
		this.type = type;
		Main.setVariable(name, new NumberVariable(0));
	}

	public Declaration(String name, NumberNode number, DeclarationType type, ResultantNode action) {
		this.name = name;
		this.number = number;
		this.action = action;
		this.type = type;
		Main.setVariable(name, new ArrayVariable());
	}

	@Override
	public void action() throws InterpreterException {
		action.action();
		Variable var = action.result();

		if (type == DeclarationType.NEGATE) {
			if (var instanceof BooleanVariable) {
				((BooleanVariable) var).flip();
			} else {
				throw new UndefinedDeclaringException(type, var);
			}
		}
		if (number == null) {
			Main.setVariable(name, var);
		} else {
			number.action();
			NumberVariable num = number.result();
			Main.setVariable(name, num, var);
		}
	}

	@Override
	public String toString() {
		String res = action.toString();
		if (number != null) {
			return name + type.getChar() + res;
		} else {
			return name + "[" + number.toString() + "]" + type.getChar() + res;
		}
	}
}
