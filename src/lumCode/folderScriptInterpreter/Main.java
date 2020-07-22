package lumCode.folderScriptInterpreter;

import java.util.ArrayList;

import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.arithmetic.ArithmeticType;
import lumCode.folderScriptInterpreter.handlers.command.CommandType;
import lumCode.folderScriptInterpreter.handlers.logic.LogicType;
import lumCode.folderScriptInterpreter.variables.Variable;

public class Main {
	public static boolean overwrite = false;
	public static boolean caseSensitive = false;
	public static String script = "";
	public static Variable[] a;
	public static Variable[] i;
	public static final ArrayList<Variable> vars = new ArrayList<>();
	public static final ArrayList<Node> nodes = new ArrayList<>();

	public static void main(String[] args) {
		script = args[0];
		a = new Variable[args.length - 1];
		for (int i = 1; i < args.length; i++) {
			a[i - 1] = Variable.fromString(args[i]);
		}

		breakDownScript();

	}

	private static void breakDownScript() {
		char[] c = script.toCharArray();

		for (int i = 0; i < c.length; i++) {
			if (CommandType.valid(c[i])) {

			} else if (ArithmeticType.valid(c[i])) {

			} else if (LogicType.valid(c[i])) {

			} else if (c[i] == 'i') {

			} else if (c[i] == 'h') {

			} else if (c[i] == '#') {

			} else if (c[i] == 'x') {

			}
		}
	}

	public static Variable lookUpVariable(String name) {
		for (Variable var : vars) {
			if (var.name.equals(name)) {
				return var;
			}
		}
		return null;
	}
}
