package lumCode.folderScriptInterpreter.handlers.arithmetic;

import java.io.File;

import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedArithmeticTypeException;
import lumCode.folderScriptInterpreter.exceptions.undefinedExceptions.UndefinedArithmeticException;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.TextVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.VariableType;

public class NumberArithmeticHandler {

	public static Variable calculate(NumberVariable left, ArithmeticType type, Variable right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {
		Long l = left.getVar();

		if (right.type == VariableType.NUMBER) {
			long r = ((NumberVariable) right).getVar();
			switch (type) {
			case ADDITION:
				return new NumberVariable(l + r);
			case DIVISION:
				return new NumberVariable(l / r);
			case MODULO:
				return new NumberVariable(l % r);
			case MULTIPLICATION:
				return new NumberVariable(l * r);
			case SUBTRACTION:
				return new NumberVariable(l - r);
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		} else if (right.type == VariableType.TEXT) {
			String r = ((TextVariable) right).getVar();
			switch (type) {
			case ADDITION:
				return new TextVariable("" + l + ((TextVariable) right).getVar());
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
				// Not defined
				break;
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		} else if (right.type == VariableType.FILE) {
			File r = ((FileVariable) right).getVar();
			switch (type) {
			case ADDITION:
				return new FileVariable(new File(r.getParent() + "\\" + l + r.getName()));
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
				// Not defined
				break;
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		} else if (right.type == VariableType.FOLDER) {
			File r = ((FolderVariable) right).getVar();
			switch (type) {
			case ADDITION:
				return new FolderVariable(new File(r.getParent() + "\\" + l + r.getName()));
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
				// Not defined
				break;
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		}
		throw new UndefinedArithmeticException(left.toString(), type, right.toString());
	}
}
