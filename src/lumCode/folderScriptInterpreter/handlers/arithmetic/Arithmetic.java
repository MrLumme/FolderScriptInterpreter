package lumCode.folderScriptInterpreter.handlers.arithmetic;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.UndefinedArithmeticException;
import lumCode.folderScriptInterpreter.handlers.ResultantNode;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
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
			if (right.result().type == VariableType.NUMBER) {
				result = new NumberVariable(NumberArithmeticHandler.interpret(((NumberVariable) left.result()).getVar(),
						type, ((NumberVariable) right.result()).getVar()));
			} else if (right.result().type == VariableType.TEXT) {
				result = new TextVariable(
						TextArithmeticHandler.interpret("" + ((NumberVariable) left.result()).getVar(), type,
								((TextVariable) right.result()).getVar()));
			} else if (right.result().type == VariableType.FILE) {
//			return new FileVariable(
//					fileArithmetic(((IntVariable) left).getVar(), operator, ((FileVariable) right).getVar()));
			} else if (right.result().type == VariableType.FOLDER) {
//				result = new FolderVariable(FileArithmeticHandler.interpret(((NumberVariable) left.result()).getVar(),
//						type, ((FolderVariable) right).getVar()));
			}
		} else if (left.result().type == VariableType.TEXT) {
			if (right.result().type == VariableType.NUMBER) {
				result = new TextVariable(TextArithmeticHandler.interpret(((TextVariable) left.result()).getVar(), type,
						"" + ((NumberVariable) right.result()).getVar()));
			} else if (right.result().type == VariableType.TEXT) {
				result = new TextVariable(TextArithmeticHandler.interpret(((TextVariable) left.result()).getVar(), type,
						((TextVariable) right.result()).getVar()));
			} else if (right.result().type == VariableType.FILE) {
//			return new StringVariable(
//					fileArithmetic(((StringVariable) left).getVar(), operator, ((FileVariable) right).getVar()));
			} else if (right.result().type == VariableType.FOLDER) {
//			return new IntVariable(folderArithmetic(((StringVariable) left).getVar(), operator,
//					((FolderVariable) right).getVar()));
			}
		} else if (left.result().type == VariableType.FILE) {
			if (right.result().type == VariableType.NUMBER) {
				result = new FileVariable(FileArithmeticHandler.interpret(((FileVariable) left).getVar(), type,
						"" + ((NumberVariable) right).getVar()));
			} else if (right.result().type == VariableType.TEXT) {
				result = new FileVariable(FileArithmeticHandler.interpret(((FileVariable) left).getVar(), type,
						((TextVariable) right).getVar()));
			} else if (right.result().type == VariableType.FILE) {
				result = new FileVariable(FileArithmeticHandler.interpret(((FileVariable) left).getVar(), type,
						((FileVariable) right).getVar()));
			} else if (right.result().type == VariableType.FOLDER) {
				result = new FileVariable(FolderArithmeticHandler.interpret(((FileVariable) left).getVar(), type,
						((FolderVariable) right).getVar()));
			}
		} else if (left.result().type == VariableType.FOLDER) {
			if (right.result().type == VariableType.NUMBER) {
				result = new FolderVariable(FolderArithmeticHandler.interpret(((FileVariable) left).getVar(), type,
						"" + ((NumberVariable) right).getVar()));
			} else if (right.result().type == VariableType.TEXT) {
				result = new FolderVariable(FolderArithmeticHandler.interpret(((FileVariable) left).getVar(), type,
						((TextVariable) right).getVar()));
			} else if (right.result().type == VariableType.FILE) {
				result = new FileVariable(FolderArithmeticHandler.interpret(((FolderVariable) left).getVar(), type,
						((FileVariable) right).getVar()));
			} else if (right.result().type == VariableType.FOLDER) {
				result = new FileVariable(FolderArithmeticHandler.interpret(((FileVariable) left).getVar(), type,
						((FileVariable) right).getVar()));
			}
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
