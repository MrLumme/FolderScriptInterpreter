package lumCode.folderScriptInterpreter.variables;

import java.io.File;

public class FolderVariable extends Variable {
	private File var;

	public FolderVariable(File var) {
		super(VariableType.FOLDER);
		this.var = var;
	}

	protected FolderVariable(File var, VariableType type) {
		super(type);
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

	@Override
	public String toString() {
		return var.getAbsolutePath();
	}

	@Override
	public FolderVariable copy() {
		return new FolderVariable(var);
	}
}
