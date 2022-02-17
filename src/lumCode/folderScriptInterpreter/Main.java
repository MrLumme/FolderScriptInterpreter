package lumCode.folderScriptInterpreter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.arrayExceptions.ArrayPositionEmptyException;
import lumCode.folderScriptInterpreter.exceptions.arrayExceptions.NotArrayException;
import lumCode.folderScriptInterpreter.exceptions.nameNotFoundExceptions.ArgumentNameNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.nameNotFoundExceptions.IteratorNameNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.nameNotFoundExceptions.VariableNameNotFoundException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.arithmetic.ArithmeticType;
import lumCode.folderScriptInterpreter.handlers.command.CommandType;
import lumCode.folderScriptInterpreter.handlers.logic.LogicType;
import lumCode.folderScriptInterpreter.variables.ArrayVariable;
import lumCode.folderScriptInterpreter.variables.Variable;

public class Main {
	public static boolean overwrite = false;
	public static boolean caseSensitive = false;
	public static boolean helpMode = false;
	public static File tempDir = new File(System.getProperty("java.io.tmpdir") + "FolderScript-" + UUID.randomUUID());
	public static String script = "";
	public static final HashMap<Integer, Variable> a = new HashMap<Integer, Variable>();
	public static final HashMap<Integer, Variable> i = new HashMap<Integer, Variable>();
	public static final HashMap<String, Variable> v = new HashMap<String, Variable>();
	public static final List<Node> nodes = new ArrayList<>();

	public static void main(String[] args) throws InterpreterException {
		if (args.length == 0) {
			System.out.println("No script. Exiting program.");
			System.exit(1);
		} else if (args[0].toLowerCase().startsWith("h")) {
			// Detailed help
			String help = args[0].replaceAll("[\\r\\n\\t\\f\\v ]", "").toLowerCase();
			if (help.length() == 2) {
				detailedHelp(help.charAt(1));
			}
		}

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

	private static void detailedHelp(char c) {
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
			System.out.println("Syntax:\tfx:");
			System.out.println("#var\t#name, #saved_number");
			System.out.println("#var[num]\t#array[0], #list[54]");
			System.out.println();
			System.out.println("Used to store and read values while a script is executing.");
			System.out.println(
					"Note that only alphanumeric characters and underscore ('_') can be used in variable names.");
			System.out.println("Declaring a value to variable is done like any of the following:");
			System.out.println();
			System.out.println("#var = 54");
			System.out.println("#var[0] = \"Hallo World!\"");
			System.out.println("#var = C:\folder\file1.txt");
			System.out.println("#var = l(a[0],$)");
			System.out.println("#var ! f(a[0])");
			System.out.println();
			System.out.println(
					"When declaring a variable you can either set it equal to a given value, or negate it using '!' instead of '='.");
			System.out.println(
					"This can only be done in cases where the value is true ('1') or false ('0'), in which case it will store the opposate of the value.");
		} else if (c == '?') {
			// Conditional
			System.out.println("~ Conditional ~");
			System.out.println("Syntax:\tfx:");
			System.out.println("?(res){prg}\t?(n(a[0])=\"filename\"){w(\"It matches!\",$)}");
			System.out.println(
					"?(res){prg:prg}\t?(n(a[0])=\"filename\"){w(\"It matches!\",$):w(\"It does not match!\",$)}");
			System.out.println();
			System.out.println("Used to run specific program code depending on whether a given result is true or not.");
			System.out.println(
					"If the conditional separator (':') is present, the code before it is run if the result is true, otherwise the code after it is run.");
		} else if (c == 'i') {
			// Iteration
		} else if (c == 'b') {
			// Break
		} else if (c == 'h') {
			// Help
			System.out.println("~ Help ~");
			System.out.println("Syntax:\tfx:");
			System.out.println("h com\th a");
			System.out.println("h(boo)\th(1),i0(50){w(i0,a[0])}");
			System.out.println();
			System.out.println(
					"If put in the start with only one other character, it will return detailed information about the give character command.");
			System.out.println(
					"If put as part of a script it will toggle help mode on ('1') and off ('0'). Help mode prevents the script from moving, copying, deleting or outputting files or folders.");
		} else if (c == 't') {
			// Test
		} else if (CommandType.valid(c)) {
			// Command
		} else if (LogicType.valid(c)) {
			// Logic
		} else if (ArithmeticType.valid(c)) {
			// Arithmetic
		}
	}

	public static Variable lookUpVariable(String name)
			throws VariableNameNotFoundException, ArrayPositionEmptyException {
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
		throw new VariableNameNotFoundException(name);
	}

	public static Variable lookUpArgument(String name) throws ArgumentNameNotFoundException {
		int n = Integer.parseInt(name.split("\\[|\\]")[1]);
		Variable var = a.get(n);
		if (var != null) {
			return var;
		}
		throw new ArgumentNameNotFoundException(n);
	}

	public static Variable lookUpIterator(String name) throws IteratorNameNotFoundException {
		int n = Integer.parseInt(name.substring(1));
		Variable var = i.get(n);
		if (var != null) {
			return var;
		}
		throw new IteratorNameNotFoundException(n);
	}

	public static void setVariable(String name, Variable value) throws NotArrayException {
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
}
