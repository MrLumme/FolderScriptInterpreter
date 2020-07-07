package lumCode.folderScriptInterpreter.handlers.logic;

import lumCode.folderScriptInterpreter.exceptions.UndefinedLogicException;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.IntVariable;
import lumCode.folderScriptInterpreter.variables.StringVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.VariableType;

public class LogicHandler {

	public static boolean Interpret(Variable left, LogicType operator, Variable right) throws UndefinedLogicException {
		if (left.type == VariableType.INT) {
			return NumberLogicHandler.Interpret(((IntVariable) left).getVar(), operator,
					((IntVariable) right).getVar());
		} else if (left.type == VariableType.STRING) {
			return stringLogic((StringVariable) left, operator, right);
		} else if (left.type == VariableType.FILE) {
			return fileLogic((FileVariable) left, operator, right);
		} else if (left.type == VariableType.FOLDER) {
			return folderLogic((FolderVariable) left, operator, right);
		}
		throw new UndefinedLogicException(
				"The equation '" + left.toString() + operator + right.toString() + "' can not be validated.");
	}

	private boolean folderLogic(FolderVariable left, LogicType operator, Variable right)
			throws UndefinedLogicException {
		if (right.type == VariableType.FOLDER) {
			switch (operator) {
			case EQUAL:
				return left.getVar().getAbsolutePath() == ((FolderVariable) right).getVar().getAbsolutePath();
			case GREATER:
				return left.getVar() < ((FolderVariable) right).getVar();
			case GREATER_EQUAL:
				return left.getVar() <= ((FolderVariable) right).getVar();
			case LESS:
				return left.getVar() > ((FolderVariable) right).getVar();
			case LESS_EQUAL:
				return left.getVar() >= ((FolderVariable) right).getVar();
			case NOT:
				return left.getVar() != ((FolderVariable) right).getVar();
			}
		}
		throw new UndefinedLogicException(
				"The equation '" + left.toString() + operator + right.toString() + "' can not be validated.");
	}

	private boolean fileLogic(FileVariable left, LogicType operator, Variable right) throws UndefinedLogicException {
		if (right.type == VariableType.STRING) {
			switch (operator) {
			case EQUAL:
				return left.getVar() == ((FileVariable) right).getVar();
			case GREATER:
				return left.getVar() < ((FileVariable) right).getVar();
			case GREATER_EQUAL:
				return left.getVar() <= ((FileVariable) right).getVar();
			case LESS:
				return left.getVar() > ((FileVariable) right).getVar();
			case LESS_EQUAL:
				return left.getVar() >= ((FileVariable) right).getVar();
			case NOT:
				return left.getVar() != ((FileVariable) right).getVar();
			}
		}
		throw new UndefinedLogicException(
				"The equation '" + left.toString() + operator + right.toString() + "' can not be validated.");
	}

	private boolean stringLogic(StringVariable left, LogicType operator, Variable right)
			throws UndefinedLogicException {
		if (right.type == VariableType.STRING) {
			switch (operator) {
			case EQUAL:
				return left.getVar() == ((StringVariable) right).getVar();
			case GREATER:
				return left.getVar() < ((StringVariable) right).getVar();
			case GREATER_EQUAL:
				return left.getVar() <= ((StringVariable) right).getVar();
			case LESS:
				return left.getVar() > ((StringVariable) right).getVar();
			case LESS_EQUAL:
				return left.getVar() >= ((StringVariable) right).getVar();
			case NOT:
				return left.getVar() != ((StringVariable) right).getVar();
			}
		}
		throw new UndefinedLogicException(
				"The equation '" + left.toString() + operator + right.toString() + "' can not be validated.");
	}

}
