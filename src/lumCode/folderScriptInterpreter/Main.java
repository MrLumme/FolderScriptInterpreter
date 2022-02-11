package lumCode.folderScriptInterpreter;

import java.io.File;
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
import lumCode.folderScriptInterpreter.variables.ArrayVariable;
import lumCode.folderScriptInterpreter.variables.Variable;

public class Main {
	public static boolean overwrite = false;
	public static boolean caseSensitive = false;
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
		}

		script = args[0];
		for (int i = 1; i < args.length; i++) {
			a.put(i - 1, Variable.fromString(args[i]));
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
