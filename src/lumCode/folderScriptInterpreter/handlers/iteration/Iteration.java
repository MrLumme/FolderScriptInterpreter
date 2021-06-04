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
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.TextVariable;
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

		if (iterant instanceof NumberVariable) {
			if (((NumberVariable) iterant).getVar() < 0) {
				throw new InfiniteLoopException((NumberVariable) iterant);
			}
			Main.i.put(number, new NumberVariable(0));
			type = IterationType.INTEGER_ITERATION;
		} else if (iterant instanceof FolderVariable) {
			Main.i.put(number, new FileVariable(null));
			type = IterationType.FOLDER_ITERATION;
		} else if (iterant instanceof TextVariable) {
			Main.i.put(number, new TextVariable(""));
			type = IterationType.STRING_ITERATION;
		} else if (iterant instanceof ArrayVariable) {
			Main.i.put(number, new ArrayVariable());
			type = IterationType.LIST_ITERATION;
		} else {
			throw new IteratorTypeException(iterant);
		}
	}

	@Override
	public void action() throws InterpreterException {
		if (type == IterationType.INTEGER_ITERATION) {
			int till = (int) ((NumberVariable) iterant).getVar();
			while (((NumberVariable) Main.i.get(number)).getVar() < till) {
				for (Node n : script) {
					n.action();
				}
				((NumberVariable) Main.i.get(number))
						.setVar((int) (((NumberVariable) Main.i.get(number)).getVar() + 1));
			}
		} else if (type == IterationType.FOLDER_ITERATION) {
			File[] list = ((FolderVariable) iterant).getVar().listFiles();
			for (File f : list) {
				((FileVariable) Main.i.get(number)).setVar(f);
				for (Node n : script) {
					n.action();
				}
			}
		} else if (type == IterationType.STRING_ITERATION) {
			char[] seq = ((TextVariable) iterant).getVar().toCharArray();
			for (char c : seq) {
				((TextVariable) Main.i.get(number)).setVar("" + c);
				for (Node n : script) {
					n.action();
				}
			}
		} else if (type == IterationType.LIST_ITERATION) {
			TreeMap<Integer, Variable> list = new TreeMap<Integer, Variable>();
			list.putAll(((ArrayVariable) iterant).getAll());
			for (Variable v : list.values()) {
				Main.i.put(number, v);
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
