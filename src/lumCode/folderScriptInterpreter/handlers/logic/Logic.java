package lumCode.folderScriptInterpreter.handlers.logic;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.UndefinedLogicException;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.IntVariable;
import lumCode.folderScriptInterpreter.variables.StringVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.VariableType;

public class Logic implements LogicNode {
	private final Variable left, right;
	private final LogicType type;
	private boolean result;

	public Logic(Variable left, LogicType type, Variable right) {
		this.left = left;
		this.right = right;
		this.type = type;
	}

	@Override
	public void action() throws InterpreterException {
		if (left.type == VariableType.INT) {
			if (right.type == VariableType.INT) {
				result = NumberLogicHandler.Interpret(((IntVariable) left).getVar(), type,
						((IntVariable) right).getVar());
			}
		} else if (left.type == VariableType.STRING) {
			if (right.type == VariableType.STRING) {
				result = StringLogicHandler.Interpret(((StringVariable) left).getVar(), type,
						((StringVariable) right).getVar());
			}
		} else if (left.type == VariableType.FILE) {
			if (right.type == VariableType.FILE) {
				result = FileLogicHandler.Interpret(((FileVariable) left).getVar(), type,
						((FileVariable) right).getVar());
			}
		} else if (left.type == VariableType.FOLDER) {
			if (right.type == VariableType.FOLDER) {
				result = FolderLogicHandler.Interpret(((FolderVariable) left).getVar(), type,
						((FolderVariable) right).getVar());
			}
		}
		throw new UndefinedLogicException(left.toString(), type, right.toString());
	}

	@Override
	public void explain() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean result() {
		return result;
	}
}
