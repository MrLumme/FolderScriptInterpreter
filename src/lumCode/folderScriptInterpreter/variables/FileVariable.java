package lumCode.folderScriptInterpreter.variables;

import java.io.File;

public class FileVariable extends FolderVariable {

	public FileVariable(File var) {
		super(var, VariableType.FILE);
	}

	@Override
	public String getName() {
		return getVar().getName().substring(0, getVar().getName().lastIndexOf('.'));
	}

	public String getExtension() {
		return getVar().getName().substring(getVar().getName().lastIndexOf('.'));
	}

	@Override
	public FileVariable copy() {
		return new FileVariable(getVar());
	}
}
