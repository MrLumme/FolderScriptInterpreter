package lumCode.folderScriptInterpreter.variables;

import java.io.File;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.ArgumentNameNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.IteratorNameNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.VariableNameNotFoundException;

public class Variable {
	public final VariableType type;
	public final String name;

	protected Variable(VariableType type, String name) {
		this.type = type;
		this.name = name;
	}

	protected Variable(VariableType type) {
		this(type, null);
	}

	public static Variable fromString(String var) {
		if (var.matches("^-{0,1}[0-9]{1,}$")) {
			return new IntVariable(Integer.parseInt(var));
		} else if (var.matches("^[A-Za-z]{1}:(\\\\|\\/).{1,}")) {
			if (var.contains(".")) {
				return new FileVariable(new File(var));
			} else {
				return new FolderVariable(new File(var));
			}
		} else if (var.equals("$")) {
			return new SpecialVariable();
		} else {
			StringVariable s = new StringVariable(var);
			if (var.startsWith("{") && var.endsWith("}")) {
				s.setRegex(true);
			}
			return s;
		}
	}

	public static Variable interpret(String in)
			throws VariableNameNotFoundException, ArgumentNameNotFoundException, IteratorNameNotFoundException {
		if (in.startsWith("#")) {
			return Main.lookUpVariable(in);
		} else if (in.startsWith("a")) {
			return Main.lookUpArgument(in);
		} else if (in.startsWith("i")) {
			return Main.lookUpIterator(in);
		} else {
			return Variable.fromString(in);
		}
	}
}