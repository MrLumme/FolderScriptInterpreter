package lumCode.folderScriptInterpreter.variables;

import java.io.File;

public class FolderVariable extends Variable {
	File var;

	protected FolderVariable(File var) {
		super(VariableType.FOLDER);
		this.var = var;
	}

	protected FolderVariable(File var, VariableType type) {
		super(type);
		this.var = var;
	}

	protected FolderVariable(String name, File var) {
		super(VariableType.FOLDER, name);
		this.var = var;
	}

	protected FolderVariable(String name, File var, VariableType type) {
		super(type, name);
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
