package lumCode.folderScriptInterpreter.handlers.arithmetic;

import java.io.File;

import lumCode.folderScriptInterpreter.exceptions.UndefinedArithmeticException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedArithmeticTypeException;

public class FolderArithmeticHandler {
	@SuppressWarnings("incomplete-switch")
	public static File interpret(File left, ArithmeticType type, File right)
			throws UndefinedArithmeticException, UnsupportedArithmeticTypeException {

		if (right.isFile()) {
			switch (type) {
			case ADDITION:
				return new File(left.getAbsolutePath() + "\\" + right.getName());
			}
		} else {
			// Only directories
			switch (type) {
			case ADDITION:
				return new File(left.getAbsolutePath() + "\\" + right.getName());
			}
		}
		throw new UndefinedArithmeticException(left.getAbsolutePath(), type, right.getAbsolutePath());
	}

	public static File interpret(File left, ArithmeticType operator, String right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {
		switch (operator) {
		case ADDITION:
			return new File(left.getParent() + "\\" + left.getName().replace(".", right + "."));
		default:
			throw new UndefinedArithmeticException(left.getAbsolutePath(), operator, right);
		}
	}

	public static File interpret(File left, ArithmeticType operator, long right)
			throws UnsupportedArithmeticTypeException, UndefinedArithmeticException {
		switch (operator) {
		case ADDITION:
			return new File(left.getParent() + "\\" + left.getName() + right);
		default:
			throw new UndefinedArithmeticException(left.getAbsolutePath(), operator, "" + right);
		}
	}
}
