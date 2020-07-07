package lumCode.folderScriptInterpreter.handlers.logic;

import java.io.File;

import lumCode.folderScriptInterpreter.exceptions.UndefinedLogicException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedLogicTypeException;

public class FileLogicHandler {

	protected static boolean Interpret(File left, LogicType operator, File right)
			throws UndefinedLogicException, UnsupportedLogicTypeException {
		return StringLogicHandler.Interpret(left.getAbsolutePath(), operator, left.getAbsolutePath());
	}
}
