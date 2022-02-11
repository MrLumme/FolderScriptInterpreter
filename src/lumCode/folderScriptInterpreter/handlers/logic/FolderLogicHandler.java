package lumCode.folderScriptInterpreter.handlers.logic;

import java.io.File;
import java.io.FileNotFoundException;

import lumCode.folderScriptInterpreter.Utilities;
import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedLogicTypeException;
import lumCode.folderScriptInterpreter.exceptions.undefinedExceptions.UndefinedLogicException;

public class FolderLogicHandler {

	protected static boolean Interpret(File left, LogicType operator, File right)
			throws UndefinedLogicException, UnsupportedLogicTypeException, FileNotFoundException {
		switch (operator) {
		case EQUAL:
			return TextLogicHandler.Interpret(left.getAbsolutePath(), operator, left.getAbsolutePath());
		case NOT:
			return TextLogicHandler.Interpret(left.getAbsolutePath(), operator, left.getAbsolutePath());
		case LESS:
			return Utilities.folderSize(left) < Utilities.folderSize(right);
		case GREATER:
			return Utilities.folderSize(left) > Utilities.folderSize(right);
		}
		throw new UndefinedLogicException(left.getAbsolutePath(), operator, right.getAbsolutePath());
	}
}
