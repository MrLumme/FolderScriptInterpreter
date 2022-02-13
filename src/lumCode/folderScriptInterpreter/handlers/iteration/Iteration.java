package lumCode.folderScriptInterpreter.handlers.iteration;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.InfiniteLoopException;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.typeExceptions.IteratorTypeException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.ResultantNode;
import lumCode.folderScriptInterpreter.variables.ArrayVariable;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.TextVariable;
import lumCode.folderScriptInterpreter.variables.Variable;

public class Iteration implements Node {
	public static boolean breakCalled = false;

	private final int number;
	private final ResultantNode iterant;
	private final List<Node> script;
	private IterationType type;

	public Iteration(int number, ResultantNode iterant, List<Node> script) {
		this.number = number;
		this.iterant = iterant;
		this.script = script;
	}

	@Override
	public void action() throws InterpreterException {
		iterant.action();
		Variable var = iterant.result();
		if (var instanceof NumberVariable) {
			if (((NumberVariable) var).getVar() < 0) {
				throw new InfiniteLoopException((NumberVariable) var);
			}
			Main.i.put(number, new NumberVariable(0));
			type = IterationType.INTEGER_ITERATION;
		} else if (var instanceof FolderVariable) {
			Main.i.put(number, new FileVariable(null));
			type = IterationType.FOLDER_ITERATION;
		} else if (var instanceof TextVariable) {
			Main.i.put(number, new TextVariable(""));
			type = IterationType.STRING_ITERATION;
		} else if (var instanceof ArrayVariable) {
			Main.i.put(number, new ArrayVariable());
			type = IterationType.LIST_ITERATION;
		} else {
			throw new IteratorTypeException(var);
		}

		if (type == IterationType.INTEGER_ITERATION) {
			int till = (int) ((NumberVariable) var).getVar();
			while (((NumberVariable) Main.i.get(number)).getVar() < till) {
				for (Node n : script) {
					if (!breakCalled) {
						n.action();
					} else {
						break;
					}
				}
				if (breakCalled) {
					breakCalled = false;
					break;
				}
				((NumberVariable) Main.i.get(number))
						.setVar((int) (((NumberVariable) Main.i.get(number)).getVar() + 1));
			}
		} else if (type == IterationType.FOLDER_ITERATION) {
			File[] list = ((FolderVariable) var).getVar().listFiles();
			for (File f : list) {
				((FileVariable) Main.i.get(number)).setVar(f);
				for (Node n : script) {
					if (!breakCalled) {
						n.action();
					} else {
						break;
					}
				}
				if (breakCalled) {
					breakCalled = false;
					break;
				}
			}
		} else if (type == IterationType.STRING_ITERATION) {
			char[] seq = ((TextVariable) var).getVar().toCharArray();
			for (char c : seq) {
				((TextVariable) Main.i.get(number)).setVar("" + c);
				for (Node n : script) {
					if (!breakCalled) {
						n.action();
					} else {
						break;
					}
				}
				if (breakCalled) {
					breakCalled = false;
					break;
				}
			}
		} else if (type == IterationType.LIST_ITERATION) {
			TreeMap<Integer, Variable> list = new TreeMap<Integer, Variable>();
			list.putAll(((ArrayVariable) var).getAll());
			for (Variable v : list.values()) {
				Main.i.put(number, v);
				for (Node n : script) {
					if (!breakCalled) {
						n.action();
					} else {
						break;
					}
				}
				if (breakCalled) {
					breakCalled = false;
					break;
				}
			}
		}
	}

	public IterationType getType() {
		return type;
	}

	public int getNumber() {
		return number;
	}

	public ResultantNode getIterant() {
		return iterant;
	}

	public List<Node> getScript() {
		return script;
	}

	@Override
	public String toString() {
		String s = "";
		for (Node n : script) {
			s += n.toString() + ",";
		}
		s = s.substring(0, s.length() - 1);

		return "i" + number + "(" + iterant.toString() + ")" + "{" + s + "}";
	}

	public static void callBreak() {
		breakCalled = true;
	}
}
