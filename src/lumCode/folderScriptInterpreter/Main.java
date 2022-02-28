package lumCode.folderScriptInterpreter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.arrayExceptions.ArrayPositionEmptyException;
import lumCode.folderScriptInterpreter.exceptions.arrayExceptions.DisallowedDataInArrayException;
import lumCode.folderScriptInterpreter.exceptions.arrayExceptions.NotArrayException;
import lumCode.folderScriptInterpreter.exceptions.nameNotFoundExceptions.ArgumentNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.nameNotFoundExceptions.IteratorNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.nameNotFoundExceptions.VariableNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedTypeException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.arithmetic.ArithmeticType;
import lumCode.folderScriptInterpreter.handlers.command.CommandType;
import lumCode.folderScriptInterpreter.handlers.logic.LogicType;
import lumCode.folderScriptInterpreter.variables.ArrayVariable;
import lumCode.folderScriptInterpreter.variables.Variable;

public class Main {
	public static final Map<Integer, Boolean> options = Options.constructMap();
	public static final File tempDir = new File(
			System.getProperty("java.io.tmpdir") + "FolderScript-" + UUID.randomUUID());

	public static String script = "";

	public static final HashMap<Integer, Variable> a = new HashMap<Integer, Variable>();
	public static final HashMap<Integer, Variable> i = new HashMap<Integer, Variable>();
	public static final HashMap<String, Variable> v = new HashMap<String, Variable>();
	public static final List<Node> nodes = new ArrayList<>();

