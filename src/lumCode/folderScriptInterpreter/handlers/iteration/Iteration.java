package lumCode.folderScriptInterpreter.handlers.iteration;

import java.io.File;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.InfiniteLoopException;
import lumCode.folderScriptInterpreter.exceptions.IteratorTypeException;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.IntVariable;
import lumCode.folderScriptInterpreter.variables.StringVariable;
import lumCode.folderScriptInterpreter.variables.Variable;

public class Iteration {
	private final int number;
	private final Variable rule;
	private final String[] script;
	private final IterationType type;

	public Iteration(int number, Variable rule, String[] script) throws IteratorTypeException, InfiniteLoopException {
		super();
		this.number = number;
		this.rule = rule;
		this.script = script;

		if (rule instanceof IntVariable) {
			if (((IntVariable) rule).getVar() < 0) {
				throw new InfiniteLoopException((IntVariable) rule);
			}
			Main.i[number] = new IntVariable(0);
			type = IterationType.INTEGER_ITERATION;
		} else if (rule instanceof FolderVariable) {
			Main.i[number] = new FileVariable(null);
			type = IterationType.FOLDER_ITERATION;
		} else if (rule instanceof StringVariable) {
			Main.i[number] = new StringVariable("");
			type = IterationType.STRING_ITERATION;
		} else {
			throw new IteratorTypeException(rule);
		}
	}

	public void action() throws IteratorTypeException, InfiniteLoopException {
		if (type == IterationType.INTEGER_ITERATION) {
			int till = ((IntVariable) rule).getVar();
			while (((IntVariable) Main.i[number]).getVar() < till) {

				// iterate

				((IntVariable) Main.i[number]).setVar(((IntVariable) Main.i[number]).getVar() + 1);
			}
		} else if (type == IterationType.FOLDER_ITERATION) {
			File[] list = ((FolderVariable) rule).getVar().listFiles();
			Main.i[number] = new FileVariable(null);
			for (File f : list) {
				((FileVariable) Main.i[number]).setVar(f);

				// iterate

			}
		} else if (type == IterationType.STRING_ITERATION) {
			char[] seq = ((StringVariable) rule).getVar().toCharArray();
			Main.i[number] = new StringVariable("");
			for (char c : seq) {
				((StringVariable) Main.i[number]).setVar("" + c);

				// iterate

			}
		}
	}

	public void explain() {
		if (type == IterationType.INTEGER_ITERATION) {
			// explain
		} else if (type == IterationType.FOLDER_ITERATION) {
			// explain
		} else if (type == IterationType.STRING_ITERATION) {
			// explain
		}
	}

	public IterationType getType() {
		return type;
	}

	public int getNumber() {
		return number;
	}

	public Variable getRule() {
		return rule;
	}

	public String[] getScript() {
		return script;
	}
}
