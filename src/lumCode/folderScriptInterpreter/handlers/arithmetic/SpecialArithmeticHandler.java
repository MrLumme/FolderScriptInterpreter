package lumCode.folderScriptInterpreter.handlers.arithmetic;

import lumCode.folderScriptInterpreter.exceptions.UndefinedArithmeticException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedArithmeticTypeException;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
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
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		} else if (right.type == VariableType.SPECIAL) {
			switch (type) {
			case ADDITION:
				return new NumberVariable(3141592653L);
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		}
		throw new UndefinedArithmeticException(left.toString(), type, right.toString());
	}
}
