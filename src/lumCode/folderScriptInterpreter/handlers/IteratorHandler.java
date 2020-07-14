package lumCode.folderScriptInterpreter.handlers;

import java.io.File;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.InfiniteLoopException;
import lumCode.folderScriptInterpreter.exceptions.IteratorTypeException;
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

			IntVariable val = new IntVariable(0);
			Main.i[number] = val;

			while (val.getVar() < till) {

				// iterate

				val.setVar(val.getVar() + 1);
			}
		} else if (rule instanceof FolderVariable) {
			File[] list = ((FolderVariable) rule).getVar().listFiles();
			for (File f : list) {
				// Add file val

				// iterate
			}
		} else if (rule instanceof StringVariable) {
			String key = ((StringVariable) rule).getVar();
			int point = 0;
			char val = key.charAt(point);

			// iterate

		} else {
			throw new IteratorTypeException(rule);
		}
	}
}
