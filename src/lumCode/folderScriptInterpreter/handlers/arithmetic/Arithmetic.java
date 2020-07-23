package lumCode.folderScriptInterpreter.handlers.arithmetic;

import lumCode.folderScriptInterpreter.exceptions.UndefinedArithmeticException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedArithmeticTypeException;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.IntVariable;
import lumCode.folderScriptInterpreter.variables.StringVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.VariableType;

public class Arithmetic {
	private final Variable left, right;
	private final ArithmeticType type;

	public Arithmetic(Variable left, ArithmeticType type, Variable right) {
		this.left = left;
		this.right = right;
		this.type = type;
	}

	public Variable Interpret(Variable left, ArithmeticType operator, Variable right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {

		if (left.type == VariableType.INT) {
			if (right.type == VariableType.INT) {
				return new IntVariable(NumberArithmeticHandler.interpret(((IntVariable) left).getVar(), operator,
						((IntVariable) right).getVar()));
			} else if (right.type == VariableType.STRING) {
				return new StringVariable(StringArithmeticHandler.interpret("" + ((IntVariable) left).getVar(),
						operator, ((StringVariable) right).getVar()));
			} else if (right.type == VariableType.FILE) {
//				return new FileVariable(
//						fileArithmetic(((IntVariable) left).getVar(), operator, ((FileVariable) right).getVar()));
			} else if (right.type == VariableType.FOLDER) {
				return new FolderVariable(FolderArithmeticHandler.interpret(((IntVariable) left).getVar(), operator,
						((FolderVariable) right).getVar()));
			}
		} else if (left.type == VariableType.STRING) {
			if (right.type == VariableType.INT) {
				return new StringVariable(StringArithmeticHandler.interpret(((StringVariable) left).getVar(), operator,
						"" + ((IntVariable) right).getVar()));
			} else if (right.type == VariableType.STRING) {
				return new StringVariable(StringArithmeticHandler.interpret(((StringVariable) left).getVar(), operator,
						((StringVariable) right).getVar()));
			} else if (right.type == VariableType.FILE) {
//				return new StringVariable(
//						fileArithmetic(((StringVariable) left).getVar(), operator, ((FileVariable) right).getVar()));
			} else if (right.type == VariableType.FOLDER) {
//				return new IntVariable(folderArithmetic(((StringVariable) left).getVar(), operator,
//						((FolderVariable) right).getVar()));
			}
		} else if (left.type == VariableType.FILE) {
			if (right.type == VariableType.INT) {
				return new FileVariable(FileArithmeticHandler.interpret(((FileVariable) left).getVar(), operator,
						"" + ((IntVariable) right).getVar()));
			} else if (right.type == VariableType.STRING) {
				return new FileVariable(FileArithmeticHandler.interpret(((FileVariable) left).getVar(), operator,
						((StringVariable) right).getVar()));
			} else if (right.type == VariableType.FILE) {
//				return new FileVariable(
//						fileArithmetic(((FileVariable) left).getVar(), operator, ((IntVariable) right).getVar()));
			} else if (right.type == VariableType.FOLDER) {
				return new FileVariable(FolderArithmeticHandler.interpret(((FileVariable) right).getVar(), operator,
						((FileVariable) left).getVar()));
			}
		} else if (left.type == VariableType.FOLDER) {
			if (right.type == VariableType.INT) {
				return new FolderVariable(FolderArithmeticHandler.interpret(((FileVariable) left).getVar(), operator,
						"" + ((IntVariable) right).getVar()));
			} else if (right.type == VariableType.STRING) {
				return new FolderVariable(FolderArithmeticHandler.interpret(((FileVariable) left).getVar(), operator,
						((StringVariable) right).getVar()));
			} else if (right.type == VariableType.FILE) {
//				return new FileVariable(
//						fileArithmetic(((FileVariable) left).getVar(), operator, ((FileVariable) right).getVar()));
			} else if (right.type == VariableType.FOLDER) {
				return new FileVariable(FolderArithmeticHandler.interpret(((FileVariable) left).getVar(), operator,
						((FileVariable) right).getVar()));
			}
		}
		throw new UndefinedArithmeticException(left.toString(), operator, right.toString());
	}

}
