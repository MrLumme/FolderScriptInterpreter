package lumCode.folderScriptInterpreter.exceptions;

import lumCode.folderScriptInterpreter.variables.FolderVariable;

public class AttributeLookupErrorException extends InterpreterException {
	private static final long serialVersionUID = -5730219498059848406L;

	public AttributeLookupErrorException(FolderVariable file) {
		super("Could not read attributes on file / folder \"" + file.toString() + "\".");
	}

}
