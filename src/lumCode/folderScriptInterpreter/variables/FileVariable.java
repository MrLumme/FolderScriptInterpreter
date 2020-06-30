package lumCode.folderScriptInterpreter.variables;

import java.io.File;

public class FileVariable extends FolderVariable {
	File var;

	public FileVariable(File var) {
		super(var);
	}

	@Override
	public String getName() {
		return var.getName().substring(0, var.getName().indexOf('.'));
	}

	public String getExtension() {
		return var.getName().substring(var.getName().indexOf('.'));
	}
}
