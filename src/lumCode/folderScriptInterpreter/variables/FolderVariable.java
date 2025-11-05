package lumCode.folderScriptInterpreter.variables;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.notFoundExceptions.AttributeNotFoundException;
import lumCode.folderScriptInterpreter.variables.lookUps.AttributeType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class FolderVariable extends Variable {
	private File var;
	private BasicFileAttributes att;

	public FolderVariable(File var) {
		super(VariableType.FOLDER);
		this.var = var;
	}

	protected FolderVariable(File var, VariableType type) {
		super(type);
		this.var = var;
	}

	public Variable getAttribute(AttributeType type) throws IOException, AttributeNotFoundException {
		Path file = Paths.get(var.toURI());
		if (att == null) {
			att = Files.readAttributes(file, BasicFileAttributes.class);
		}
		switch (type) {
			case IS_HIDDEN:
				return new BooleanVariable(Files.isHidden(file));
			case IS_LINK:
				return new BooleanVariable(Files.isSymbolicLink(file));
			case CREATION_TIME:
				return new TextVariable(Main.dateFormater.format(att.creationTime().toInstant()));
			case LAST_MODIFIED_TIME:
				return new TextVariable(Main.dateFormater.format(Files.getLastModifiedTime(file).toInstant()));
			case LAST_ACCESS_TIME:
				return new TextVariable(Main.dateFormater.format(att.lastAccessTime().toInstant()));
		}
		throw new AttributeNotFoundException(type.getId());
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
