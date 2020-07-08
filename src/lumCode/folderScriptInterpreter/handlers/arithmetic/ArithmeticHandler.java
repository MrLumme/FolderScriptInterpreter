package lumCode.folderScriptInterpreter.handlers.arithmetic;

import java.io.File;

import lumCode.folderScriptInterpreter.exceptions.UndefinedArithmeticException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedArithmeticTypeException;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.IntVariable;
import lumCode.folderScriptInterpreter.variables.StringVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.VariableType;

public class ArithmeticHandler {

	public Variable Interpret(Variable left, ArithmeticType operator, Variable right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {

		if (left.type == VariableType.INT) {
			if (right.type == VariableType.INT) {
				return new IntVariable(
						numberArithmetic(((IntVariable) left).getVar(), operator, ((IntVariable) right).getVar()));
			} else if (right.type == VariableType.STRING) {
				return new StringVariable(stringArithmetic("" + ((IntVariable) left).getVar(), operator,
						((StringVariable) right).getVar()));
			} else if (right.type == VariableType.FILE) {
//				return new FileVariable(
//						fileArithmetic(((IntVariable) left).getVar(), operator, ((FileVariable) right).getVar()));
			} else if (right.type == VariableType.FOLDER) {
				return new FolderVariable(
						folderArithmetic(((IntVariable) left).getVar(), operator, ((FolderVariable) right).getVar()));
			}
		} else if (left.type == VariableType.STRING) {
			if (right.type == VariableType.INT) {
				return new StringVariable(stringArithmetic(((StringVariable) left).getVar(), operator,
						"" + ((IntVariable) right).getVar()));
			} else if (right.type == VariableType.STRING) {
				return new StringVariable(stringArithmetic(((StringVariable) left).getVar(), operator,
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
				return new FileVariable(
						fileArithmetic(((FileVariable) left).getVar(), operator, "" + ((IntVariable) right).getVar()));
			} else if (right.type == VariableType.STRING) {
				return new FileVariable(
						fileArithmetic(((FileVariable) left).getVar(), operator, ((StringVariable) right).getVar()));
			} else if (right.type == VariableType.FILE) {
//				return new FileVariable(
//						fileArithmetic(((FileVariable) left).getVar(), operator, ((IntVariable) right).getVar()));
			} else if (right.type == VariableType.FOLDER) {
				return new FileVariable(
						folderArithmetic(((FileVariable) right).getVar(), operator, ((FileVariable) left).getVar()));
			}
		} else if (left.type == VariableType.FOLDER) {
			if (right.type == VariableType.INT) {
				return new FolderVariable(folderArithmetic(((FileVariable) left).getVar(), operator,
						"" + ((IntVariable) right).getVar()));
			} else if (right.type == VariableType.STRING) {
				return new FolderVariable(
						folderArithmetic(((FileVariable) left).getVar(), operator, ((StringVariable) right).getVar()));
			} else if (right.type == VariableType.FILE) {
//				return new FileVariable(
//						fileArithmetic(((FileVariable) left).getVar(), operator, ((FileVariable) right).getVar()));
			} else if (right.type == VariableType.FOLDER) {
				return new FileVariable(
						folderArithmetic(((FileVariable) left).getVar(), operator, ((FileVariable) right).getVar()));
			}
		}
		throw new UndefinedArithmeticException("No arithmetic have been defined for setup '" + left.toString() + " "
				+ ArithmeticType.toChar(operator) + " " + right.toString() + "'.");
	}

	private String stringArithmetic(String left, ArithmeticType operator, String right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {
		switch (operator) {
		case ADDITION:
			return left + right;
		case SUBTRACTION:
			return left.replace(right, "");
		default:
			throw new UndefinedArithmeticException("No arithmetic have been defined for setup '" + left + " "
					+ ArithmeticType.toChar(operator) + " " + right + "'.");
		}
	}

	private int numberArithmetic(int left, ArithmeticType operator, int right)
			throws UnsupportedArithmeticTypeException {
		switch (operator) {
		case ADDITION:
			return left + right;
		case DIVISION:
			return left / right;
		case MODULO:
			return left % right;
		case MULTIPLICATION:
			return left * right;
		case SUBTRACTION:
			return left - right;
		default:
			throw new UnsupportedArithmeticTypeException(operator);
		}
	}

	private File fileArithmetic(File left, ArithmeticType operator, String right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {
		String path = left.getParent();
		String name = left.getName();
		String ext = left.getName().substring(left.getName().indexOf('.'));

		switch (operator) {
		case ADDITION:
			return new File(path + "\\" + name + right + "." + ext);
		default:
			throw new UndefinedArithmeticException("No arithmetic have been defined for setup '"
					+ left.getAbsolutePath() + " " + ArithmeticType.toChar(operator) + " " + right + "'.");
		}
	}

	private File fileArithmetic(String left, ArithmeticType operator, File right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {
		String path = right.getParent();
		String name = right.getName();
		String ext = right.getName().substring(right.getName().indexOf('.'));

		switch (operator) {
		case ADDITION:
			return new File(path + "\\" + left + name + "." + ext);
		default:
			throw new UndefinedArithmeticException("No arithmetic have been defined for setup '" + left + " "
					+ ArithmeticType.toChar(operator) + " " + right.getAbsolutePath() + "'.");
		}
	}

	private File folderArithmetic(File left, ArithmeticType operator, File right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {
		String path = left.getAbsolutePath();
		String name = right.getName();
		String ext = right.getName().substring(right.getName().indexOf('.'));

		switch (operator) {
		case ADDITION:
			return new File(path + "\\" + name + "." + ext);
		default:
			throw new UndefinedArithmeticException("No arithmetic have been defined for setup '" + left + " "
					+ ArithmeticType.toChar(operator) + " " + right.getAbsolutePath() + "'.");
		}
	}

	private File folderArithmetic(File left, ArithmeticType operator, String right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {
		String path = left.getParent();
		String name = left.getName();

		switch (operator) {
		case ADDITION:
			return new File(path + "\\" + name + right);
		default:
			throw new UndefinedArithmeticException("No arithmetic have been defined for setup '"
					+ left.getAbsolutePath() + " " + ArithmeticType.toChar(operator) + " " + right + "'.");
		}
	}

	private File folderArithmetic(String left, ArithmeticType operator, File right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {
		String path = right.getParent();
		String name = right.getName();

		switch (operator) {
		case ADDITION:
			return new File(path + "\\" + left + name);
		default:
			throw new UndefinedArithmeticException("No arithmetic have been defined for setup '" + left + " "
					+ ArithmeticType.toChar(operator) + " " + right.getAbsolutePath() + "'.");
		}
	}

	private File folderArithmetic(int left, ArithmeticType operator, File right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {
		String path = right.getParent();
		String name = right.getName();

		switch (operator) {
		case ADDITION:
			return new File(path + "\\" + left + name);
		default:
			throw new UndefinedArithmeticException("No arithmetic have been defined for setup '" + left + " "
					+ ArithmeticType.toChar(operator) + " " + right.getAbsolutePath() + "'.");
		}
	}

}
