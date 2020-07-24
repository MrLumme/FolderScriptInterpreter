package lumCode.folderScriptInterpreter.handlers.command;

import lumCode.folderScriptInterpreter.exceptions.UnsupportedCommandTypeException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedTypeException;

public enum CommandType {
	NAME, EXTENSION, PARENT, IS_FILE, IS_AVAILABLE, COPY, MOVE, DELETE, LIST, PRINT, SIZE, REPLACE, SUBSTRING, RANDOM,
	READ, TEST, OVERWRITE, CASE_SENSITIVE, SLEEP, EXIT;

	public static CommandType fromChar(char c) throws UnsupportedTypeException {
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
		case 'k':
			return CASE_SENSITIVE;
		case 'z':
			return SLEEP;
		case 'x':
			return EXIT;
		}

		throw new UnsupportedTypeException(c);
	}

	public static char toChar(CommandType c) throws UnsupportedCommandTypeException {
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
		case CASE_SENSITIVE:
			return 'k';
		case SLEEP:
			return 'z';
		case EXIT:
			return 'x';
		}
		throw new UnsupportedCommandTypeException(c);
	}

	public static boolean valid(char c) {
		switch (c) {
		case 'n':
		case 'e':
		case 'p':
		case 'f':
		case 'v':
		case 'c':
		case 'm':
		case 'd':
		case 'l':
		case 'o':
		case 's':
		case 'r':
		case 'u':
		case 'q':
		case 'g':
		case 't':
		case 'w':
		case 'k':
		case 'z':
		case 'x':
			return true;
		default:
			return false;
		}
	}

	public static int inputCount(CommandType c) throws UnsupportedCommandTypeException {
		switch (c) {
		case NAME:
		case OVERWRITE:
		case CASE_SENSITIVE:
		case PARENT:
		case SIZE:
		case TEST:
		case IS_FILE:
		case IS_AVAILABLE:
		case EXTENSION:
		case DELETE:
		case READ:
		case SLEEP:
		case EXIT:
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
		throw new UnsupportedCommandTypeException(c);
	}

	public static boolean outputs(CommandType c) throws UnsupportedCommandTypeException {
		switch (c) {
		case NAME:
		case PARENT:
		case SIZE:
		case TEST:
		case IS_FILE:
		case IS_AVAILABLE:
		case EXTENSION:
		case DELETE:
		case READ:
		case MOVE:
		case COPY:
		case LIST:
		case RANDOM:
		case REPLACE:
		case SUBSTRING:
			return true;
		case OVERWRITE:
		case CASE_SENSITIVE:
		case PRINT:
		case SLEEP:
		case EXIT:
			return false;
		}
		throw new UnsupportedCommandTypeException(c);
	}
}
