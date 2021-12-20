package lumCode.folderScriptInterpreter.handlers.arithmetic;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.UndefinedArithmeticException;
import lumCode.folderScriptInterpreter.handlers.ResultantNode;
import lumCode.folderScriptInterpreter.variables.ArrayVariable;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.SpecialVariable;
import lumCode.folderScriptInterpreter.variables.TextVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.VariableType;

public class Arithmetic implements ResultantNode {
	private final ResultantNode left, right;
	private final ArithmeticType type;
	private Variable result;

	public Arithmetic(ResultantNode left, ArithmeticType type, ResultantNode right) {
		this.left = left;
		this.right = right;
		this.type = type;
	}

	@Override
	public void action() throws InterpreterException {
		left.action();
		right.action();
		result = null;

		if (left.result().type == VariableType.NUMBER) {
			result = NumberArithmeticHandler.calculate((NumberVariable) left.result(), type, right.result());
		} else if (left.result().type == VariableType.TEXT) {
			result = TextArithmeticHandler.calculate((TextVariable) left.result(), type, right.result());
		} else if (left.result().type == VariableType.FILE) {
			result = FileArithmeticHandler.calculate((FileVariable) left.result(), type, right.result());
		} else if (left.result().type == VariableType.FOLDER) {
			result = FolderArithmeticHandler.calculate((FolderVariable) left.result(), type, right.result());
		} else if (left.result().type == VariableType.ARRAY) {
			result = ArrayArithmeticHandler.calculate((ArrayVariable) left.result(), type, right.result());
		} else if (left.result().type == VariableType.SPECIAL) {
			result = SpecialArithmeticHandler.calculate((SpecialVariable) left.result(), type, right.result());
		}
		if (result == null) {
			throw new UndefinedArithmeticException(left.toString(), type, right.toString());
		}
	}

	@Override
	public void explain() {
		// TODO Auto-generated method stub

	}

	@Override
	public Variable result() {
		return result;
	}

	@Override
	public String toString() {
		return left.toString() + type.getChar() + right.toString();
	}

}
