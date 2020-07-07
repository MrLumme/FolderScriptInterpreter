package lumCode.folderScriptInterpreter.handlers;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.IncorrentParameterAmountException;
import lumCode.folderScriptInterpreter.exceptions.InvalidOperatorException;
import lumCode.folderScriptInterpreter.exceptions.LogicConversionException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedVariableTypeException;
import lumCode.folderScriptInterpreter.variables.IntVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.VariableType;

public class CommandHandler {

	public static Variable Interpret(char c, String params) throws InvalidOperatorException,
			IncorrentParameterAmountException, UnsupportedVariableTypeException, LogicConversionException {
		CommandType type = CommandType.fromChar(c);

		String[] list = params.split(",");
		if (CommandType.varCount(type) != list.length) {
			throw new IncorrentParameterAmountException("The command '" + c + "' expects " + CommandType.varCount(type)
					+ " inputs parameters, but has gotten " + list.length + ".");
		}

		// Make variables

		Variable[] vars = new Variable[list.length];
		for (int i = 0; i < list.length; i++) {
			vars[i] = Variable.fromString(list[i]);

			if (type == CommandType.COPY || type == CommandType.DELETE || type == CommandType.MOVE
					|| type == CommandType.EXTENSION || type == CommandType.NAME || type == CommandType.IS_FILE
					|| type == CommandType.IS_AVAILABLE || type == CommandType.PARENT || type == CommandType.READ
					|| (i == 1 && type == CommandType.PRINT) || (i == 0 && type == CommandType.LIST)) {
				if (vars[i].type != VariableType.FILE && vars[i].type != VariableType.FOLDER) {
					throw new UnsupportedVariableTypeException("The variable '" + vars[i].toString() + "' at position "
							+ i + " resolves to the type '" + vars[i].type.toString().toLowerCase()
							+ "' which is not supported for the command '" + CommandType.toChar(type) + "'.");
				}
			} else if (type == CommandType.REPLACE || type == CommandType.SUBSTRING) {
				if (vars[i].type == VariableType.SPECIAL) {
					throw new UnsupportedVariableTypeException("The variable '" + vars[i].toString() + "' at position "
							+ i + " resolves to the type '" + vars[i].type.toString().toLowerCase()
							+ "' which is not supported for the command '" + CommandType.toChar(type) + "'.");
				}
			} else if (type == CommandType.RANDOM) {
				if (vars[i].type != VariableType.INT) {
					throw new UnsupportedVariableTypeException("The variable '" + vars[i].toString() + "' at position "
							+ i + " resolves to the type '" + vars[i].type.toString().toLowerCase()
							+ "' which is not supported for the command '" + CommandType.toChar(type) + "'.");
				}
			} else if (i == 1 && type == CommandType.LIST) {
				if (vars[i].type != VariableType.INT || vars[i].type != VariableType.SPECIAL) {
					throw new UnsupportedVariableTypeException("The variable '" + vars[i].toString() + "' at position "
							+ i + " resolves to the type '" + vars[i].type.toString().toLowerCase()
							+ "' which is not supported for the command '" + CommandType.toChar(type) + "'.");
				}

			} else if (type == CommandType.OVERWRITE) {
				if (vars[i].type != VariableType.INT) {
					throw new UnsupportedVariableTypeException("The variable '" + vars[i].toString() + "' at position "
							+ i + " resolves to the type '" + vars[i].type.toString().toLowerCase()
							+ "' which is not supported for the command '" + CommandType.toChar(type) + "'.");
				}
			}
		}

		// Perform action

		if (type == CommandType.OVERWRITE) {
			Main.overwrite = ((IntVariable) vars[0]).asBoolean();
			return null;
		} else if (type == CommandType.RANDOM) {

			Main.overwrite = ((IntVariable) vars[0]).asBoolean();
			return new IntVariable(Math.random());
		}
	}
}