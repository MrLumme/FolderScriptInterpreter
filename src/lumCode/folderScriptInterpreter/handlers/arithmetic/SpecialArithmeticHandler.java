package lumCode.folderScriptInterpreter.handlers.arithmetic;

import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedArithmeticTypeException;
import lumCode.folderScriptInterpreter.exceptions.undefinedExceptions.UndefinedArithmeticException;
import lumCode.folderScriptInterpreter.variables.SpecialVariable;
import lumCode.folderScriptInterpreter.variables.TextVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.VariableType;

public class SpecialArithmeticHandler {

	public static Variable calculate(SpecialVariable left, ArithmeticType type, Variable right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {
		if (right.type == VariableType.TEXT) {
			String r = ((TextVariable) right).getVar();
			switch (type) {
			case ADDITION:
				return new TextVariable("$" + r);
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
