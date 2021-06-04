package lumCode.folderScriptInterpreter.handlers.logic;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.UndefinedLogicException;
import lumCode.folderScriptInterpreter.handlers.ResultantNode;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.TextVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.VariableType;

public class Logic implements ResultantNode {
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
		boolean found = false;
		if (left.type == VariableType.NUMBER) {
			if (right.type == VariableType.NUMBER) {
				result = NumberLogicHandler.Interpret(((NumberVariable) left).getVar(), type,
						((NumberVariable) right).getVar());
				found = true;
			}
		} else if (left.type == VariableType.TEXT) {
			if (right.type == VariableType.TEXT) {
				result = StringLogicHandler.Interpret(((TextVariable) left).getVar(), type,
						((TextVariable) right).getVar());
				found = true;
			}
		} else if (left.type == VariableType.FILE) {
			if (right.type == VariableType.FILE) {
				result = FileLogicHandler.Interpret(((FileVariable) left).getVar(), type,
						((FileVariable) right).getVar());
				found = true;
			}
		} else if (left.type == VariableType.FOLDER) {
			if (right.type == VariableType.FOLDER) {
				result = FolderLogicHandler.Interpret(((FolderVariable) left).getVar(), type,
						((FolderVariable) right).getVar());
				found = true;
			}
		}

		if (!found) {
			throw new UndefinedLogicException(left.toString(), type, right.toString());
		}
	}

	@Override
	public void explain() {
		// TODO Auto-generated method stub

	}

	@Override
	public Variable result() {
		return new NumberVariable(result ? 1 : 0);
	}
}
