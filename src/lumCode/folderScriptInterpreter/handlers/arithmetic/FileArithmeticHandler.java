package lumCode.folderScriptInterpreter.handlers.arithmetic;

import java.io.File;

import lumCode.folderScriptInterpreter.exceptions.UndefinedArithmeticException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedArithmeticTypeException;

public class FileArithmeticHandler {
	public static File interpret(File left, ArithmeticType operator, String right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {
		String path = left.getParent();
		String name = left.getName();
		String ext = left.getName().substring(left.getName().indexOf('.'));

		switch (operator) {
		case ADDITION:
			return new File(path + "\\" + name + right + "." + ext);
		default:
			throw new UndefinedArithmeticException(left.getAbsolutePath(), operator, right);
		}
	}

	public static File interpret(String left, ArithmeticType operator, File right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {
		String path = right.getParent();
		String name = right.getName();
		String ext = right.getName().substring(right.getName().indexOf('.'));

		switch (operator) {
		case ADDITION:
			return new File(path + "\\" + name + left + "." + ext);
		default:
			throw new UndefinedArithmeticException(left, operator, right.getAbsolutePath());
		}
	}
}
