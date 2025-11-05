package lumCode.folderScriptInterpreter.variables.lookUps;

import lumCode.folderScriptInterpreter.exceptions.AttributeLookupErrorException;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.typeExceptions.IncorrectVariableTypeException;
import lumCode.folderScriptInterpreter.variables.*;

import java.io.IOException;

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

		FolderVariable folder = ((FolderVariable) var);
		try {
			setResult(folder.getAttribute(type));
		} catch (IOException e) {
			throw new AttributeLookupErrorException(folder);
		}
	}

	@Override
	public String toString() {
		return "'" + type.getId();
	}

}
