package lumCode.folderScriptInterpreter.handlers.command;

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
import lumCode.folderScriptInterpreter.exceptions.VariableNameNotFoundException;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.IntVariable;
import lumCode.folderScriptInterpreter.variables.SpecialVariable;
import lumCode.folderScriptInterpreter.variables.StringVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.VariableType;

public class Command implements CommandNode {
	private final CommandType type;
	private final Variable[] vars;
	private Variable[] result;

	public Command(CommandType type, String params) throws IncorrentParameterAmountException,
			IncorrectParameterTypeException, VariableNameNotFoundException, UnsupportedCommandTypeException {
		this.type = type;

		String[] list = params.split(",");
		if (CommandType.inputCount(type) != list.length) {
			throw new IncorrentParameterAmountException(type, list.length);
		}

		// Make variables

		vars = new Variable[list.length];
		for (int i = 0; i < list.length; i++) {
			vars[i] = Variable.interpret(list[i]);

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
			} else if (type == CommandType.OVERWRITE || type == CommandType.CASE_SENSITIVE) {
				if (vars[i].type != VariableType.INT) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			} else if (type == CommandType.PRINT && i == 1) {
				if (vars[i].type != VariableType.FILE && vars[i].type != VariableType.SPECIAL) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			}
		}

		result = null;
	}

	@Override
	public void action() throws UnsupportedCommandTypeException, CommandErrorException, LogicConversionException,
			UndefinedCommandException {
		if (type == CommandType.OVERWRITE) {
			overwriteCommand();
		} else if (type == CommandType.CASE_SENSITIVE) {
			caseSensitiveCommand();
		} else if (type == CommandType.RANDOM) {
			result = new Variable[] { randomCommand() };
		} else if (type == CommandType.IS_FILE) {
			result = new Variable[] { isFileCommand() };
		} else if (type == CommandType.IS_AVAILABLE) {
			result = new Variable[] { isAvailableCommand() };
		} else if (type == CommandType.NAME) {
			result = new Variable[] { nameCommand() };
		} else if (type == CommandType.PARENT) {
			result = new Variable[] { parentCommand() };
		} else if (type == CommandType.EXTENSION) {
			result = new Variable[] { extensionCommand() };
		} else if (type == CommandType.REPLACE) {
			result = new Variable[] { replaceCommand() };
		} else if (type == CommandType.SUBSTRING) {
			result = new Variable[] { substringCommand() };
		} else if (type == CommandType.PRINT) {
			printCommand();
		} else if (type == CommandType.MOVE) {
			result = new Variable[] { moveCommand() };
		} else if (type == CommandType.COPY) {
			result = new Variable[] { copyCommand() };
		} else if (type == CommandType.DELETE) {
			result = new Variable[] { deleteCommand() };
		} else if (type == CommandType.LIST) {
			// todo
		} else if (type == CommandType.SIZE) {
			// todo
		} else if (type == CommandType.EXIT) {
			exitCommand();
		}
		throw new UndefinedCommandException(type, vars);
	}

	private void exitCommand() {
		System.exit(((IntVariable) vars[0]).getVar());
	}

	private Variable deleteCommand() {
		return new IntVariable(FileUtils.deleteQuietly(((FolderVariable) vars[0]).getVar()) ? 1 : 0);
	}

	private Variable copyCommand() throws UnsupportedCommandTypeException, CommandErrorException {
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

	private Variable moveCommand() throws UnsupportedCommandTypeException, CommandErrorException {
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

	private void printCommand() throws UnsupportedCommandTypeException, CommandErrorException {
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

	private Variable replaceCommand() {
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

	private Variable substringCommand() throws UnsupportedCommandTypeException, CommandErrorException {
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

	private StringVariable nameCommand() {
		return new StringVariable(((FolderVariable) vars[0]).getName());
	}

	private FolderVariable parentCommand() {
		return new FolderVariable(((FolderVariable) vars[0]).getParent());
	}

	private StringVariable extensionCommand() {
		return new StringVariable(((FileVariable) vars[0]).getExtension());
	}

	private IntVariable isFileCommand() {
		return new IntVariable(vars[0] instanceof FileVariable ? 1 : 0);
	}

	private IntVariable isAvailableCommand() {
		return new IntVariable(((FolderVariable) vars[0]).getVar().exists() ? 1 : 0);
	}

	private IntVariable randomCommand() {
		double u = ((IntVariable) vars[0]).getVar()
				+ (Math.random() * (((IntVariable) vars[1]).getVar() - ((IntVariable) vars[0]).getVar()));
		return new IntVariable((int) Math.floor(u));
	}

	private void overwriteCommand() throws LogicConversionException {
		Main.overwrite = ((IntVariable) vars[0]).asBoolean();
	}

	private void caseSensitiveCommand() throws LogicConversionException {
		Main.caseSensitive = ((IntVariable) vars[0]).asBoolean();
	}

	@Override
	public void explain() {
		// TODO Auto-generated method stub

	}

	@Override
	public Variable[] result() {
		return result;
	}
}