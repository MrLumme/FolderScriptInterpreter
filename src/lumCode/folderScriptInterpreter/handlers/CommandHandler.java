package lumCode.folderScriptInterpreter.handlers;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.IncorrectParameterTypeException;
import lumCode.folderScriptInterpreter.exceptions.IncorrentParameterAmountException;
import lumCode.folderScriptInterpreter.exceptions.LogicConversionException;
import lumCode.folderScriptInterpreter.exceptions.UndefinedCommandException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedTypeException;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.IntVariable;
import lumCode.folderScriptInterpreter.variables.StringVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.VariableType;

public class CommandHandler {

	public static Variable Interpret(char c, String params)
			throws IncorrentParameterAmountException, IncorrectParameterTypeException, LogicConversionException,
			UndefinedCommandException, UnsupportedTypeException {
		CommandType type = CommandType.fromChar(c);

		String[] list = params.split(",");
		if (CommandType.varCount(type) != list.length) {
			throw new IncorrentParameterAmountException(type, list.length);
		}

		// Make variables

		Variable[] vars = new Variable[list.length];
		for (int i = 0; i < list.length; i++) {
			vars[i] = Variable.fromString(list[i]);

			if (type == CommandType.COPY || type == CommandType.DELETE || type == CommandType.MOVE
					|| type == CommandType.EXTENSION || type == CommandType.NAME || type == CommandType.IS_FILE
					|| type == CommandType.IS_AVAILABLE || type == CommandType.PARENT || type == CommandType.READ
					|| (i == 0 && type == CommandType.LIST)) {
				if (vars[i].type != VariableType.FILE && vars[i].type != VariableType.FOLDER) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			} else if (type == CommandType.REPLACE || type == CommandType.SUBSTRING) {
				if (vars[i].type == VariableType.SPECIAL) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			} else if (type == CommandType.RANDOM) {
				if (vars[i].type != VariableType.INT) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			} else if (i == 1 && type == CommandType.LIST) {
				if (vars[i].type != VariableType.INT && vars[i].type != VariableType.SPECIAL) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			} else if (type == CommandType.OVERWRITE) {
				if (vars[i].type != VariableType.INT) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			} else if (type == CommandType.PRINT && i == 1) {
				if (vars[i].type != VariableType.FILE && vars[i].type != VariableType.SPECIAL) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			}
		}

		// Perform action

		if (type == CommandType.OVERWRITE) {
			overwriteCommand(vars);
			return null;
		} else if (type == CommandType.RANDOM) {
			return randomCommand(vars);
		} else if (type == CommandType.IS_FILE) {
			return isFileCommand(vars);
		} else if (type == CommandType.IS_AVAILABLE) {
			return isAvailableCommand(vars);
		} else if (type == CommandType.NAME) {
			return nameCommand(vars);
		} else if (type == CommandType.PARENT) {
			return parentCommand(vars);
		} else if (type == CommandType.EXTENSION) {
			return extensionCommand(vars);
		}
		throw new UndefinedCommandException(type, params);
	}

	private static Variable nameCommand(Variable[] vars) {
		return new StringVariable(((FolderVariable) vars[0]).getName());
	}

	private static Variable parentCommand(Variable[] vars) {
		return new FolderVariable(((FolderVariable) vars[0]).getParent());
	}

	private static Variable extensionCommand(Variable[] vars) {
		return new StringVariable(((FileVariable) vars[0]).getExtension());
	}

	private static Variable isFileCommand(Variable[] vars) {
		return new IntVariable(vars[0] instanceof FileVariable ? 1 : 0);
	}

	private static Variable isAvailableCommand(Variable[] vars) {
		return new IntVariable(((FolderVariable) vars[0]).getVar().exists() ? 1 : 0);
	}

	private static IntVariable randomCommand(Variable[] vars) {
		double u = ((IntVariable) vars[0]).getVar()
				+ (Math.random() * (((IntVariable) vars[1]).getVar() - ((IntVariable) vars[0]).getVar()));
		return new IntVariable((int) Math.floor(u));
	}

	private static void overwriteCommand(Variable[] vars) throws LogicConversionException {
		Main.overwrite = ((IntVariable) vars[0]).asBoolean();
	}
}