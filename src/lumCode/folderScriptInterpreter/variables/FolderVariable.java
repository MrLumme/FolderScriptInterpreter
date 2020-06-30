package lumCode.folderScriptInterpreter.variables;

import java.io.File;

public class FolderVariable extends Variable {
	File var;

	public FolderVariable(File var) {
		this.var = var;
	}

	public File getVar() {
		return var;
	}

	public void setVar(File var) {
		this.var = var;
	}

	public String getName() {
		return var.getName();
	}

	public String getPath() {
		return var.getAbsolutePath();
	}

	public File getParent() {
		return var.getParentFile();
	}
}
