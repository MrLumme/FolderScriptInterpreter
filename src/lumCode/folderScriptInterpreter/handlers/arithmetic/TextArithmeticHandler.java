package lumCode.folderScriptInterpreter.handlers.arithmetic;

import java.io.File;

import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedArithmeticTypeException;
import lumCode.folderScriptInterpreter.exceptions.undefinedExceptions.UndefinedArithmeticException;
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
			case DIVISION:
				// Not defined
				break;
			case MODULO:
				// Not defined
				break;
			case MULTIPLICATION:
				// Not defined
				break;
			case SUBTRACTION:
				return new TextVariable(l.substring(0, (int) (l.length() - r)));
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		} else if (right.type == VariableType.TEXT) {
			String r = ((TextVariable) right).getVar();
			switch (type) {
			case ADDITION:
				return new TextVariable(left.getVar() + r);
			case DIVISION:
				// Not defined
				break;
			case MODULO:
				// Not defined
				break;
			case MULTIPLICATION:
				// Not defined
				break;
			case SUBTRACTION:
				return new TextVariable(l.replace(r, ""));
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		} else if (right.type == VariableType.FILE || right.type == VariableType.FOLDER) {
			File r = ((FolderVariable) right).getVar();
			switch (type) {
			case ADDITION:
				return new TextVariable(l + r.getAbsolutePath());
			case DIVISION:
				// Not defined
				break;
			case MODULO:
				// Not defined
				break;
			case MULTIPLICATION:
				// Not defined
				break;
			case SUBTRACTION:
				return new TextVariable(l.replace(r.getAbsolutePath(), ""));
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		} else if (right.type == VariableType.SPECIAL) {
			switch (type) {
			case ADDITION:
				return new TextVariable(left.getVar() + "$");
			case DIVISION:
				// Not defined
				break;
			case MODULO:
				// Not defined
				break;
			case MULTIPLICATION:
				// Not defined
				break;
			case SUBTRACTION:
				return new TextVariable(l.replace("$", ""));
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		} else if (right.type == VariableType.ARRAY) {
			ArrayVariable r = (ArrayVariable) right;
			String str = left.getVar();
			switch (type) {
			case ADDITION:
				boolean rem = false;
				for (Variable v : r.getAll().values()) {
					str += v.toString() + ", ";
					rem = true;
				}
				if (rem) {
					str = str.substring(0, str.length() - 2);
				}
				return new TextVariable(str);
			case DIVISION:
				// Not defined
				break;
			case MODULO:
				// Not defined
				break;
			case MULTIPLICATION:
				// Not defined
				break;
			case SUBTRACTION:
				for (Variable v : r.getAll().values()) {
					str = str.replace(v.toString(), "");
				}
				return new TextVariable(str);
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		}
		throw new UndefinedArithmeticException(left.toString(), type, right.toString());
	}
}
