package lumCode.folderScriptInterpreter.handlers.command;

import java.util.List;

import lumCode.folderScriptInterpreter.exceptions.CommandErrorException;
import lumCode.folderScriptInterpreter.exceptions.IncorrentParameterAmountException;
import lumCode.folderScriptInterpreter.handlers.BooleanNode;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.variables.NumberVariable;

public class BooleanCommand extends Command implements BooleanNode {

	public BooleanCommand(CommandType type, List<Node> input)
			throws IncorrentParameterAmountException, CommandErrorException {
		super(type, input);
		if (!type.isBooleanOutput()) {
			throw new CommandErrorException("Command of type '" + type.getChar()
					+ "' does not output a true ('1') or false ('0') only output.");
		}
	}

	@Override
	public NumberVariable result() {
		return (NumberVariable) super.result();
	}
}
