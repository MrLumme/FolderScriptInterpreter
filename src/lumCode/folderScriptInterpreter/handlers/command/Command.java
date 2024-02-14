package lumCode.folderScriptInterpreter.handlers.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.Options;
import lumCode.folderScriptInterpreter.Utilities;
import lumCode.folderScriptInterpreter.exceptions.ArithmeticErrorException;
import lumCode.folderScriptInterpreter.exceptions.CommandErrorException;
import lumCode.folderScriptInterpreter.exceptions.IncorrectParameterAmountException;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.LogicConversionException;
import lumCode.folderScriptInterpreter.exceptions.arrayExceptions.ArrayNotEmptyException;
import lumCode.folderScriptInterpreter.exceptions.arrayExceptions.ArrayPositionEmptyException;
import lumCode.folderScriptInterpreter.exceptions.arrayExceptions.DisallowedDataInArrayException;
import lumCode.folderScriptInterpreter.exceptions.notFoundExceptions.OptionNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.typeExceptions.IncorrectParameterTypeException;
import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedArithmeticTypeException;
import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedCommandTypeException;
import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedVariableTypeException;
import lumCode.folderScriptInterpreter.exceptions.undefinedExceptions.UndefinedArithmeticException;
import lumCode.folderScriptInterpreter.exceptions.undefinedExceptions.UndefinedCommandException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.ResultantNode;
import lumCode.folderScriptInterpreter.handlers.arithmetic.ArithmeticType;
import lumCode.folderScriptInterpreter.handlers.arithmetic.FileArithmeticHandler;
import lumCode.folderScriptInterpreter.handlers.arithmetic.FolderArithmeticHandler;
import lumCode.folderScriptInterpreter.variables.ArrayVariable;
import lumCode.folderScriptInterpreter.variables.BooleanVariable;
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

	public Command(CommandType type, List<Node> input) throws IncorrectParameterAmountException, CommandErrorException {
		this.type = type;
		this.input = input;
		output = null;

		if (type.getInput() != input.size()) {
			throw new IncorrectParameterAmountException(type, input.size());
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
		Variable[] vars = new Variable[input.size()];
		for (int i = 0; i < input.size(); i++) {
			input.get(i).action();
			vars[i] = ((ResultantNode) input.get(i)).result();
		}

		for (int i = 0; i < vars.length; i++) {
			if (vars[i].type == VariableType.FILE) {
				if (type == CommandType.REPLACE || type == CommandType.SUBSTRING || type == CommandType.RANDOM
						|| type == CommandType.SLEEP || type == CommandType.EXIT || type == CommandType.OPTIONS) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				} else if (i == 1 && type == CommandType.LIST) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			} else if (vars[i].type == VariableType.FOLDER) {
				if (type == CommandType.REPLACE || type == CommandType.EXTENSION || type == CommandType.SUBSTRING
						|| type == CommandType.SLEEP || type == CommandType.EXIT || type == CommandType.OPTIONS
						|| type == CommandType.GEN_MD5) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				} else if (i == 0 && (type == CommandType.EXTERNAL)) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				} else if (i == 1 && (type == CommandType.LIST || type == CommandType.WRITE)) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			} else if (vars[i].type == VariableType.TEXT) {
				if (type == CommandType.NAME || type == CommandType.EXTENSION || type == CommandType.PARENT
						|| type == CommandType.IS_FILE || type == CommandType.IS_AVAILABLE || type == CommandType.COPY
						|| type == CommandType.MOVE || type == CommandType.DELETE || type == CommandType.READ
						|| type == CommandType.SLEEP || type == CommandType.EXIT || type == CommandType.OPTIONS) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				} else if (i == 2 && type == CommandType.SUBSTRING) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				} else if (i == 1
						&& (type == CommandType.WRITE || type == CommandType.LIST || type == CommandType.SUBSTRING)) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			} else if (vars[i].type == VariableType.NUMBER) {
				if (type == CommandType.NAME || type == CommandType.EXTENSION || type == CommandType.PARENT
						|| type == CommandType.IS_FILE || type == CommandType.IS_AVAILABLE || type == CommandType.COPY
						|| type == CommandType.MOVE || type == CommandType.DELETE || type == CommandType.READ
						|| type == CommandType.REPLACE) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				} else if (i == 0 && (type == CommandType.EXTERNAL)) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				} else if (i == 1 && (type == CommandType.WRITE)) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			} else if (vars[i].type == VariableType.BOOLEAN) {
				if (type == CommandType.NAME || type == CommandType.EXTENSION || type == CommandType.PARENT
						|| type == CommandType.IS_FILE || type == CommandType.IS_AVAILABLE || type == CommandType.COPY
						|| type == CommandType.MOVE || type == CommandType.DELETE || type == CommandType.READ
						|| type == CommandType.REPLACE || type == CommandType.SUBSTRING) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				} else if (i == 0 && (type == CommandType.EXTERNAL)) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				} else if (i == 1 && (type == CommandType.WRITE)) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			} else if (vars[i].type == VariableType.ARRAY) {
				if (type == CommandType.NAME || type == CommandType.EXTENSION || type == CommandType.PARENT
						|| type == CommandType.IS_FILE || type == CommandType.IS_AVAILABLE || type == CommandType.COPY
						|| type == CommandType.MOVE || type == CommandType.DELETE || type == CommandType.READ
						|| type == CommandType.GEN_MD5 || type == CommandType.REPLACE || type == CommandType.SUBSTRING
						|| type == CommandType.SLEEP || type == CommandType.EXIT || type == CommandType.OPTIONS) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				} else if (i == 0 && (type == CommandType.EXTERNAL)) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				} else if (i == 1 && (type == CommandType.LIST || type == CommandType.WRITE)) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			} else if (vars[i].type == VariableType.SPECIAL) {
				if (type == CommandType.NAME || type == CommandType.EXTENSION || type == CommandType.PARENT
						|| type == CommandType.IS_FILE || type == CommandType.IS_AVAILABLE
						|| type == CommandType.REPLACE || type == CommandType.SUBSTRING || type == CommandType.RANDOM
						|| type == CommandType.SLEEP || type == CommandType.EXIT || type == CommandType.OPTIONS) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				} else if (i == 0 && (type == CommandType.SUBSTRING || type == CommandType.LIST
						|| type == CommandType.WRITE || type == CommandType.DELETE || type == CommandType.COPY
						|| type == CommandType.MOVE || type == CommandType.EXTERNAL)) {
					throw new IncorrectParameterTypeException(type, vars[i]);
				}
			}
		}

		if (type == CommandType.OPTIONS) {
			optionsCommand((NumberVariable) vars[0], (BooleanVariable) vars[1]);
		} else if (type == CommandType.RANDOM) {
			output = randomCommand(vars[0]);
		} else if (type == CommandType.IS_FILE) {
			output = isFileCommand(vars[0]);
		} else if (type == CommandType.IS_AVAILABLE) {
			output = isAvailableCommand((FolderVariable) vars[0]);
		} else if (type == CommandType.NAME) {
			output = nameCommand((FolderVariable) vars[0]);
		} else if (type == CommandType.PARENT) {
			output = parentCommand((FolderVariable) vars[0]);
		} else if (type == CommandType.EXTENSION) {
			output = extensionCommand((FileVariable) vars[0]);
		} else if (type == CommandType.REPLACE) {
			output = replaceCommand((TextVariable) vars[0], (TextVariable) vars[1], (TextVariable) vars[2]);
		} else if (type == CommandType.SUBSTRING) {
			output = substringCommand(vars[0], vars[1], vars[2]);
		} else if (type == CommandType.WRITE) {
			writeCommand(vars[0], vars[1]);
		} else if (type == CommandType.MOVE) {
			output = moveCommand(vars[0], vars[1]);
		} else if (type == CommandType.COPY) {
			output = copyCommand(vars[0], vars[1]);
		} else if (type == CommandType.DELETE) {
			output = deleteCommand(vars[0]);
		} else if (type == CommandType.LIST) {
			output = listCommand(vars[0], vars[1]);
		} else if (type == CommandType.SIZE) {
			output = sizeCommand(vars[0]);
		} else if (type == CommandType.SLEEP) {
			sleepCommmand((NumberVariable) vars[0]);
		} else if (type == CommandType.EXIT) {
			exitCommand((NumberVariable) vars[0]);
		} else if (type == CommandType.READ) {
			output = readCommand(vars[0]);
		} else if (type == CommandType.GEN_MD5) {
			output = genMD5Command(vars[0]);
		} else if (type == CommandType.EXTERNAL) {
			output = externalCommand(vars[0], vars[1]);
		} else {
			throw new UndefinedCommandException(type, vars);
		}
	}

	protected TextVariable genMD5Command(Variable var0) throws CommandErrorException {
		if (var0.type == VariableType.NUMBER) {
			return new TextVariable(Utilities.getMD5("" + ((NumberVariable) var0).getVar()));
		} else if (var0.type == VariableType.FILE) {
			return new TextVariable(Utilities.getMD5(((FileVariable) var0).getVar()));
		} else if (var0.type == VariableType.TEXT) {
			return new TextVariable(Utilities.getMD5("" + ((TextVariable) var0).getVar()));
		} else if (var0.type == VariableType.SPECIAL) {
			return new TextVariable(Utilities.getMD5(Main.script));
		}
		throw new CommandErrorException("Command '" + type.getChar() + "' does not supported variables of type '"
				+ var0.type.name().toLowerCase() + "'.");
	}

	protected Variable readCommand(Variable var0) throws CommandErrorException, DisallowedDataInArrayException {
		if (var0 instanceof SpecialVariable) {
			Scanner sc = new Scanner(System.in);
			while (!sc.hasNext()) {
				// Wait for input
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// Do nothing
				}
			}
			return new TextVariable(sc.nextLine());
		} else {
			File f = ((FileVariable) var0).getVar();
			ArrayVariable o = new ArrayVariable();
			try {
				BufferedReader r = new BufferedReader(new FileReader(f));
				String l = r.readLine();
				while (l != null) {
					o.setNextVar(Variable.fromString(l));
					l = r.readLine();
				}
				r.close();
				return o;
			} catch (FileNotFoundException e) {
				throw new CommandErrorException("File '" + f.getAbsolutePath() + "' could not be found.");
			} catch (IOException e) {
				throw new CommandErrorException("Encountered a reading error on file '" + f.getAbsolutePath() + "'.");
			}
		}
	}

	protected ArrayVariable listCommand(Variable var0, Variable var1)
			throws CommandErrorException, DisallowedDataInArrayException, ArrayNotEmptyException {
		ArrayVariable out = new ArrayVariable();
		if (var0.type == VariableType.NUMBER) {
			if (((NumberVariable) var0).getVar() < 0) {
				if (var1 instanceof SpecialVariable) {
					for (int i = 0; i >= (int) ((NumberVariable) var0).getVar() + 1; i--) {
						out.setNextVar(new NumberVariable(i));
					}
				} else {
					for (int i = (int) ((NumberVariable) var0).getVar(); i < ((NumberVariable) var1).getVar(); i--) {
						out.setNextVar(new NumberVariable(i));
					}
				}
			} else {
				if (var1 instanceof SpecialVariable) {
					for (int i = (int) ((NumberVariable) var0).getVar() - 1; i >= 0; i--) {
						out.setNextVar(new NumberVariable(i));
					}
				} else {
					for (int i = (int) ((NumberVariable) var1).getVar(); i < ((NumberVariable) var0).getVar(); i++) {
						out.setNextVar(new NumberVariable(i));
					}
				}
			}
			return out;
		} else if (var0.type == VariableType.ARRAY) {
			return Utilities.sortArray((ArrayVariable) var0,
					var1 instanceof SpecialVariable ? -1 : (int) ((NumberVariable) var1).getVar());
		} else if (var0.type == VariableType.FOLDER) {
			List<File> list = Utilities.listFolder(((FolderVariable) var0).getVar(),
					var1 instanceof SpecialVariable ? Integer.MAX_VALUE : (int) ((NumberVariable) var1).getVar(),
					Main.getOption(Options.RETURN_FOLDERS));
			for (File f : list) {
				if (Main.getOption(Options.STRICT_ARRAY_DATA) && !Main.getOption(Options.RETURN_FOLDERS)) {
					out.setType(VariableType.FILE);
				}
				try {
					out.setNextVar(f.isFile() ? new FileVariable(f) : new FolderVariable(f));
				} catch (DisallowedDataInArrayException e) {
					// Ignore and do not insert
				}
			}
			return out;
		} else if (var0.type == VariableType.FILE) {
			String[] spl = ((FileVariable) var0).getVar().getAbsolutePath().split(System.getProperty("file.separator"));
			for (String s : spl) {
				out.setNextVar(Variable.fromString(s));
			}
			return out;
		} else if (var0.type == VariableType.TEXT) {
			String str = ((TextVariable) var0).getVar();
			if (var1 instanceof SpecialVariable) {
				for (char c : str.toCharArray()) {
					out.setNextVar(new TextVariable("" + c));
				}
			} else if (((NumberVariable) var1).getVar() > 0) {
				for (int i = (int) ((NumberVariable) var1).getVar(); i < str.length(); i++) {
					out.setNextVar(new TextVariable("" + str.charAt(i)));
				}
			} else {
				for (int i = 0; i < ((NumberVariable) var1).getVar() && i < str.length(); i++) {
					out.setNextVar(new TextVariable("" + str.charAt(i)));
				}
			}
			return out;
		}
		throw new CommandErrorException(
				"Command '" + type.getChar() + "' does not supported variables of the type combination '"
						+ var0.type.name().toLowerCase() + "' and '" + var1.type.name().toLowerCase() + "'.");
	}

	protected NumberVariable sizeCommand(Variable var0) throws UnsupportedVariableTypeException, CommandErrorException {
		try {
			return new NumberVariable(Utilities.varSize(var0));
		} catch (FileNotFoundException e) {
			throw new CommandErrorException("Command failed with message: " + e.getMessage());
		}
	}

	protected void sleepCommmand(NumberVariable var0) {
		try {
			Thread.sleep(var0.getVar());
		} catch (InterruptedException e) {
			// Do nothing
		}
	}

	protected void exitCommand(NumberVariable var0) {
		System.exit((int) var0.getVar());
	}

	protected BooleanVariable deleteCommand(Variable var0) {
		if (var0 instanceof SpecialVariable) {
			var0 = new FolderVariable(Main.tempDir);
		}

		if (Main.getOption(Options.DEBUG)) {
			System.out.println("Delete " + (var0 instanceof FileVariable ? "file" : "folder") + " '"
					+ (((FolderVariable) var0).getVar() == null ? "null"
							: ((FolderVariable) var0).getVar().getAbsolutePath())
					+ "'");
			return new BooleanVariable(true);
		}

		return new BooleanVariable(FileUtils.deleteQuietly(((FolderVariable) var0).getVar()));
	}

	protected BooleanVariable copyCommand(Variable var0, Variable var1)
			throws UnsupportedCommandTypeException, CommandErrorException, UnsupportedArithmeticTypeException,
			UndefinedArithmeticException, ArithmeticErrorException {
		try {
			if (!Main.getOption(Options.OVERWRITE)) {
				if (var1 instanceof SpecialVariable) {
					if (var0 instanceof FileVariable) {
						var1 = FileArithmeticHandler.calculate((FileVariable) var0, ArithmeticType.ADDITION,
								SpecialVariable.getInstance());
					} else {
						var1 = FolderArithmeticHandler.calculate((FolderVariable) var0, ArithmeticType.ADDITION,
								SpecialVariable.getInstance());
					}
				}

				if (var1 instanceof FileVariable) {
					if (var0 instanceof FileVariable) {
						if (((FileVariable) var1).getVar().exists()) {
							return new BooleanVariable(false);
						}
					} else {
						throw new CommandErrorException("Can not copy the folder \"" + var0.toString()
								+ "\" to the file \"" + var0.toString() + "\".");
					}
				} else {
					if (var0 instanceof FileVariable) {
						FileVariable f = ((FileVariable) var0);
						if (new File(((FolderVariable) var1).getPath() + f.getName() + f.getExtension()).exists()) {
							return new BooleanVariable(false);
						}
					} else {
						List<File> l = Utilities.listFolder(((FolderVariable) var0).getVar(), Integer.MAX_VALUE, true);
						String pR = ((FolderVariable) var1).getPath();
						String pO = ((FolderVariable) var0).getParent().getAbsolutePath();
						for (File f : l) {
							if (new File(f.getAbsolutePath().replace(pO, pR)).exists()) {
								return new BooleanVariable(false);
							}
						}
					}
				}
			}

			if (var0 instanceof FileVariable) {
				if (var1 instanceof FileVariable) {
					if (!Main.getOption(Options.DEBUG)) {
						FileUtils.copyFile(((FileVariable) var0).getVar(), ((FileVariable) var1).getVar(), true);
					} else {
						System.out.println("Copy file '" + ((FileVariable) var0).getVar().getAbsolutePath() + "'");
						System.out.println("\tto file '" + ((FileVariable) var1).getVar().getAbsolutePath() + "'");
					}
				} else {
					if (!Main.getOption(Options.DEBUG)) {
						FileUtils.copyFileToDirectory(((FileVariable) var0).getVar(), ((FolderVariable) var1).getVar(),
								true);
					} else {
						System.out.println("Copy file '" + ((FileVariable) var0).getVar().getAbsolutePath() + "'");
						System.out.println("\tto folder '" + ((FolderVariable) var1).getVar().getAbsolutePath() + "'");
					}
				}
			} else {
				if (var1 instanceof FileVariable) {
					throw new CommandErrorException("Can not copy the folder \"" + var0.toString() + "\" to the file \""
							+ var0.toString() + "\".");
				} else {
					if (!Main.getOption(Options.DEBUG)) {
						FileUtils.copyDirectory(((FolderVariable) var0).getVar(), ((FolderVariable) var1).getVar());
					} else {
						System.out.println("Copy folder '" + ((FolderVariable) var0).getVar().getAbsolutePath() + "'");
						System.out.println("\tto folder '" + ((FolderVariable) var1).getVar().getAbsolutePath() + "'");
					}
				}
			}
		} catch (IOException e) {
			throw new CommandErrorException("Can not copy \"" + var0.toString() + "\" to \"" + var0.toString() + "\".");
		}
		return new BooleanVariable(true);
	}

	protected BooleanVariable moveCommand(Variable var0, Variable var1)
			throws UnsupportedCommandTypeException, CommandErrorException, UnsupportedArithmeticTypeException,
			UndefinedArithmeticException, ArithmeticErrorException {
		try {
			if (!Main.getOption(Options.OVERWRITE)) {
				if (var1 instanceof SpecialVariable) {
					if (var0 instanceof FileVariable) {
						var1 = FileArithmeticHandler.calculate((FileVariable) var0, ArithmeticType.ADDITION,
								SpecialVariable.getInstance());
					} else {
						var1 = FolderArithmeticHandler.calculate((FolderVariable) var0, ArithmeticType.ADDITION,
								SpecialVariable.getInstance());
					}
				}

				if (var1 instanceof FileVariable) {
					if (var0 instanceof FileVariable) {
						if (((FileVariable) var1).getVar().exists()) {
							return new BooleanVariable(false);
						}
					} else {
						throw new CommandErrorException("Can not move the folder \"" + var0.toString()
								+ "\" to the file \"" + var0.toString() + "\".");
					}
				} else {
					if (var0 instanceof FileVariable) {
						FileVariable f = ((FileVariable) var0);
						if (new File(((FolderVariable) var1).getPath() + f.getName() + f.getExtension()).exists()) {
							return new BooleanVariable(false);
						}
					} else {
						List<File> l = Utilities.listFolder(((FolderVariable) var0).getVar(), Integer.MAX_VALUE, true);
						String pR = ((FolderVariable) var1).getPath();
						String pO = ((FolderVariable) var0).getParent().getAbsolutePath();
						for (File f : l) {
							if (new File(f.getAbsolutePath().replace(pO, pR)).exists()) {
								return new BooleanVariable(false);
							}
						}
					}
				}
			}

			if (var0 instanceof FileVariable) {
				if (var1 instanceof FileVariable) {
					if (!Main.getOption(Options.DEBUG)) {
						FileUtils.moveFile(((FileVariable) var0).getVar(), ((FileVariable) var1).getVar());
					} else {
						System.out.println("Move file '" + ((FileVariable) var0).getVar().getAbsolutePath() + "'");
						System.out.println("\tto file '" + ((FileVariable) var1).getVar().getAbsolutePath() + "'");
					}
				} else {
					if (!Main.getOption(Options.DEBUG)) {
						FileUtils.moveFileToDirectory(((FileVariable) var0).getVar(), ((FolderVariable) var1).getVar(),
								true);
					} else {
						System.out.println("Move file '" + ((FileVariable) var0).getVar().getAbsolutePath() + "'");
						System.out.println("\tto folder '" + ((FolderVariable) var1).getVar().getAbsolutePath() + "'");
					}
				}
			} else {
				if (var1 instanceof FileVariable) {
					throw new CommandErrorException("Can not move the folder \"" + var0.toString() + "\" to the file \""
							+ var0.toString() + "\".");
				} else {
					if (!Main.getOption(Options.DEBUG)) {
						FileUtils.moveDirectory(((FolderVariable) var0).getVar(), ((FolderVariable) var1).getVar());
					} else {
						System.out.println("Move folder '" + ((FolderVariable) var0).getVar().getAbsolutePath() + "'");
						System.out.println("\tto folder '" + ((FolderVariable) var1).getVar().getAbsolutePath() + "'");
					}
				}
			}
		} catch (IOException e) {
			throw new CommandErrorException("Can not move \"" + var0.toString() + "\" to \"" + var0.toString() + "\".");
		}
		return new BooleanVariable(true);
	}

	protected void writeCommand(Variable var0, Variable var1)
			throws UnsupportedCommandTypeException, CommandErrorException {
		if (var1 instanceof SpecialVariable) {
			if (Main.getOption(Options.WRITE_TO_LOG)) {
				try {
					if (Main.logFile.exists()) {
						Files.write(Paths.get(Main.logFile.getAbsolutePath()), var0.toString().getBytes(),
								StandardOpenOption.APPEND);
					} else {
						Main.logFile.getParentFile().mkdirs();
						Files.write(Paths.get(Main.logFile.getAbsolutePath()), var0.toString().getBytes(),
								StandardOpenOption.CREATE);
					}
				} catch (IOException e) {
					throw new CommandErrorException(
							"Can not print to log \"" + ((FileVariable) var1).getVar().getAbsolutePath() + "\".");
				}
			} else {
				System.out.println(var0.toString());
			}
		} else if (var1 instanceof FileVariable) {
			File f = ((FileVariable) var1).getVar();
			try {
				if (!Main.getOption(Options.DEBUG)) {
					if (f.exists()) {
						Files.write(Paths.get(f.getAbsolutePath()), var0.toString().getBytes(),
								StandardOpenOption.APPEND);
					} else {
						f.getParentFile().mkdirs();
						Files.write(Paths.get(f.getAbsolutePath()), var0.toString().getBytes(),
								StandardOpenOption.CREATE);
					}
				} else {
					System.out.println("Write '" + var0.toString() + "'");
					System.out.println("\tto file '" + f.getAbsolutePath() + "'");
				}
			} catch (IOException e) {
				throw new CommandErrorException(
						"Can not print to file \"" + ((FileVariable) var1).getVar().getAbsolutePath() + "\".");
			}
		} else {
			throw new CommandErrorException(
					"Print location \"" + ((FileVariable) var1).getVar().getAbsolutePath() + "\" is invalid.");
		}
	}

	protected Variable replaceCommand(TextVariable var0, TextVariable var1, TextVariable var2) {
		String str;
		if (var1.isRegex()) {
			str = var0.getVar().replaceAll(var1.getVar(), var2.getVar());
		} else {
			str = var0.getVar().replace(var1.getVar(), var2.getVar());
		}
		return Variable.fromString(str);
	}

	protected Variable substringCommand(Variable var0, Variable var1, Variable var2)
			throws UnsupportedCommandTypeException, CommandErrorException {
		String base = var0.toString();
		int start = 0;
		int end = base.length();

		if (var1 instanceof NumberVariable) {
			start = (int) ((NumberVariable) var1).getVar();
		} else if (!(var1 instanceof SpecialVariable)) {
			start = base.indexOf(((TextVariable) var1).getVar()) - ((TextVariable) var1).getVar().length();
		}
		if (var2 instanceof NumberVariable) {
			end = (int) ((NumberVariable) var2).getVar();
		} else if (!(var2 instanceof SpecialVariable)) {
			end = base.indexOf(((TextVariable) var2).getVar()) - ((TextVariable) var2).getVar().length();
		}

		try {
			return Variable.fromString(base.substring(start, end));
		} catch (StringIndexOutOfBoundsException e) {
			throw new CommandErrorException(
					"Can not get a substring of \"" + base + "\" taking from \"" + start + "\" to \"" + end + "\"");
		}
	}

	protected TextVariable nameCommand(FolderVariable var0) {
		return new TextVariable(var0.getName());
	}

	protected FolderVariable parentCommand(FolderVariable var0) {
		return new FolderVariable(var0.getParent());
	}

	protected TextVariable extensionCommand(FileVariable var0) {
		return new TextVariable(var0.getExtension());
	}

	protected NumberVariable isFileCommand(Variable var0) {
		return new BooleanVariable(var0 instanceof FileVariable);
	}

	protected NumberVariable isAvailableCommand(FolderVariable var0) {
		return new BooleanVariable(var0.getVar().exists());
	}

	protected Variable randomCommand(Variable var0) throws CommandErrorException, ArrayPositionEmptyException {
		Random r = new Random();
		if (var0.type == VariableType.NUMBER) {
			try {
				int val = (int) ((NumberVariable) var0).getVar();
				return new NumberVariable((val / Math.abs(val)) * r.nextInt(Math.abs(val)));
			} catch (ArithmeticException e) {
				return new NumberVariable(0);
			}
		} else if (var0.type == VariableType.ARRAY) {
			Variable[] a = ((ArrayVariable) var0).getAll().values().toArray(new Variable[0]);
			return a[r.nextInt(a.length)];
		} else if (var0.type == VariableType.FOLDER) {
			File[] fl = ((FolderVariable) var0).getVar().listFiles();
			File f = fl[r.nextInt(fl.length)];
			return f.isFile() ? new FileVariable(f) : new FolderVariable(f);
		} else if (var0.type == VariableType.TEXT) {
			String s = ((TextVariable) var0).getVar();
			return new TextVariable("" + s.charAt(r.nextInt(s.length())));
		}
		throw new CommandErrorException(
				"Command 'q' does not support input of type '" + var0.type.name().toLowerCase() + "'.");
	}

	protected void optionsCommand(NumberVariable var0, BooleanVariable var1)
			throws LogicConversionException, OptionNotFoundException {
		if (Options.isValid((int) var0.getVar())) {
			Main.options.put((int) var0.getVar(), var1.asBoolean());
		} else {
			throw new OptionNotFoundException((int) var0.getVar());
		}
	}

	protected NumberVariable externalCommand(Variable var0, Variable var1) throws CommandErrorException {
		ArrayList<String> args = new ArrayList<String>();
		args.add(var0.toString());

		args.addAll(Utilities.cleanArguments(var1));

//		if (Main.getOption(Options.IMPORT_VARIABLES)) {
//			args.add("-key");
//			args.add(UUID.randomUUID().toString());
//		}

		ProcessBuilder pb = new ProcessBuilder(args);

		if (Main.getOption(Options.OUTPUT_EXTERNAL_LOG)) {
			pb.redirectOutput(Redirect.PIPE);
		} else {
			pb.redirectOutput(Redirect.DISCARD);
		}

		try {
			Process p = pb.start();
			while (p.isAlive()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// Do nothing
				}
			}
			return new NumberVariable(p.exitValue());

		} catch (IOException e) {
			throw new CommandErrorException("Command 'k' failed with the following message: " + e.getMessage());
		}
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