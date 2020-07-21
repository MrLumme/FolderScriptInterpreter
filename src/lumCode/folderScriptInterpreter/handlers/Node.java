package lumCode.folderScriptInterpreter.handlers;

import lumCode.folderScriptInterpreter.exceptions.CommandErrorException;
import lumCode.folderScriptInterpreter.exceptions.LogicConversionException;
import lumCode.folderScriptInterpreter.exceptions.UndefinedCommandException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedCommandTypeException;

public interface Node {

	public void action() throws UnsupportedCommandTypeException, CommandErrorException, LogicConversionException,
			UndefinedCommandException;

	public void explain();
}
