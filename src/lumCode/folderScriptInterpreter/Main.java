package lumCode.folderScriptInterpreter;

import java.util.ArrayList;

import lumCode.folderScriptInterpreter.variables.Variable;

public class Main {
	public static ArrayList<Variable> vars = new ArrayList<>();
	public static boolean overwrite = false;

	public static String script = "";
	public static Variable[] a;

	public static void main(String[] args) {
		script = args[0];
		a = new Variable[args.length - 1];
		for (int i = 1; i < args.length; i++) {
			a[i - 1] = Variable.fromString(args[i]);
		}
	}

	public static Variable lookUpArgument(String name) {
		for (Variable var : vars) {
			if (var.name.equals(name)) {
				return var;
			}
		}
		return null;
	}
}