	public static void main(String[] args) throws InterpreterException {
		script = "";

		a.clear();
		i.clear();
		v.clear();

		nodes.clear();

		if (args.length == 0) {
			System.out.println("No script. Exiting program.");
		} else if (args[0].toLowerCase().startsWith("h")) {
			// Detailed help
			String help = args[0].replaceAll("[\\r\\n\\t\\f\\v ]", "").toLowerCase();
			if (help.length() < 3) {
				detailedHelp(help.charAt(help.length() - 1));
			}
		} else {

			for (int i = 1; i < args.length; i++) {
				a.put(i - 1, Variable.fromString(args[i]));
			}

			if (new File(args[0]).exists()) {
				try {
					script = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8);
				} catch (IOException e) {
					System.out.println("Could not load script file.");
					System.exit(2);
				}
			} else {
				script = args[0];
			}
			script = Utilities.cleanAndValidateScript(script);

			// Construct node tree
			nodes.addAll(ScriptBuilder.buildNodeTree(script));

			// Execute program
			for (Node n : nodes) {
				n.action();
			}

			// Delete temp folder
			FileUtils.deleteQuietly(tempDir);
		}
	}

	private static void detailedHelp(char c) throws UnsupportedTypeException {
		if (c == 'a') {
			// Argument
			System.out.println("~ Arguments ~");
			System.out.println("Syntax:\tfx:");
			System.out.println("a[num]\ta[0]");
			System.out.println();
			System.out.println("Read-only array given to the script upon execution start.");
			System.out.println(
					"For example if you run the script \"w(a[0],$)\" and it would print the first argument given to the script.");
		} else if (c == '#') {
			// Variables
			System.out.println("~ Variables ~");
			System.out.println("Syntax:\t\tfx:");
			System.out.println("#var\t\t#name, #saved_number");
			System.out.println("#var[num]\t#array[0], #list[54]");
			System.out.println();
			System.out.println("Used to store and read values while a script is executing.");
			System.out.println(
					"Note that only alphanumeric characters and underscore ('_') can be used in variable names.");
			System.out.println("Declaring a value to variable is done like any of the following:");
			System.out.println("\t#var = 54");
			System.out.println("\t#var[0] = \"Hallo World!\"");
			System.out.println("\t#var = C:\folder\file1.txt");
			System.out.println("\t#var = l(a[0],$)");
			System.out.println("\t#var ! f(a[0])");
			System.out.println();
			System.out.println(
					"When declaring a variable you can either set it equal to a given value, or negate it using '!' instead of '='.");
			System.out.println(
					"This can only be done in cases where the value is true ('1') or false ('0'), in which case it will store the opposate of the value.");
		} else if (c == '?') {
			// Conditional
			System.out.println("~ Conditional ~");
			System.out.println("Syntax:\t\tfx:");
			System.out.println("?(res){prg}\t?(n(a[0])=\"filename\"){w(\"It matches!\",$)}");
			System.out.println(
					"?(res){prg:prg}\t?(n(a[0])=\"filename\"){w(\"It matches!\",$):w(\"It does not match!\",$)}");
			System.out.println();
			System.out.println("Used to run specific program code depending on whether a given result is true or not.");
			System.out.println(
					"If the conditional separator (':') is present, the code before it is run if the result is true, otherwise the code after it is run.");
		} else if (c == 'i') {
			// Iteration
			System.out.println("~ Iteration ~");
			System.out.println("Syntax:\t\tfx:");
			System.out.println("inum(res){prg}\ti0(50){w(\"Number is: \"+i0,$)}");
			System.out.println();
			System.out.println(
					"Runs a loop of the given program code using a result giving code as the iterant. The iteration name (fx: i0) can be called in the program code to get the current iteration value.");
			System.out.println("All data types except files can be used as iterants. Fx:");
			System.out.println("\ti0(13) would start at 0 and iterate up to 13.");
			System.out.println(
					"\ti0(\"This is text\") would start at the first charater 'T' and iterate though each, ending with the character 't'.");
			System.out.println(
					"\ti0(C:/documents) would list the files in the folder 'C:/documents' and iterate though them.");
			System.out.println("\ti0(#array) would iterate through all the variables in the array.");
			System.out.println(
					"\ti0($) would start at 0 and iterate until stopped by a break ('b') command in the program code ");
		} else if (c == 'b') {
			// Break
			System.out.println("~ Break ~");
			System.out.println("Syntax:\tfx:");
			System.out.println("b\ti0(50){?(i0=20){b}}");
			System.out.println();
			System.out.println("Used to manuelly break out of an iteration.");
		} else if (c == 'h') {
			// Help
			System.out.println("~ Help ~");
			System.out.println("Syntax:\tfx:");
			System.out.println("h com\th a");
			System.out.println();
			System.out.println(
					"If put in the start with only one other character, it will return detailed information about the give character command.");
		} else if (c == 't') {
			// Test
			System.out.println("~ Test ~");
			System.out.println("Syntax:\tfx:");
			System.out.println("t{prg}\t#clean=t{c(#from[0],#to[0])}");
			System.out.println();
			System.out.println(
					"Attempts to execute a piece of program code and returns if it was completed ('1') or errored ('0')");
		} else if (CommandType.valid(c)) {
			// Command
			switch (CommandType.fromChar(c)) {
			case COPY:
				System.out.println("~ Copy ~");
				System.out.println("Syntax:\t\tfx:");
				System.out.println("c(fil/fol, fil/fol)\tc(#from, #to)");
				System.out.println();
				System.out.println("Copies one file or folder to another file or folder.");
				System.out.println("Is affected by the options '" + Options.OVERWRITE.toString() + "' ("
						+ Options.OVERWRITE.getId() + ") and '" + Options.DEBUG.toString() + "' ("
						+ Options.DEBUG.getId() + ").");
				break;
			case EXTENSION:
				System.out.println("~ Extension ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("e(fil)\t#ext=e(#file)");
				System.out.println();
				System.out.println("Returns the extension of the given file.");
				System.out.println(
						"Also returns the initial '.' such that a code piece like 'p(#file) + n(#file) + e(#file)' gives the full path.");
				break;
			case MOVE:
				System.out.println("~ Move ~");
				System.out.println("Syntax:\t\tfx:");
				System.out.println("m(fil/fol, fil/fol)\tm(#from, #to)");
				System.out.println();
				System.out.println("Moves one file or folder to another file or folder.");
				System.out.println("Is affected by the options '" + Options.OVERWRITE.toString() + "' ("
						+ Options.OVERWRITE.getId() + ") and '" + Options.DEBUG.toString() + "' ("
						+ Options.DEBUG.getId() + ").");
				break;
			case NAME:
				System.out.println("~ Name ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("n(fil/fol)\t#name=n(#file)");
				System.out.println();
				System.out.println("Returns the name of the given file without its extension.");
				break;
			case PARENT:
				System.out.println("~ Parent ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("p(fil/fol)\t#top=p(#file)");
				System.out.println();
				System.out.println("Returns the containing folder of the given file or folder.");
				break;
			case RANDOM:
				System.out.println("~ Random ~");
				System.out.println("Syntax:\t\tfx:");
				System.out.println("q(txt/fol/num/arr)\tq(20), q(\"Text\")");
				System.out.println();
				System.out.println("Returns a random value determined by the given variable.");
				System.out.println("For numbers, it returns a random value between the given number and zero.");
				System.out.println("For arrays, it returns a random entry contained in the array.");
				System.out.println("For text, it returns a random character in the text.");
				System.out.println("For folders, it returns a random object inside it.");
				break;
			case READ:
				System.out.println("~ Read ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("r(fil)\t#contents=r(file.txt)");
				System.out.println();
				System.out.println("Returns the content of a given file.");
				break;
			case SIZE:
				System.out.println("~ Size ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("s(var)\ts(#array), s(-10), s(\"Text\"), s(file.txt)");
				System.out.println();
				System.out.println("Returns the size of a given variable.");
				System.out.println("For numbers, it returns the absolute value.");
				System.out.println("For arrays, it returns the amount of entries in the array.");
				System.out.println("For text, it returns the length of the text.");
				System.out.println("For folders, it returns the amount of objects inside it.");
				System.out.println("For files, it returns the file size.");
				System.out.println("For special, it returns the character count of the executing script.");
				break;
			case SLEEP:
				System.out.println("~ Sleep ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("s(num)\ts(1000)");
				System.out.println();
				System.out.println("Puts the program to sleep for a given amount of milliseconds.");
				break;
			case WRITE:
				System.out.println("~ Write ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("w(txt, fil/spe)\tw(\"Text\", file.txt)");
				System.out.println();
				System.out.println("Writes a piece of text into a file or to the console if '$' is given.");
				System.out.println("If the output file already existed, the text will be appended to it.");
				System.out.println("Is affected by the option '" + Options.DEBUG.toString() + "' ("
						+ Options.DEBUG.getId() + ") which forces it to write to the console.");
				break;

			}
		} else if (LogicType.valid(c)) {
			// Logic
		} else if (ArithmeticType.valid(c)) {
			// Arithmetic
		}
	}

	public static Variable lookUpVariable(String name) throws VariableNotFoundException, ArrayPositionEmptyException {
		if (name.contains("[")) {
			int numb = Integer.parseInt(name.substring(name.indexOf('[') + 1, name.indexOf(']')));
			Variable var = v.get(name.substring(0, name.indexOf('[')));
			if (var != null && var instanceof ArrayVariable) {
				return ((ArrayVariable) var).getVar(numb);
			}
		} else {
			Variable var = v.get(name);
			if (var != null) {
				return var;
			}
		}
		throw new VariableNotFoundException(name);
	}

	public static Variable lookUpArgument(String name) throws ArgumentNotFoundException {
		int n = Integer.parseInt(name.split("\\[|\\]")[1]);
		Variable var = a.get(n);
		if (var != null) {
			return var;
		}
		throw new ArgumentNotFoundException(n);
	}

	public static Variable lookUpIterator(String name) throws IteratorNotFoundException {
		int n = Integer.parseInt(name.substring(1));
		Variable var = i.get(n);
		if (var != null) {
			return var;
		}
		throw new IteratorNotFoundException(n);
	}

	public static void setVariable(String name, Variable value)
			throws NotArrayException, DisallowedDataInArrayException {
		if (name.contains("[")) {
			int number = Integer.parseInt(name.substring(name.indexOf('[') + 1, name.indexOf(']')));
			name = name.substring(0, name.indexOf('['));
			Variable arr = v.get(name);
			if (arr != null && !(arr instanceof ArrayVariable)) {
				throw new NotArrayException(name);
			}
			if (arr == null) {
				arr = new ArrayVariable();
				v.put(name, arr);
			}
			((ArrayVariable) arr).setVar(number, value);
		} else {
			v.put(name, value);
		}
	}

	public static boolean getOption(Options option) {
		return options.get(option.getId());
	}
}
