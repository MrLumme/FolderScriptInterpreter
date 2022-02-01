package lumCode.folderScriptInterpreter.handlers.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.Utilities;
import lumCode.folderScriptInterpreter.exceptions.CommandErrorException;
import lumCode.folderScriptInterpreter.exceptions.IncorrentParameterAmountException;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.LogicConversionException;
import lumCode.folderScriptInterpreter.exceptions.arrayExceptions.ArrayPositionEmptyException;
import lumCode.folderScriptInterpreter.exceptions.typeExceptions.IncorrectParameterTypeException;
import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedCommandTypeException;
import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedVariableTypeException;
import lumCode.folderScriptInterpreter.exceptions.undefinedExceptions.UndefinedCommandException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.ResultantNode;
import lumCode.folderScriptInterpreter.variables.ArrayVariable;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.SpecialVariable;
import lumCode.folderScriptInterpreter.variables.TextVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.VariableType;

public class Command implements ResultantNode {
	private final CommandType type;
	private final List<Node> input;
	private Variable output;
	private Variable[] vars;

	public Command(CommandType type, List<Node> input) throws IncorrentParameterAmountException, CommandErrorException {
		this.type = type;
		this.input = input;
		output = null;
		vars = null;

		if (type.getInput() != input.size()) {
			throw new IncorrentParameterAmountException(type, input.size());
		}
		for (Node i : input) {
			if (!(i instanceof ResultantNode)) {
				throw new CommandErrorException("The commands needs resulting inputs, which at least one is not.");
			}
		}
	}

	@Override
	public void action() throws InterpreterException {
		// Fetch results
		vars = new Variable[input.size()];
		for (int i = 0; i < input.size(); i++) {
			input.get(i).action();
			vars[i] = ((ResultantNode) input.get(i)).result();
		}

		// TODO match to Zip and MD5 commands too

		for (int i = 0; i < vars.length; i++) {
			if (type == CommandType.COPY || type == CommandType.DELETE || type == CommandType.MOVE
					|| type == CommandType.NAME || type == CommandType.IS_FILE || type == CommandType.IS_AVAILABLE
					|| type == CommandType.PARENT) {
				if (vars[i].type != VariableType.FILE && vars[i].type != VariableType.FOLDER) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			} else if (type == CommandType.REPLACE
					|| (i == 0 && (type == CommandType.SUBSTRING || type == CommandType.LIST))) {
				if (vars[i].type == VariableType.SPECIAL) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			} else if (type == CommandType.RANDOM || type == CommandType.EXIT || type == CommandType.SLEEP) {
				if (vars[i].type != VariableType.NUMBER) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			} else if (i == 1 && type == CommandType.LIST) {
				if (vars[i].type != VariableType.NUMBER && vars[i].type != VariableType.SPECIAL) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			} else if (type == CommandType.OVERWRITE || type == CommandType.CASE_SENSITIVE) {
				if (vars[i].type != VariableType.NUMBER || ((NumberVariable) vars[i]).isBoolean()) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			} else if (type == CommandType.OUTPUT && i == 1) {
				if (vars[i].type != VariableType.FILE && vars[i].type != VariableType.SPECIAL) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			} else if (type == CommandType.EXTENSION || type == CommandType.READ) {
				if (vars[i].type != VariableType.FILE) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			}
		}

		if (type == CommandType.OVERWRITE) {
			overwriteCommand();
		} else if (type == CommandType.CASE_SENSITIVE) {
			caseSensitiveCommand();
		} else if (type == CommandType.RANDOM) {
			output = randomCommand();
		} else if (type == CommandType.IS_FILE) {
			output = isFileCommand();
		} else if (type == CommandType.IS_AVAILABLE) {
			output = isAvailableCommand();
		} else if (type == CommandType.NAME) {
			output = nameCommand();
		} else if (type == CommandType.PARENT) {
			output = parentCommand();
		} else if (type == CommandType.EXTENSION) {
			output = extensionCommand();
		} else if (type == CommandType.REPLACE) {
			output = replaceCommand();
		} else if (type == CommandType.SUBSTRING) {
			output = substringCommand();
		} else if (type == CommandType.OUTPUT) {
			outputCommand();
		} else if (type == CommandType.MOVE) {
			output = moveCommand();
		} else if (type == CommandType.COPY) {
			output = copyCommand();
		} else if (type == CommandType.DELETE) {
			output = deleteCommand();
		} else if (type == CommandType.LIST) {
			output = listCommand();
		} else if (type == CommandType.SIZE) {
			output = sizeCommand();
		} else if (type == CommandType.SLEEP) {
			sleepCommmand();
		} else if (type == CommandType.EXIT) {
			exitCommand();
		} else if (type == CommandType.READ) {
			output = readCommand();
		} else {
			throw new UndefinedCommandException(type, vars);
		}
	}

