package lumCode.folderScriptInterpreter.handlers.logic;

import java.io.FileNotFoundException;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.undefinedExceptions.UndefinedLogicException;
import lumCode.folderScriptInterpreter.handlers.ResultantNode;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.TextVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.VariableType;

public class Logic implements ResultantNode {
	private final ResultantNode left, right;
	private final LogicType type;

	private boolean result;

	public Logic(ResultantNode left, LogicType type, ResultantNode right) {
		this.left = left;
		this.right = right;
		this.type = type;
	}

	@Override
	public void action() throws InterpreterException {
		left.action();
		right.action();
		Variable l = left.result();
		Variable r = right.result();

		boolean found = false;
		if (l.type == VariableType.NUMBER) {
			if (r.type == VariableType.NUMBER) {
				result = NumberLogicHandler.Interpret(((NumberVariable) l).getVar(), type,
						((NumberVariable) r).getVar());
				found = true;
			}
		} else if (l.type == VariableType.TEXT) {
			if (r.type == VariableType.TEXT) {
				result = TextLogicHandler.Interpret(((TextVariable) l).getVar(), type, ((TextVariable) r).getVar());
				found = true;
			}
		} else if (l.type == VariableType.FILE) {
			if (r.type == VariableType.FILE) {
				try {
					result = FileLogicHandler.Interpret(((FileVariable) l).getVar(), type, ((FileVariable) r).getVar());
				} catch (FileNotFoundException e) {
					throw new InterpreterException("Interpreter failed with the following message: " + e.getMessage());
				}
				found = true;
			}
		} else if (l.type == VariableType.FOLDER) {
			if (r.type == VariableType.FOLDER) {
				try {
					result = FolderLogicHandler.Interpret(((FolderVariable) l).getVar(), type,
							((FolderVariable) r).getVar());
				} catch (FileNotFoundException e) {
					throw new InterpreterException("Interpreter failed with the following message: " + e.getMessage());
				}
				found = true;
			}
		}

		if (!found) {
			throw new UndefinedLogicException(left.toString(), type, right.toString());
		}
	}

	@Override
	public Variable result() {
		return new NumberVariable(result ? 1 : 0);
	}

	@Override
	public String toString() {
		return left.toString() + type.getChar() + right.toString();
	}
}
