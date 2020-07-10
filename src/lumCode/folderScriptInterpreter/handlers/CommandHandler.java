package lumCode.folderScriptInterpreter.handlers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.commons.io.FileUtils;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.CommandErrorException;
import lumCode.folderScriptInterpreter.exceptions.IncorrectParameterTypeException;
import lumCode.folderScriptInterpreter.exceptions.IncorrentParameterAmountException;
import lumCode.folderScriptInterpreter.exceptions.LogicConversionException;
import lumCode.folderScriptInterpreter.exceptions.UndefinedCommandException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedCommandTypeException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedTypeException;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.IntVariable;
import lumCode.folderScriptInterpreter.variables.SpecialVariable;
import lumCode.folderScriptInterpreter.variables.StringVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.VariableType;

public class CommandHandler {

	public static Variable Interpret(char c, String params)
			throws IncorrentParameterAmountException, IncorrectParameterTypeException, LogicConversionException,
			UndefinedCommandException, UnsupportedTypeException, CommandErrorException {
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
			} else if (type == CommandType.REPLACE || (type == CommandType.SUBSTRING && i == 0)) {
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
		} else if (type == CommandType.REPLACE) {
			return replaceCommand(vars);
		} else if (type == CommandType.SUBSTRING) {
			return substringCommand(vars);
		} else if (type == CommandType.PRINT) {
			printCommand(vars);
			return null;
		} else if (type == CommandType.MOVE) {
			return moveCommand(vars);
		} else if (type == CommandType.COPY) {
			return copyCommand(vars);
		} else if (type == CommandType.DELETE) {
			return deleteCommand(vars);
		}
		throw new UndefinedCommandException(type, params);
	}

	private static Variable deleteCommand(Variable[] vars) {
		return new IntVariable(FileUtils.deleteQuietly(((FolderVariable) vars[0]).getVar()) ? 1 : 0);
	}

	private static Variable copyCommand(Variable[] vars) throws UnsupportedCommandTypeException, CommandErrorException {
		try {
			if (Main.overwrite || !((FileVariable) vars[0]).getVar().exists()) {
				if (vars[0] instanceof FileVariable) {
					if (vars[1] instanceof FileVariable) {
						FileUtils.copyFile(((FileVariable) vars[0]).getVar(), ((FileVariable) vars[1]).getVar());
					} else {
						throw new CommandErrorException("Can not copy the folder \"" + vars[0].toString()
								+ "\" to the file \"" + vars[0].toString() + "\".");
					}
				} else {
					if (vars[1] instanceof FileVariable) {
						FileUtils.copyFile(((FileVariable) vars[0]).getVar(), ((FileVariable) vars[1]).getVar());
					} else {
						FileUtils.copyDirectory(((FolderVariable) vars[0]).getVar(),
								((FolderVariable) vars[1]).getVar());
					}
				}
			} else {
				return new IntVariable(0);
			}
		} catch (IOException e) {
			throw new CommandErrorException(
					"Can not copy \"" + vars[0].toString() + "\" to \"" + vars[0].toString() + "\".");
		}
		return new IntVariable(1);
	}

	private static Variable moveCommand(Variable[] vars) throws UnsupportedCommandTypeException, CommandErrorException {
		try {
			if (Main.overwrite || !((FileVariable) vars[0]).getVar().exists()) {
				if (vars[0] instanceof FileVariable) {
					if (vars[1] instanceof FileVariable) {
						FileUtils.moveFile(((FileVariable) vars[0]).getVar(), ((FileVariable) vars[1]).getVar());
					} else {
						throw new CommandErrorException("Can not copy the folder \"" + vars[0].toString()
								+ "\" to the file \"" + vars[0].toString() + "\".");
					}
				} else {
					if (vars[1] instanceof FileVariable) {
						FileUtils.moveFile(((FileVariable) vars[0]).getVar(), ((FileVariable) vars[1]).getVar());
					} else {
						FileUtils.moveDirectory(((FolderVariable) vars[0]).getVar(),
								((FolderVariable) vars[1]).getVar());
					}
				}
			} else {
				return new IntVariable(0);
			}
		} catch (IOException e) {
			throw new CommandErrorException(
					"Can not copy \"" + vars[0].toString() + "\" to \"" + vars[0].toString() + "\".");
		}
		return new IntVariable(1);
	}

	private static void printCommand(Variable[] vars) throws UnsupportedCommandTypeException, CommandErrorException {
		if (vars[1] instanceof SpecialVariable) {
			System.out.println(vars[0].toString());
		} else {
			try {
				Files.write(Paths.get(((FileVariable) vars[1]).getVar().getAbsolutePath()),
						vars[0].toString().getBytes(), StandardOpenOption.APPEND);
			} catch (IOException e) {
				throw new CommandErrorException(
						"Can not print to file \"" + ((FileVariable) vars[1]).getVar().getAbsolutePath() + "\".");
			}
		}
	}

	private static Variable replaceCommand(Variable[] vars) {
		String base = vars[0].toString();
		String look = vars[1].toString();
		String replace = vars[2].toString();

		String str;
		if (vars[1] instanceof StringVariable && ((StringVariable) vars[1]).isRegex()) {
			str = base.replaceAll(look, replace);
		} else {
			str = base.replace(look, replace);
		}

		return Variable.fromString(str);
	}

	private static Variable substringCommand(Variable[] vars)
			throws UnsupportedCommandTypeException, CommandErrorException {
		String base = vars[0].toString();
		int arg[] = new int[2];
		arg[0] = 0;
		arg[1] = base.length();

		for (int i = 0; i < 2; i++) {
			if (vars[i + 1] instanceof IntVariable) {
				arg[i] = ((IntVariable) vars[i + 1]).getVar();
			} else if (!(vars[i + 1] instanceof SpecialVariable)) {
				arg[i] = base.indexOf(((StringVariable) vars[i + 1]).getVar())
						- ((StringVariable) vars[i + 1]).getVar().length();
			}
		}

		try {
			return Variable.fromString(base.substring(arg[0], arg[1]));
		} catch (StringIndexOutOfBoundsException e) {
			throw new CommandErrorException(
					"Can not get a substring of \"" + base + "\" taking from \"" + arg[0] + "\" to \"" + arg[1] + "\"");
		}
	}

	private static StringVariable nameCommand(Variable[] vars) {
		return new StringVariable(((FolderVariable) vars[0]).getName());
	}

	private static FolderVariable parentCommand(Variable[] vars) {
		return new FolderVariable(((FolderVariable) vars[0]).getParent());
	}

	private static StringVariable extensionCommand(Variable[] vars) {
		return new StringVariable(((FileVariable) vars[0]).getExtension());
	}

	private static IntVariable isFileCommand(Variable[] vars) {
		return new IntVariable(vars[0] instanceof FileVariable ? 1 : 0);
	}

	private static IntVariable isAvailableCommand(Variable[] vars) {
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