package lumCode.folderScriptInterpreter.handlers;

import lumCode.folderScriptInterpreter.exceptions.UndefinedArithmeticException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedArithmeticTypeException;
import lumCode.folderScriptInterpreter.variables.IntVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.VariableType;

public class ArithmeticHandler {

	public Variable Interpret(Variable left, ArithmeticType operator, Variable right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {
		if (left.type == VariableType.INT) {
			if (right.type == VariableType.INT) {
				return new IntVariable(numberArithmetic((IntVariable) left, operator, (IntVariable) right));
			} else if (left.type == VariableType.STRING) {

			} else if (left.type == VariableType.FILE) {

			} else if (left.type == VariableType.FOLDER) {

			}
		} else if (left.type == VariableType.STRING) {
			if (right.type == VariableType.INT) {

			} else if (left.type == VariableType.STRING) {

			} else if (left.type == VariableType.FILE) {

			} else if (left.type == VariableType.FOLDER) {

			}
		} else if (left.type == VariableType.FILE) {
			if (right.type == VariableType.INT) {

			} else if (left.type == VariableType.STRING) {

			} else if (left.type == VariableType.FILE) {

			} else if (left.type == VariableType.FOLDER) {

			}
		} else if (left.type == VariableType.FOLDER) {
			if (right.type == VariableType.INT) {

			} else if (left.type == VariableType.STRING) {

			} else if (left.type == VariableType.FILE) {

			} else if (left.type == VariableType.FOLDER) {

			}
		}
		throw new UndefinedArithmeticException("No arithmetic have been defined for setup '" + left.toString() + " "
				+ ArithmeticType.toChar(operator) + " " + right.toString() + "'.");
	}

	private int numberArithmetic(IntVariable left, ArithmeticType operator, IntVariable right)
			throws UnsupportedArithmeticTypeException {
		switch (operator) {
		case ADDITION:
			return left.getVar() + right.getVar();
		case DIVISION:
			return left.getVar() / right.getVar();
		case MODULO:
			return left.getVar() % right.getVar();
		case MULTIPLICATION:
			return left.getVar() * right.getVar();
		case SUBTRACTION:
			return left.getVar() - right.getVar();
		}
		throw new UnsupportedArithmeticTypeException("The operator '" + operator.toString() + "' is unsupported.");
	}

}
