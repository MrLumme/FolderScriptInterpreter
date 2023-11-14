package lumCode.folderScriptInterpreter.handlers.breaking;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.Node;

public class Break implements Node {
	private static boolean called = false;

	@Override
	public void action() throws InterpreterException {
		called = true;
	}

	public static boolean isCalled() {
		return called;
	}

	public static void handled() {
		called = false;
	}

	@Override
	public String toString() {
		return "b";
	}
}
