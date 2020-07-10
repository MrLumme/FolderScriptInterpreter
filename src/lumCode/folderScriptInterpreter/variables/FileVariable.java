package lumCode.folderScriptInterpreter.variables;

import java.io.File;

public class FileVariable extends FolderVariable {

	public FileVariable(File var) {
		super(var, VariableType.FILE);
	}

	protected FileVariable(String name, File var) {
		super(name, var, VariableType.FILE);
	}

	@Override
	public String getName() {
		return getVar().getName().substring(0, getVar().getName().indexOf('.'));
	}

	public String getExtension() {
		return getVar().getName().substring(getVar().getName().indexOf('.'));
	}

	@Override
	public String toString() {
		return getVar().getAbsolutePath();
	}
}
