package lumCode.folderScriptInterpreter.handlers.iteration;

import java.io.File;
import java.util.TreeMap;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.InfiniteLoopException;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.IteratorTypeException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.variables.ArrayVariable;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.IntVariable;
import lumCode.folderScriptInterpreter.variables.StringVariable;
import lumCode.folderScriptInterpreter.variables.Variable;

public class Iteration implements Node {
	private final int number;
	private final Variable iterant;
	private final Node[] script;
	private final IterationType type;

	public Iteration(int number, Variable iterant, Node[] script) throws IteratorTypeException, InfiniteLoopException {
		this.number = number;
		this.iterant = iterant;
		this.script = script;

		if (iterant instanceof IntVariable) {
			if (((IntVariable) iterant).getVar() < 0) {
				throw new InfiniteLoopException((IntVariable) iterant);
			}
			Main.i[number] = new IntVariable(0);
			type = IterationType.INTEGER_ITERATION;
		} else if (iterant instanceof FolderVariable) {
			Main.i[number] = new FileVariable(null);
			type = IterationType.FOLDER_ITERATION;
		} else if (iterant instanceof StringVariable) {
			Main.i[number] = new StringVariable("");
			type = IterationType.STRING_ITERATION;
		} else if (iterant instanceof ArrayVariable) {
			Main.i[number] = new ArrayVariable();
			type = IterationType.LIST_ITERATION;
		} else {
			throw new IteratorTypeException(iterant);
		}
	}

	@Override
	public void action() throws InterpreterException {
		if (type == IterationType.INTEGER_ITERATION) {
			int till = ((IntVariable) iterant).getVar();
			while (((IntVariable) Main.i[number]).getVar() < till) {
				for (Node n : script) {
					n.action();
				}
				((IntVariable) Main.i[number]).setVar(((IntVariable) Main.i[number]).getVar() + 1);
			}
		} else if (type == IterationType.FOLDER_ITERATION) {
			File[] list = ((FolderVariable) iterant).getVar().listFiles();
			Main.i[number] = new FileVariable(null);
			for (File f : list) {
				((FileVariable) Main.i[number]).setVar(f);
				for (Node n : script) {
					n.action();
				}
			}
		} else if (type == IterationType.STRING_ITERATION) {
			char[] seq = ((StringVariable) iterant).getVar().toCharArray();
			Main.i[number] = new StringVariable("");
			for (char c : seq) {
				((StringVariable) Main.i[number]).setVar("" + c);
				for (Node n : script) {
					n.action();
				}
			}
		} else if (type == IterationType.LIST_ITERATION) {
			TreeMap<Integer, Variable> list = new TreeMap<Integer, Variable>();
			list.putAll(((ArrayVariable) iterant).getAll());
			Main.i[number] = new ArrayVariable();
			for (Variable f : list.values()) {
				Main.i[number] = f;
				for (Node n : script) {
					n.action();
				}
			}
		}
	}

	@Override
	public void explain() {
		if (type == IterationType.INTEGER_ITERATION) {
			// explain
		} else if (type == IterationType.FOLDER_ITERATION) {
			// explain
		} else if (type == IterationType.STRING_ITERATION) {
			// explain
		} else if (type == IterationType.LIST_ITERATION) {
			// explain
		}
	}

	public IterationType getType() {
		return type;
	}

	public int getNumber() {
		return number;
	}

	public Variable getIterant() {
		return iterant;
	}

	public Node[] getScript() {
		return script;
	}
}
