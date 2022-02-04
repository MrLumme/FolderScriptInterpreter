package lumCode.folderScriptInterpreter.handlers.logic;

import java.io.File;
import java.io.FileNotFoundException;

import lumCode.folderScriptInterpreter.Utilities;
import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedLogicTypeException;
import lumCode.folderScriptInterpreter.exceptions.undefinedExceptions.UndefinedLogicException;

public class FileLogicHandler {

	@SuppressWarnings("incomplete-switch")
	protected static boolean Interpret(File left, LogicType operator, File right)
			throws UndefinedLogicException, UnsupportedLogicTypeException, FileNotFoundException {
		switch (operator) {
		case EQUAL:
			return Utilities.getMD5(left).equals(Utilities.getMD5(right));
		case NOT:
			return !Utilities.getMD5(left).equals(Utilities.getMD5(right));
		case LESS:
			return Utilities.fileSize(left) < Utilities.fileSize(right);
		case GREATER:
			return Utilities.fileSize(left) > Utilities.fileSize(right);
		}
		throw new UndefinedLogicException(left.getAbsolutePath(), operator, right.getAbsolutePath());
	}
}
