package lumCode.folderScriptInterpreter.variables.lookUps;

import java.io.File;
import java.time.LocalDateTime;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.TextVariable;

public class EnvironmentLookUp extends LookUp {
	private final EnvironmentType type;

	public EnvironmentLookUp(EnvironmentType type) {
		this.type = type;
	}

	@Override
	public void action() throws InterpreterException {
		switch (type) {
		case DATE_TIME:
			setResult(new TextVariable(LocalDateTime.now().format(Main.dateFormater)));
			break;
		case EXECUTION_LOCATION:
			setResult(new FolderVariable(new File(System.getProperty("user.dir"))));
			break;
		case LOG_FILE:
			setResult(new FileVariable(Main.logFile));
			break;
		case SYSTEM_TIME:
			setResult(new NumberVariable(System.currentTimeMillis()));
			break;
		case TEMP_LOCATION:
			setResult(new FolderVariable(Main.tempDir));
			break;
		}
	}

	@Override
	public String toString() {
		return "." + type.getId();
	}

}
