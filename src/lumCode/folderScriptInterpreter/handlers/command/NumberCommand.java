package lumCode.folderScriptInterpreter.handlers.command;

import java.util.List;

import lumCode.folderScriptInterpreter.exceptions.CommandErrorException;
import lumCode.folderScriptInterpreter.exceptions.IncorrentParameterAmountException;
import lumCode.folderScriptInterpreter.handlers.BooleanNode;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.variables.BooleanVariable;
import lumCode.folderScriptInterpreter.variables.NumberVariable;

public class NumberCommand extends Command implements BooleanNode {

	public NumberCommand(CommandType type, List<Node> input)
			throws IncorrentParameterAmountException, CommandErrorException {
		super(type, input);
		if (!type.isNumberOutput()) {
			if (type.isInputDependantNumberOutput()) {
				if (type == CommandType.RANDOM && input.size() > 0 && input.get(0) instanceof NumberVariable) {

//				} else if (type == CommandType.SUBSTRING && input.size() > 2 && (super.substringCommand(input.get(0),
//						input.get(1), input.get(2)) instanceof NumberVariable)) {
//
//				} else if (type == CommandType.REPLACE && input.size() > 0 && input.get(0) instanceof NumberVariable) {

				} else {
					throw new CommandErrorException("Command of type '" + type.getChar()
							+ "' does not output a true ('1') or false ('0') only output.");
				}
			} else {
				throw new CommandErrorException("Command of type '" + type.getChar()
						+ "' does not output a true ('1') or false ('0') only output.");
			}
		}
	}

	@Override
	public BooleanVariable result() {
		return (BooleanVariable) super.result();
	}
}
