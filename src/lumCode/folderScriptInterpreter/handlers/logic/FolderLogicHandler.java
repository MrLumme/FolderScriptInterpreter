package lumCode.folderScriptInterpreter.handlers.logic;

import java.io.File;

import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedLogicTypeException;
import lumCode.folderScriptInterpreter.exceptions.undefinedExceptions.UndefinedLogicException;

public class FolderLogicHandler {

	protected static boolean Interpret(File left, LogicType operator, File right)
			throws UndefinedLogicException, UnsupportedLogicTypeException {
		return StringLogicHandler.Interpret(left.getAbsolutePath(), operator, left.getAbsolutePath());
	}
}
