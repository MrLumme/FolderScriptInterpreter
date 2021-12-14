package lumCode.folderScriptInterpreter.handlers.arithmetic;

import java.io.File;

import lumCode.folderScriptInterpreter.exceptions.UndefinedArithmeticException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedArithmeticTypeException;

public class FileArithmeticHandler {
	public static File interpret(File left, ArithmeticType type, String right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {
		String path = left.getParent();
		String name = left.getName();
		String ext = left.getName().substring(left.getName().indexOf('.'));

		switch (type) {
		case ADDITION:
			return new File(path + "\\" + name + right + "." + ext);
		default:
			throw new UndefinedArithmeticException(left.getAbsolutePath(), type, right);
		}
	}

	public static File interpret(File left, ArithmeticType operator, long right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {
		switch (operator) {
		case ADDITION:
			return new File(left.getParent() + "\\" + left.getName().replace(".", "" + right + "."));
		default:
			throw new UndefinedArithmeticException(left.getAbsolutePath(), operator, "" + right);
		}
	}

	@SuppressWarnings("incomplete-switch")
	public static File interpret(File left, ArithmeticType type, File right)
			throws UndefinedArithmeticException, UnsupportedArithmeticTypeException {

		if (right.isDirectory()) {
			switch (type) {
			case ADDITION:
				return new File(right.getAbsolutePath() + "\\" + left.getName());
			}
		} else {
			// Only files
			switch (type) {
			case ADDITION:
				return new File(left.getParent() + "\\" + right.getName());
			}
		}
		throw new UndefinedArithmeticException(left.getAbsolutePath(), type, right.getAbsolutePath());
	}
}