	private Variable readCommand() throws CommandErrorException {
		File f = ((FileVariable) vars[0]).getVar();
		try {
			BufferedReader r = new BufferedReader(new FileReader(f));
			String o = "";
			String l = r.readLine();
			while (l != null) {
				o += l;
				l = r.readLine();
			}
			r.close();
			return new TextVariable(o);
		} catch (FileNotFoundException e) {
			throw new CommandErrorException("File '" + f.getAbsolutePath() + "' could not be found.");
		} catch (IOException e) {
			throw new CommandErrorException("Encountered a reading error on file '" + f.getAbsolutePath() + "'.");
		}
	}

	private Variable listCommand() throws CommandErrorException {
		ArrayVariable out = new ArrayVariable();
		if (vars[0].type == VariableType.NUMBER) {
			if (vars[1] instanceof SpecialVariable) {
				for (int i = (int) ((NumberVariable) vars[0]).getVar(); i >= 0; i--) {
					out.setNextVar(new NumberVariable(i));
				}
			} else {
				for (int i = (int) ((NumberVariable) vars[1]).getVar(); i < ((NumberVariable) vars[0]).getVar(); i++) {
					out.setNextVar(new NumberVariable(i));
				}
			}
		} else if (vars[0].type == VariableType.ARRAY) {
			if (vars[1].type == VariableType.NUMBER) {
				out = Utilities.sortArray((ArrayVariable) vars[0], (int) ((NumberVariable) vars[1]).getVar());
			} else {
				throw new CommandErrorException(
						"Second input in list command must be a number when first input is an array.");
			}
		} else if (vars[0].type == VariableType.FOLDER) {
			List<File> list = Utilities.listFolder(((FolderVariable) vars[0]).getVar(),
					vars[1] instanceof SpecialVariable ? Integer.MAX_VALUE : (int) ((NumberVariable) vars[1]).getVar(),
					true);
			for (File f : list) {
				out.setNextVar(f.isFile() ? new FileVariable(f) : new FolderVariable(f));
			}
		} else if (vars[0].type == VariableType.FILE) {
			String[] spl = ((FileVariable) vars[0]).getVar().getAbsolutePath()
					.split(System.getProperty("file.separator"));
			for (int i = 0; i < spl.length; i++) {
				out.setVar(i, Variable.fromString(spl[i]));
			}
		} else if (vars[0].type == VariableType.TEXT) {
			String str = ((TextVariable) vars[0]).getVar();
			for (int i = 0; i < str.length(); i++) {
				out.setVar(i, new TextVariable("" + str.charAt(i)));
			}
		}
		return out;
	}

	private Variable sizeCommand() throws CommandErrorException {
		try {
			return new NumberVariable(Utilities.varSize(vars[0]));
		} catch (UnsupportedVariableTypeException e) {
			throw new CommandErrorException("Unsupported variable: " + e.getMessage());
		}
	}

	private void sleepCommmand() {
		try {
			Thread.sleep(((NumberVariable) vars[0]).getVar());
		} catch (InterruptedException e) {
			// Do nothing
		}
	}

	private void exitCommand() {
		System.exit((int) ((NumberVariable) vars[0]).getVar());
	}

	private Variable deleteCommand() {
		return new NumberVariable(FileUtils.deleteQuietly(((FolderVariable) vars[0]).getVar()) ? 1 : 0);
	}

	private Variable copyCommand() throws UnsupportedCommandTypeException, CommandErrorException {
		try {
			if (Main.overwrite || vars[1] instanceof FolderVariable
					|| (vars[1] instanceof FileVariable && !((FileVariable) vars[1]).getVar().exists())) {
				if (vars[0] instanceof FileVariable) {
					if (vars[1] instanceof FileVariable) {
						FileUtils.copyFile(((FileVariable) vars[0]).getVar(), ((FileVariable) vars[1]).getVar());
					} else {
						FileUtils.copyFileToDirectory(((FileVariable) vars[0]).getVar(),
								((FolderVariable) vars[1]).getVar(), true);
					}
				} else {
					if (vars[1] instanceof FileVariable) {
						throw new CommandErrorException("Can not copy the folder \"" + vars[0].toString()
								+ "\" to the file \"" + vars[0].toString() + "\".");
					} else {
						FileUtils.copyDirectory(((FolderVariable) vars[0]).getVar(),
								((FolderVariable) vars[1]).getVar());
					}
				}
			} else {
				return new NumberVariable(0);
			}
		} catch (IOException e) {
			throw new CommandErrorException(
					"Can not copy \"" + vars[0].toString() + "\" to \"" + vars[0].toString() + "\".");
		}
		return new NumberVariable(1);
	}

