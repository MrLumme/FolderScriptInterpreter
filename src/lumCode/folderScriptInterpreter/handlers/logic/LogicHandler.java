package lumCode.folderScriptInterpreter.handlers.logic;

import lumCode.folderScriptInterpreter.exceptions.UndefinedLogicException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedLogicTypeException;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.IntVariable;
import lumCode.folderScriptInterpreter.variables.StringVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.VariableType;

public class LogicHandler {

	public static boolean Interpret(Variable left, LogicType operator, Variable right)
			throws UndefinedLogicException, UnsupportedLogicTypeException {
		if (left.type == VariableType.INT) {
			if (right.type == VariableType.INT) {
				return NumberLogicHandler.Interpret(((IntVariable) left).getVar(), operator,
						((IntVariable) right).getVar());
			}
		} else if (left.type == VariableType.STRING) {
			if (right.type == VariableType.STRING) {
				return StringLogicHandler.Interpret(((StringVariable) left).getVar(), operator,
						((StringVariable) right).getVar());
			}
		} else if (left.type == VariableType.FILE) {
			if (right.type == VariableType.FILE) {
				return FileLogicHandler.Interpret(((FileVariable) left).getVar(), operator,
						((FileVariable) right).getVar());
			}
		} else if (left.type == VariableType.FOLDER) {
			if (right.type == VariableType.FOLDER) {
				return FolderLogicHandler.Interpret(((FolderVariable) left).getVar(), operator,
						((FolderVariable) right).getVar());
			}
		}
		throw new UndefinedLogicException(left.toString(), operator, right.toString());
	}

}
