package lumCode.folderScriptInterpreter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.method.Method;
import lumCode.folderScriptInterpreter.variables.ArrayVariable;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.TextVariable;
import lumCode.folderScriptInterpreter.variables.Variable;

public class Main {
	public static final Map<Integer, Boolean> options = Options.constructMap();
	public static final File tempDir = new File(
			System.getProperty("java.io.tmpdir") + "FolderScript-" + UUID.randomUUID());
	public static final File logFile = new File(
			System.getProperty("java.io.tmpdir") + "FolderScript-" + System.currentTimeMillis() + ".log");
	public static final DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

	public static final String version = "0.9.2";

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
				Help.display(help.charAt(help.length() - 1));
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
			nodes.addAll(ScriptBuilder.buildProgram(script));

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
