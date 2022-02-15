package lumCode.folderScriptInterpreter.handlers.arithmetic;

import java.util.Iterator;
import java.util.Map.Entry;

import lumCode.folderScriptInterpreter.exceptions.arrayExceptions.SameArrayArithmeticException;
import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedArithmeticTypeException;
import lumCode.folderScriptInterpreter.exceptions.undefinedExceptions.UndefinedArithmeticException;
import lumCode.folderScriptInterpreter.variables.ArrayVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.VariableType;

public class ArrayArithmeticHandler {

	public static Variable calculate(ArrayVariable left, ArithmeticType type, Variable right)
			throws UnsupportedArithmeticTypeException, SameArrayArithmeticException, UndefinedArithmeticException {

		if (right.type == VariableType.ARRAY) {
			ArrayVariable r = ((ArrayVariable) right);
			if (left == r) {
				throw new SameArrayArithmeticException();
			}
			switch (type) {
			case ADDITION:
				for (Variable e : r.getAll().values()) {
					left.setNextVar(e);
				}
				return left;
			case SUBTRACTION:
				for (Variable v : r.getAll().values()) {
					Iterator<Entry<Integer, Variable>> it = left.getAll().entrySet().iterator();
					while (it.hasNext()) {
						Entry<Integer, Variable> e = it.next();
						if (v.toString().equals(e.getValue().toString())) {
							it.remove();
						}
					}
				}
				return left;
			case DIVISION:
				// Not defined
				break;
			case MODULO:
				// Not defined
				break;
			case MULTIPLICATION:
				// Not defined
				break;
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		} else {
			switch (type) {
			case ADDITION:
				left.setNextVar(right);
				return left;
			case SUBTRACTION:
				Iterator<Entry<Integer, Variable>> it = left.getAll().entrySet().iterator();
				while (it.hasNext()) {
					Entry<Integer, Variable> e = it.next();
					if (right.toString().equals(e.getValue().toString())) {
						it.remove();
					}
				}
				return left;
			case DIVISION:
				// Not defined
				break;
			case MODULO:
				// Not defined
				break;
			case MULTIPLICATION:
				// Not defined
				break;
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		}
		throw new UndefinedArithmeticException(left.toString(), type, right.toString());
	}
}
