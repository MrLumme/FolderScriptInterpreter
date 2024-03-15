package lumCode.folderScriptInterpreter.variables;

import java.io.File;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.arrayExceptions.ArrayPositionEmptyException;
import lumCode.folderScriptInterpreter.exceptions.notFoundExceptions.ArgumentNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.notFoundExceptions.IteratorNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.notFoundExceptions.VariableNotFoundException;
import lumCode.folderScriptInterpreter.handlers.ResultantNode;

public abstract class Variable implements ResultantNode {
	public final VariableType type;

	protected Variable(VariableType type) {
		this.type = type;
	}

	public static Variable fromString(String var) {
		if (var.matches("^-{0,1}[0-9]{1,}$")) {
			long num = Long.parseLong(var);
			if (num > -1 && num < 2) {
				return new BooleanVariable(num == 1);
			} else {
				return new NumberVariable(Long.parseLong(var));
			}
		} else if (var.matches("^([A-Za-z]{1}:|\\\\)(\\\\|\\/)[^:\"?|*<>]{1,}$")) {
			var = var.replace("\\", "/");
			if (var.contains(".") && var.lastIndexOf(".") > var.lastIndexOf("/")) {
				return new FileVariable(new File(var));
			} else {
				return new FolderVariable(new File(var));
			}
		} else if (var.equals("$")) {
			return SpecialVariable.getInstance();
		} else {
			TextVariable s = new TextVariable(var.replaceAll("\\\\\"", "\""));
			if (var.startsWith("{") && var.endsWith("}")) {
				s.setRegex(true);
			}
			return s;
		}
	}

	public static Variable fetch(String in) throws VariableNotFoundException, ArgumentNotFoundException,
			IteratorNotFoundException, ArrayPositionEmptyException {
		if (in.startsWith("#")) {
			if (in.contains("[") && in.contains("]")) {
				return Main.lookUpVariable(in,
						Integer.parseInt(in.substring(in.indexOf('[') + 1, in.lastIndexOf(']'))));
			} else {
				return Main.lookUpVariable(in);
			}
		} else if (in.startsWith("a")) {
			return Main.lookUpArgument(in);
		} else if (in.startsWith("i")) {
			return Main.lookUpIterator(in);
		}
		return null;
	}

	public static boolean exists(String in) {
		try {
			fetch(in);
			return true;
		} catch (VariableNotFoundException | ArgumentNotFoundException | IteratorNotFoundException
				| ArrayPositionEmptyException e) {
			return false;
		}
	}

	public abstract Variable copy();

	@Override
	public void action() {
		// Do nothing
	}

	@Override
	public Variable result() {
		return this;
	}
}