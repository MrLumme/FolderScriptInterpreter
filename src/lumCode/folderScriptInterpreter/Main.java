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
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.arrayExceptions.ArrayPositionEmptyException;
import lumCode.folderScriptInterpreter.exceptions.arrayExceptions.DisallowedDataInArrayException;
import lumCode.folderScriptInterpreter.exceptions.arrayExceptions.NotArrayException;
import lumCode.folderScriptInterpreter.exceptions.notFoundExceptions.ArgumentNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.notFoundExceptions.IteratorNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.notFoundExceptions.MethodNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.notFoundExceptions.VariableNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedTypeException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.arithmetic.ArithmeticType;
import lumCode.folderScriptInterpreter.handlers.command.CommandType;
import lumCode.folderScriptInterpreter.handlers.logic.LogicType;
import lumCode.folderScriptInterpreter.handlers.method.Method;
import lumCode.folderScriptInterpreter.variables.ArrayVariable;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.TextVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.lookUps.EnvironmentType;

public class Main {
	public static final Map<Integer, Boolean> options = Options.constructMap();
	public static final File tempDir = new File(
			System.getProperty("java.io.tmpdir") + "FolderScript-" + UUID.randomUUID());
	public static final File logFile = new File(
			System.getProperty("java.io.tmpdir") + "FolderScript-" + System.currentTimeMillis() + ".log");
	public static final String version = "0.9.1";

	public static UUID key = null;
	public static String script = "";

	public static final HashMap<Integer, Variable> a = new HashMap<Integer, Variable>();
	public static final HashMap<Integer, Variable> i = new HashMap<Integer, Variable>();
	public static final HashMap<String, Variable> v = new HashMap<String, Variable>();
	public static final HashMap<String, Method> m = new HashMap<String, Method>();
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
				if (args[i].equals("-key") && args.length > i && key == null) {
					key = UUID.fromString(args[i + 1]);
				} else {
					Variable var = Variable.fromString(args[i]);
					if (var instanceof TextVariable) {
						((TextVariable) var).setVar(Utilities.collapseEscapeCharacters(((TextVariable) var).getVar()));
					}
					a.put(i - (key == null ? 1 : 3), var);
				}
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

			// Construct methods
			List<String> methodScripts = Utilities.extractMethods(script);
			for (String method : methodScripts) {
				script = script.replace(method, "");
				Method met = ScriptBuilder.buildMethod(method);
				m.put(met.getName(), met);
			}

			// Construct node tree
			nodes.addAll(ScriptBuilder.buildNodeTree(script));

			// Execute program
			for (Node n : nodes) {
				n.action();
			}