	private Variable moveCommand() throws UnsupportedCommandTypeException, CommandErrorException {
		try {
			if (Main.overwrite || vars[1] instanceof FolderVariable
					|| (vars[1] instanceof FileVariable && !((FileVariable) vars[1]).getVar().exists())) {
				if (vars[0] instanceof FileVariable) {
					if (vars[1] instanceof FileVariable) {
						FileUtils.moveFile(((FileVariable) vars[0]).getVar(), ((FileVariable) vars[1]).getVar());
					} else {
						FileUtils.moveFileToDirectory(((FileVariable) vars[0]).getVar(),
								((FolderVariable) vars[1]).getVar(), true);
					}
				} else {
					if (vars[1] instanceof FileVariable) {
						throw new CommandErrorException("Can not move \"" + vars[0].toString() + "\" to the file \""
								+ vars[0].toString() + "\".");
					} else {
						FileUtils.moveDirectory(((FolderVariable) vars[0]).getVar(),
								((FolderVariable) vars[1]).getVar());
					}
				}
			} else {
				return new NumberVariable(0);
			}
		} catch (IOException e) {
			throw new CommandErrorException(
					"Can not move \"" + vars[0].toString() + "\" to \"" + vars[0].toString() + "\".");
		}
		return new NumberVariable(1);
	}

	private void outputCommand() throws UnsupportedCommandTypeException, CommandErrorException {
		if (vars[1] instanceof SpecialVariable) {
			System.out.print(vars[0].toString());
		} else if (vars[1] instanceof FileVariable) {
			File f = ((FileVariable) vars[1]).getVar();
			try {
				if (f.exists()) {
					Files.write(Paths.get(f.getAbsolutePath()), vars[0].toString().getBytes(),
							StandardOpenOption.APPEND);
				} else {
					Files.write(Paths.get(f.getAbsolutePath()), vars[0].toString().getBytes(),
							StandardOpenOption.CREATE);
				}
			} catch (IOException e) {
				throw new CommandErrorException(
						"Can not print to file \"" + ((FileVariable) vars[1]).getVar().getAbsolutePath() + "\".");
			}
		} else {
			throw new CommandErrorException(
					"Print location \"" + ((FileVariable) vars[1]).getVar().getAbsolutePath() + "\" is invalid.");
		}
	}

	private Variable replaceCommand() {
		String base = vars[0].toString();
		String look = vars[1].toString();
		String replace = vars[2].toString();
		String str;
		if (vars[1] instanceof TextVariable && ((TextVariable) vars[1]).isRegex()) {
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
			if (vars[i + 1] instanceof NumberVariable) {
				arg[i] = (int) ((NumberVariable) vars[i + 1]).getVar();
			} else if (!(vars[i + 1] instanceof SpecialVariable)) {
				arg[i] = base.indexOf(((TextVariable) vars[i + 1]).getVar())
						- ((TextVariable) vars[i + 1]).getVar().length();
			}
		}

		try {
			return Variable.fromString(base.substring(arg[0], arg[1]));
		} catch (StringIndexOutOfBoundsException e) {
			throw new CommandErrorException(
					"Can not get a substring of \"" + base + "\" taking from \"" + arg[0] + "\" to \"" + arg[1] + "\"");
		}
	}

	private TextVariable nameCommand() {
		return new TextVariable(((FolderVariable) vars[0]).getName());
	}

	private FolderVariable parentCommand() {
		return new FolderVariable(((FolderVariable) vars[0]).getParent());
	}

	private TextVariable extensionCommand() {
		return new TextVariable(((FileVariable) vars[0]).getExtension());
	}

	private NumberVariable isFileCommand() {
		return new NumberVariable(vars[0] instanceof FileVariable ? 1 : 0);
	}

	private NumberVariable isAvailableCommand() {
		return new NumberVariable(((FolderVariable) vars[0]).getVar().exists() ? 1 : 0);
	}

	private Variable randomCommand() throws CommandErrorException {
		if (vars[0].type == VariableType.NUMBER) {
			return new NumberVariable((int) Math.floor(Math.random() * ((NumberVariable) vars[0]).getVar()));
		} else if (vars[0].type == VariableType.ARRAY) {
			ArrayVariable a = ((ArrayVariable) vars[0]);
			try {
				return a.getVar((int) Math.floor(Math.random() * a.getAll().size()));
			} catch (ArrayPositionEmptyException e) {
				throw new CommandErrorException("Array position empty: " + e.getMessage());
			}
		} else if (vars[0].type == VariableType.FOLDER) {
			File[] fl = ((FolderVariable) vars[0]).getVar().listFiles();
			File f = fl[(int) Math.floor(Math.random() * fl.length)];
			return f.isFile() ? new FileVariable(f) : new FolderVariable(f);
		} else if (vars[0].type == VariableType.TEXT) {
			String s = ((TextVariable) vars[0]).getVar();
			return new TextVariable("" + s.charAt((int) Math.floor(Math.random() * s.length())));
		}
		return new NumberVariable(0);
	}

	private void overwriteCommand() throws LogicConversionException {
		Main.overwrite = ((NumberVariable) vars[0]).asBoolean();
	}

	private void caseSensitiveCommand() throws LogicConversionException {
		Main.caseSensitive = ((NumberVariable) vars[0]).asBoolean();
	}

	@Override
	public void explain() {
		// TODO
	}

	@Override
	public Variable result() {
		return output;
	}

	@Override
	public String toString() {
		String s = "";
		for (Node n : input) {
			s += n.toString() + ",";
		}
		s = s.substring(0, s.length() - 1);

		return type.getChar() + "(" + s + ")";
	}
}