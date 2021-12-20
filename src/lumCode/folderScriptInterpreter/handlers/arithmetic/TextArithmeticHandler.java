package lumCode.folderScriptInterpreter.handlers.arithmetic;

import java.io.File;

import lumCode.folderScriptInterpreter.exceptions.UndefinedArithmeticException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedArithmeticTypeException;
import lumCode.folderScriptInterpreter.variables.ArrayVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.TextVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.VariableType;

public class TextArithmeticHandler {

	public static Variable calculate(TextVariable left, ArithmeticType type, Variable right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {
		String l = left.getVar();
		if (right.type == VariableType.NUMBER) {
			long r = ((NumberVariable) right).getVar();
			switch (type) {
			case ADDITION:
				return new TextVariable(left.getVar() + r);
			case SUBTRACTION:
				if (l.endsWith("" + r)) {
					return new TextVariable(l.substring(0, l.length() - ("" + r).length()));
				}
				return new TextVariable(l);
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		} else if (right.type == VariableType.TEXT) {
			String r = ((TextVariable) right).getVar();
			switch (type) {
			case ADDITION:
				return new TextVariable(left.getVar() + r);
			case SUBTRACTION:
				if (l.endsWith(r)) {
					return new TextVariable(l.substring(0, l.length() - r.length()));
				}
				return new TextVariable(l);
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		} else if (right.type == VariableType.FILE || right.type == VariableType.FOLDER) {
			File r = ((FolderVariable) right).getVar();
			switch (type) {
			case ADDITION:
				return new TextVariable(l + r.getAbsolutePath());
			case SUBTRACTION:
				if (l.endsWith(r.getAbsolutePath())) {
					return new TextVariable(l.substring(0, l.length() - r.getAbsolutePath().length()));
				}
				return new TextVariable(l);
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		} else if (right.type == VariableType.SPECIAL) {
			switch (type) {
			case ADDITION:
				return new TextVariable(left.getVar() + "$");
			case SUBTRACTION:
				if (l.endsWith("$")) {
					return new TextVariable(l.substring(0, l.length() - 1));
				}
				return new TextVariable(l);
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		} else if (right.type == VariableType.ARRAY) {
			ArrayVariable r = (ArrayVariable) right;
			switch (type) {
			case ADDITION:
				String str = left.getVar();
				boolean rem = false;
				for (Variable v : r.getAll().values()) {
					str += v.toString() + ", ";
					rem = true;
				}
				if (rem) {
					str = str.substring(0, str.length() - 2);
				}
				return new TextVariable(str);
			case SUBTRACTION:
				if (l.endsWith("$")) {
					return new TextVariable(l.substring(0, l.length() - 1));
				}
				return new TextVariable(l);
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		}
		throw new UndefinedArithmeticException(left.toString(), type, right.toString());
	}
}
