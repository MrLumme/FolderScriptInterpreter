package lumCode.folderScriptInterpreter.handlers.command;

import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedTypeException;

public enum CommandType {
	NAME('n', 1), EXTENSION('e', 1), PARENT('p', 1), IS_FILE('f', 1, true, true), IS_AVAILABLE('v', 1, true, true),
	COPY('c', 2, true, true), MOVE('m', 2, true, true), DELETE('d', 1, true, true), LIST('l', 2), WRITE('w', 2, false),
	SIZE('s', 1), REPLACE('y', 3), SUBSTRING('u', 3), RANDOM('q', 1), READ('r', 1), OPTIONS('o', 2, false),
	SLEEP('z', 1, false), EXIT('x', 1, false), GEN_MD5('g', 1);

	private char symbol;
	private int input;
	private boolean output;
	private boolean boolOut;

	private CommandType(char symbol, int input) {
		this(symbol, input, true, false);
	}

	private CommandType(char symbol, int input, boolean output) {
		this(symbol, input, output, false);
	}

	private CommandType(char symbol, int input, boolean output, boolean boolOut) {
		this.symbol = symbol;
		this.input = input;
		this.output = output;
		this.boolOut = boolOut;
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

	public boolean isBooleanOutput() {
		return boolOut;
	}
}
