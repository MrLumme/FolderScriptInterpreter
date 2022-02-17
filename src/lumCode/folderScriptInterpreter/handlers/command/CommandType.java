package lumCode.folderScriptInterpreter.handlers.command;

import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedTypeException;

public enum CommandType {
	NAME('n', 1), EXTENSION('e', 1), PARENT('p', 1), IS_FILE('f', 1), IS_AVAILABLE('v', 1), COPY('c', 2), MOVE('m', 2),
	DELETE('d', 1), LIST('l', 2), WRITE('w', 2, false), SIZE('s', 1), REPLACE('y', 3), SUBSTRING('u', 3),
	RANDOM('q', 1), READ('r', 1), OPTIONS('o', 2, false), SLEEP('z', 1, false), EXIT('x', 1, false), GEN_MD5('g', 1);

	private char symbol;
	private int input;
	private boolean output;

	private CommandType(char symbol, int input) {
		this(symbol, input, true);
	}

	private CommandType(char symbol, int input, boolean output) {
		this.symbol = symbol;
		this.input = input;
		this.output = output;
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
		return output;
	}
}
