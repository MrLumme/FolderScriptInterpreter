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

public class FileArithmeticHandler {

	public static Variable calculate(FileVariable left, ArithmeticType type, Variable right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {
		File l = left.getVar();
		if (right.type == VariableType.NUMBER) {
			long r = ((NumberVariable) right).getVar();
			switch (type) {
			case ADDITION:
				return new FileVariable(new File(l.getParent() + "\\" + l.getName().replace(".", "" + r + ".")));
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		} else if (right.type == VariableType.TEXT) {
			String r = ((TextVariable) right).getVar();
			switch (type) {
			case ADDITION:
				return new FileVariable(new File(l.getParent() + "\\" + l.getName().replace(".", r + ".")));
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		} else if (right.type == VariableType.FILE) {
			File r = ((FileVariable) right).getVar();
			switch (type) {
			case ADDITION:
				return new FileVariable(new File(l.getParent() + "\\" + r.getName()));
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		} else if (right.type == VariableType.FOLDER) {
			File r = ((FolderVariable) right).getVar();
			switch (type) {
			case ADDITION:
				return new FileVariable(new File(r.getAbsolutePath() + "\\" + l.getName()));
			default:
				throw new UnsupportedArithmeticTypeException(type);
			}
		} else if (right.type == VariableType.SPECIAL) {
			// Not defined
		} else if (right.type == VariableType.ARRAY) {
			// Not defined
		}
		throw new UndefinedArithmeticException(left.toString(), type, right.toString());
	}
}
