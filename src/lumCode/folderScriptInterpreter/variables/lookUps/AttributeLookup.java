package lumCode.folderScriptInterpreter.variables.lookUps;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.typeExceptions.IncorrectVariableTypeException;
import lumCode.folderScriptInterpreter.variables.*;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AttributeLookup extends VariableLookUp {

	private final AttributeType type;

	public AttributeLookup(String name, AttributeType type) {
		super(name);
		this.type = type;
	}

	@Override
	public void action() throws InterpreterException {
		Variable var = Variable.fetch(getName());

		if (!(var instanceof FolderVariable)) {
			throw new IncorrectVariableTypeException(type, var);
		}

		// TODO Fill cases
		switch (type) {
			case HIDDEN:
				setResult(new BooleanVariable(false));
				break;
			case LINK:
				setResult(new BooleanVariable(false));
				break;
			case CREATION_TIME:
				setResult(new TextVariable("empty"));
				break;
			case LAST_MODIFIED_TIME:
				setResult(new TextVariable("empty"));
				break;
			case LAST_ACCESSED_TIME:
				setResult(new TextVariable("empty"));
				break;
		}
	}

	@Override
	public String toString() {
		return "'" + type.getId();
	}

}
