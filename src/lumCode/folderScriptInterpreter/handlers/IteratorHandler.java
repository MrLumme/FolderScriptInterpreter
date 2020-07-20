package lumCode.folderScriptInterpreter.handlers;

import java.io.File;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.InfiniteLoopException;
import lumCode.folderScriptInterpreter.exceptions.IteratorTypeException;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.IntVariable;
import lumCode.folderScriptInterpreter.variables.StringVariable;
import lumCode.folderScriptInterpreter.variables.Variable;

public class IteratorHandler {

	public void interpret(int number, Variable rule, String[] script)
			throws IteratorTypeException, InfiniteLoopException {
		if (rule instanceof IntVariable) {
			int till = ((IntVariable) rule).getVar();

			if (till < 0) {
				throw new InfiniteLoopException((IntVariable) rule);
			}

			Main.i[number] = new IntVariable(0);
			while (((IntVariable) Main.i[number]).getVar() < till) {

				// iterate

				((IntVariable) Main.i[number]).setVar(((IntVariable) Main.i[number]).getVar() + 1);
			}
		} else if (rule instanceof FolderVariable) {
			File[] list = ((FolderVariable) rule).getVar().listFiles();
			Main.i[number] = new FileVariable(null);
			for (File f : list) {
				((FileVariable) Main.i[number]).setVar(f);

				// iterate

			}
		} else if (rule instanceof StringVariable) {
			char[] seq = ((StringVariable) rule).getVar().toCharArray();
			Main.i[number] = new StringVariable("");
			for (char c : seq) {
				((StringVariable) Main.i[number]).setVar("" + c);

				// iterate

			}
		} else {
			throw new IteratorTypeException(rule);
		}
		Main.i[number] = null;
	}
}
