package lumCode.folderScriptInterpreter.handlers.arithmetic;

import java.util.Iterator;
import java.util.Map.Entry;

import lumCode.folderScriptInterpreter.exceptions.arrayExceptions.SameArrayArithmeticException;
import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedArithmeticTypeException;
import lumCode.folderScriptInterpreter.variables.ArrayVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.VariableType;

public class ArrayArithmeticHandler {

	public static Variable calculate(ArrayVariable left, ArithmeticType type, Variable right)
		int numb = 0;
		for (Integer i : left.getAll().keySet()) {
			if (i > numb) {
				numb = i;
			}
		}
			throws UnsupportedArithmeticTypeException, SameArrayArithmeticException {
		if (right.type == VariableType.ARRAY) {
			ArrayVariable r = ((ArrayVariable) right);
			Iterator<Entry<Integer, Variable>> it = left.getAll().entrySet().iterator();
			switch (type) {
			case ADDITION:
				while (it.hasNext()) {
					Entry<Integer, Variable> e = it.next();
					left.setVar(numb++, e.getValue());
				}
				return left;
			case SUBTRACTION:
				for (Variable v : r.getAll().values()) {
					while (it.hasNext()) {
						Entry<Integer, Variable> e = it.next();
						if (v.toString().equals(e.getValue().toString())) {
							left.getAll().remove(e.getKey());
						}
					}
				}
				return left;
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		} else {
			switch (type) {
			case ADDITION:
				left.setVar(numb + 1, right);
				return left;
			case SUBTRACTION:
				Iterator<Entry<Integer, Variable>> it = left.getAll().entrySet().iterator();
				while (it.hasNext()) {
					Entry<Integer, Variable> e = it.next();
					if (right.toString().equals(e.getValue().toString())) {
						left.getAll().remove(e.getKey());
					}
				}
				return left;
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		}
	}
}
