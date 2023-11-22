package lumCode.folderScriptInterpreter.handlers.command;

import java.util.EnumSet;

import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedTypeException;

public enum CommandType {
	NAME('n', 1), EXTENSION('e', 1), PARENT('p', 1), IS_FILE('f', 1), IS_AVAILABLE('v', 1), COPY('c', 2), MOVE('m', 2),
	DELETE('d', 1), LIST('l', 2), WRITE('w', 2), SIZE('s', 1), REPLACE('y', 3), SUBSTRING('u', 3), RANDOM('q', 1),
	READ('r', 1), OPTIONS('o', 2), SLEEP('z', 1), EXIT('x', 1), GEN_MD5('g', 1), EXTERNAL('k', 2);

	public final static EnumSet<CommandType> booleanOutputs = EnumSet.of(IS_AVAILABLE, IS_FILE, COPY, MOVE, DELETE);
	public final static EnumSet<CommandType> outputs = EnumSet.of(NAME, EXTENSION, PARENT, LIST, SIZE, REPLACE,
			SUBSTRING, RANDOM, READ, GEN_MD5, EXTERNAL);

	private final char symbol;
	private final int input;

	private CommandType(char symbol, int input) {
		this.symbol = symbol;
		this.input = input;
	}

	public static CommandType fromChar(char c) throws UnsupportedTypeException {
		for (CommandType t : CommandType.values()) {
			if (t.symbol == c) {
				return t;
			}
		}
		throw new UnsupportedTypeException(c);
	}

	public static boolean valid(char c) {
		for (CommandType t : CommandType.values()) {
			if (t.symbol == c) {
				return true;
			}
		}
		return false;
	}

	public char getChar() {
		return symbol;
	}

	public int getInput() {
		return input;
	}

	public boolean isOutput() {
		return outputs.contains(this) || isBooleanOutput();
	}

	public boolean isBooleanOutput() {
		return booleanOutputs.contains(this);
	}
}
