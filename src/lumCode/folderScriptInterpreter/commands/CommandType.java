package lumCode.folderScriptInterpreter.commands;

import lumCode.folderScriptInterpreter.exceptions.InvalidOperatorException;

public enum CommandType {
	NAME, EXTENSION, PARENT, IS_FILE, IS_AVAILABLE, COPY, MOVE, DELETE, LIST, PRINT, SIZE, REPLACE, SUBSTRING, RANDOM, READ, TEST, OVERWRITE;

	public static CommandType fromChar(char c) throws InvalidOperatorException {
		switch (c) {
		case 'n':
			return NAME;
		case 'e':
			return EXTENSION;
		case 'p':
			return PARENT;
		case 'f':
			return IS_FILE;
		case 'v':
			return IS_AVAILABLE;
		case 'c':
			return COPY;
		case 'm':
			return MOVE;
		case 'd':
			return DELETE;
		case 'l':
			return LIST;
		case 'o':
			return PRINT;
		case 's':
			return SIZE;
		case 'r':
			return REPLACE;
		case 'u':
			return SUBSTRING;
		case 'q':
			return RANDOM;
		case 'g':
			return READ;
		case 't':
			return TEST;
		case 'w':
			return OVERWRITE;
		}

		throw new InvalidOperatorException("The operator '" + c + "' is not valid.");
	}

	public static char toChar(CommandType c) throws InvalidOperatorException {
		switch (c) {
		case COPY:
			return 'c';
		case DELETE:
			return 'd';
		case EXTENSION:
			return 'e';
		case IS_AVAILABLE:
			return 'v';
		case IS_FILE:
			return 'f';
		case LIST:
			return 'l';
		case MOVE:
			return 'm';
		case NAME:
			return 'n';
		case OVERWRITE:
			return 'w';
		case PARENT:
			return 'p';
		case PRINT:
			return 'o';
		case RANDOM:
			return 'q';
		case READ:
			return 'g';
		case REPLACE:
			return 'r';
		case SIZE:
			return 's';
		case SUBSTRING:
			return 'u';
		case TEST:
			return 't';
		}
		throw new InvalidOperatorException("The command type '" + c.toString() + "' is invalid.");
	}

	public static int varCount(CommandType c) throws InvalidOperatorException {
		switch (c) {
		case NAME:
		case OVERWRITE:
		case PARENT:
		case SIZE:
		case TEST:
		case IS_FILE:
		case IS_AVAILABLE:
		case EXTENSION:
		case DELETE:
		case READ:
			return 1;
		case PRINT:
		case MOVE:
		case COPY:
		case LIST:
		case RANDOM:
			return 2;
		case REPLACE:
		case SUBSTRING:
			return 3;
		}

		throw new InvalidOperatorException("The command type '" + c.toString() + "' is invalid.");
	}
}