			if (key != null) {
				System.out.println("KEY:" + key.toString());
				for (Entry<String, Variable> var : v.entrySet()) {
					System.out.println("NAME:" + var.getKey() + "\tTYPE:" + var.getValue().type.name() + "\tVALUE:"
							+ var.getValue().toString());
				}
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
		} else if (c == '.') {
			// Break
			System.out.println("~ Environment Variable ~");
			System.out.println("Syntax:\tfx:");
			System.out.println(".num\t#dir=.0 w(.4,$)");
			System.out.println();
			System.out.println("Returns an environment variable depending on the given id number, which are:");
			for (EnvironmentType t : EnvironmentType.values()) {
				System.out.println(t.getId() + ": " + t.toString());
			}
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
				System.out.println("Returns '1' if successful, '0' otherwise.");
				System.out.println("Is affected by the options '" + Options.OVERWRITE.toString() + "' ("
						+ Options.OVERWRITE.getId() + ") and '" + Options.DEBUG.toString() + "' ("
						+ Options.DEBUG.getId() + ").");
				break;
			case DELETE:
				System.out.println("~ Delete ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("d(fil/fol)\td(C:/ny mappe)");
				System.out.println();
				System.out.println("Deletes a given file or folder.");
				System.out.println("Returns '1' if successful, '0' otherwise.");
				System.out.println("Is affected by the option '" + Options.DEBUG.toString() + "' ("
						+ Options.DEBUG.getId() + ").");
				break;
			case EXIT:
				System.out.println("~ Exit ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("x(num)\tx(0)");
				System.out.println();
				System.out.println("Ends the script prematurely, using the given number as the exit code.");
				System.out.println("'0' should be used as the exit code for all normal exit call.");
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
			case GEN_MD5:
				System.out.println("~ Generate MD5 ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("g(num/txt/fil/spe)\t#md5=g(#file)");
				System.out.println();
				System.out.println(
						"Generates and returns a text format version of an MD5 checksum from the given variable.");
				break;
			case IS_AVAILABLE:
				System.out.println("~ Is Available ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("v(fil/fol)\tv(#folder)");
				System.out.println();
				System.out.println("Returns '1' if the given file or folder exists, '0' otherwise.");
				break;
			case IS_FILE:
				System.out.println("~ Is File ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("f(fil/fol)\tf(#item)");
				System.out.println();
				System.out.println("Returns '1' if the given variable is a file, '0' otherwise.");
				break;
			case LIST:
				System.out.println("~ List ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("l(fil/fol/txt/num/arr,num/spe)\t#list=l(\"C:/folder\",0)");
				System.out.println();
				System.out.println("Returns a list from a given variable.");
				System.out.println(
						"For files, it returns a list of the full file path splitted. Lists through the given amount of subdirectories down or all if '$' is given.");
				System.out.println(
						"For folders, it returns a list of contained files. Lists through the given amount of subdirectories down or all if '$' is given.");
				System.out.println(
						"For text, it returns a list of characters in a text. The second argument sets where to start (if the number is positive) or where to end (if the number is negative).");
				System.out.println(
						"For numbers, it returns a list of numbers between the two given values. If '$' is given, it acts as a '0'.");
				System.out.println(
						"For arrays, it returns a sorted list of items in the array. The second argument defines how to sort the list with '0' as size and type of item, '1' as alphanumeric on the value, and '-1' reverses the current sorting.");
				System.out.println("Is affected by the options '" + Options.RETURN_FOLDERS.toString() + "' ("
						+ Options.RETURN_FOLDERS.getId() + ").");
				break;
			case MOVE:
				System.out.println("~ Move ~");
				System.out.println("Syntax:\t\tfx:");
				System.out.println("m(fil/fol, fil/fol)\tm(#from, #to)");
				System.out.println();
				System.out.println("Moves one file or folder to another file or folder.");
				System.out.println("Returns '1' if successful, '0' otherwise.");
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
			case OPTIONS:
				System.out.println("~ Options ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("o(num, boo)\to(0,1)");
				System.out.println();
				System.out.println("Set a given option by its id to either true ('1') or false ('0').");
				System.out.println("Options:");
				for (Options o : Options.values()) {
					System.out.println(o.getId() + ": " + o.toString());
				}
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
			case REPLACE:
				System.out.println("~ Replace ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("y(txt,txt,txt)\t#result=y(\"Hallo World!\",\"World\",\"User\")");
				System.out.println();
				System.out.println(
						"Returns a copy of a given text where a given part is replace with another given part.");
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
			case SUBSTRING:
				System.out.println("~ Substring ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("u(txt,num,num)\t#sub=u(\"Text\",1,4)");
				System.out.println();
				System.out.println(
						"Returns a substring of a given text from a given start (inclusive) to a given end (exclusive).");
				System.out.println(
						"Note that text starts at position '0' so the 'text' would be indexed as so: t = 0, e = 1, x = 2, t = 3");
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
			switch (LogicType.fromChar(c)) {
			case AND:
				System.out.println("~ And ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("&\t?(#count=30&#val=1){w(\"Ding!\",$)}");
				System.out.println();
				System.out.println("Returns true ('1') if both sides are true ('1'), otherwise returns false ('0').");
				break;
			case EQUAL:
				System.out.println("~ Equal ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("=\t?(#count=30){w(\"Ding!\",$)}");
				System.out.println();
				System.out.println(
						"Returns true ('1') if the value of both sides are the same, otherwise returns false ('0').");
				break;
			case GREATER:
				System.out.println("~ Greater Than ~");
				System.out.println("Syntax:\tfx:");
				System.out.println(">\t?(#count>30){w(\"Ding!\",$)}");
				System.out.println();
				System.out.println(
						"Returns true ('1') if the value of the left side is greater than the right side, otherwise returns false ('0').");
				break;
			case LESS:
				System.out.println("~ Less Than ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("<\t?(#count<30){w(\"Ding!\",$)}");
				System.out.println();
				System.out.println(
						"Returns true ('1') if the value of the left side is less than the right side, otherwise returns false ('0').");
				break;
			case NOT:
				System.out.println("~ Not ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("!\t?(#count!30){w(\"Ding!\",$)}");
				System.out.println();
				System.out.println(
						"Returns true ('1') if the value of both sides are not the same, otherwise returns false ('0').");
				break;
			case OR:
				System.out.println("~ Or ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("|\t?(#count=30|#val=1){w(\"Ding!\",$)}");
				System.out.println();
				System.out.println("Returns true ('1') if either side are true ('1'), otherwise returns false ('0').");
				break;
			}
		} else if (ArithmeticType.valid(c)) {
			// Arithmetic
			switch (ArithmeticType.fromChar(c)) {
			case ADDITION:
				System.out.println("~ Addition ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("+\t#count=30+50");
				System.out.println();
				System.out.println("Returns the value of both sides added together.");
				break;
			case DIVISION:
				System.out.println("~ Divison ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("/\t#count=30/50");
				System.out.println();
				System.out.println("Returns the value of the left side divided by the right side.");
				break;
			case MODULO:
				System.out.println("~ Modulo ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("%\t#count=30%50");
				System.out.println();
				System.out.println("Returns the remainder of the left side divided by the right side.");
				break;
			case MULTIPLICATION:
				System.out.println("~ Multiplication ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("*\t#count=30*50");
				System.out.println();
				System.out.println("Returns the value of both sides multiplied together.");
				break;
			case SUBTRACTION:
				System.out.println("~ Subtraction ~");
				System.out.println("Syntax:\tfx:");
				System.out.println("-\t#count=30-50");
				System.out.println();
				System.out.println("Returns the value of the right side subtracted from the left side.");
				break;
			}
		}
	}

	public static Variable lookUpVariable(String name) throws VariableNotFoundException {
		if (name.contains("[")) {
			name = name.substring(0, name.indexOf('['));
		}
		Variable var = v.get(name);
		if (var != null) {
			return var;
		}
		throw new VariableNotFoundException(name);
	}

	public static Variable lookUpVariable(String name, int numb)
			throws VariableNotFoundException, ArrayPositionEmptyException {
		Variable var = lookUpVariable(name);
		if (var != null && var instanceof ArrayVariable) {
			return ((ArrayVariable) var).getVar(numb);
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

	public static Method lookUpMethod(String name) throws MethodNotFoundException {
		Method var = m.get(name);
		if (var != null) {
			return var;
		}
		throw new MethodNotFoundException(name);
	}

	public static void setVariable(String name, Variable value) {
		v.put(name, value);
	}

	public static void setVariable(String name, NumberVariable number, Variable value)
			throws NotArrayException, DisallowedDataInArrayException {
		Variable arr = v.get(name);
		if (arr != null && !(arr instanceof ArrayVariable)) {
			throw new NotArrayException(name);
		}
		if (arr == null) {
			arr = new ArrayVariable();
			v.put(name, arr);
		}
		((ArrayVariable) arr).setVar((int) number.getVar(), value);
	}

	public static boolean getOption(Options option) {
		return options.get(option.getId());
	}
}
