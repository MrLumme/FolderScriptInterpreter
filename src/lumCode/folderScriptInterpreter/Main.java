package lumCode.folderScriptInterpreter;

import java.util.ArrayList;

import lumCode.folderScriptInterpreter.exceptions.ArgumentNameNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.BreakDownException;
import lumCode.folderScriptInterpreter.exceptions.IncorrectParameterTypeException;
import lumCode.folderScriptInterpreter.exceptions.IncorrentParameterAmountException;
import lumCode.folderScriptInterpreter.exceptions.InfiniteLoopException;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.IteratorNameNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.IteratorTypeException;
import lumCode.folderScriptInterpreter.exceptions.NameNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedCommandTypeException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedTypeException;
import lumCode.folderScriptInterpreter.exceptions.VariableNameNotFoundException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.arithmetic.ArithmeticType;
import lumCode.folderScriptInterpreter.handlers.command.Command;
import lumCode.folderScriptInterpreter.handlers.command.CommandType;
import lumCode.folderScriptInterpreter.handlers.conditional.Conditional;
import lumCode.folderScriptInterpreter.handlers.iteration.Iteration;
import lumCode.folderScriptInterpreter.handlers.logic.Logic;
import lumCode.folderScriptInterpreter.handlers.logic.LogicType;
import lumCode.folderScriptInterpreter.variables.Variable;

public class Main {
	public static boolean overwrite = false;
	public static boolean caseSensitive = false;
	public static String script = "";
	public static Variable[] a;
	public static Variable[] i;
	public static final ArrayList<Variable> vars = new ArrayList<>();
	public static final ArrayList<Node> nodes = new ArrayList<>();

	public static void main(String[] args) throws InterpreterException {
		script = args[0];
		a = new Variable[args.length - 1];
		for (int i = 1; i < args.length; i++) {
			a[i - 1] = Variable.fromString(args[i]);
		}

		nodes.addAll(breakDownScript(script));

	}

	private static ArrayList<Node> breakDownScript(String script) throws BreakDownException {
		char[] c = script.toCharArray();
		ArrayList<Node> out = new ArrayList<Node>();

		for (int i = 0; i < c.length; i++) {
			if (CommandType.valid(c[i])) {
				CommandType t;
				String params = "";
				// Find command type
				try {
					t = CommandType.fromChar(c[i]);
				} catch (UnsupportedTypeException e) {
					throw new BreakDownException(script, i, c[i], e.getMessage());
				}

				// Find params
				if (c[i + 1] == '(') {
					i += 2;
					while (c[i] != ')') {
						params += c[i];
						i++;
						if (i == c.length) {
							throw new BreakDownException(script, i, c[i], "End parenthesis was expected.");
						}
					}
				} else {
					throw new BreakDownException(script, i, c[i], "Start parenthesis was expected.");
				}

				// Create node
				try {
					out.add(new Command(t, params));
				} catch (UnsupportedCommandTypeException | IncorrentParameterAmountException
						| IncorrectParameterTypeException | NameNotFoundException e) {
					throw new BreakDownException(script, i, c[i], e.getMessage());
				}
			} else if (ArithmeticType.valid(c[i])) {
				ArithmeticType t;
				// Find command type
				try {
					t = ArithmeticType.fromChar(c[i]);
				} catch (UnsupportedTypeException e) {
					throw new BreakDownException(script, i, c[i], e.getMessage());
				}
			} else if (c[i] == '#') {
				String name = "" + c[i];
				while (c[i + 1] == '=' || c[i + 1] == '!') {
					i++;
					name += c[i];
				}

			} else if (c[i] == 'i') {
				String number = "";
				String rule = "";
				ArrayList<Node> n;
				// Find iteration number
				do {
					i++;
					number += c[i];
				} while (c[i + 1] == '(');
				// Find rule
				if (c[i + 1] == '(') {
					i += 2;
					while (c[i] != ')') {
						rule += c[i];
						i++;
						if (i == c.length) {
							throw new BreakDownException(script, i, c[i], "End parenthesis was expected.");
						}
					}
				} else {
					throw new BreakDownException(script, i, c[i], "Start parenthesis was expected.");
				}

				// Find nodes
				if (c[i + 1] == '{') {
					i += 2;
					String subScript = "";
					while (c[i] != '}') {
						subScript += c[i];
						i++;
						if (i == c.length) {
							throw new BreakDownException(script, i, c[i], "End parenthesis was expected.");
						}
					}
					n = breakDownScript(subScript);
				} else {
					throw new BreakDownException(script, i, c[i], "Start parenthesis was expected.");
				}

				// Create node
				try {
					out.add(new Iteration(i, Variable.interpret(rule), (Node[]) n.toArray()));
				} catch (IteratorTypeException | InfiniteLoopException | NameNotFoundException e) {
					throw new BreakDownException(script, i, c[i], e.getMessage());
				}
			} else if (c[i] == '?') {
				Variable left, right;
				LogicType type;
				ArrayList<Node> n;
				// Find condition
				if (c[i + 1] == '(') {
					i += 2;
					String con = "";
					while (c[i] != ')') {
						con += c[i];
						i++;
						if (i == c.length) {
							throw new BreakDownException(script, i, c[i], "End parenthesis was expected.");
						}
					}

					String[] ar = con.split("=<>!");
					try {
						left = Variable.interpret(ar[0]);
					} catch (NameNotFoundException e) {
						throw new BreakDownException(script, i + 1, '#', e.getMessage());
					}
					try {
						right = Variable.interpret(ar[1]);
					} catch (NameNotFoundException e) {
						throw new BreakDownException(script, i + ar[0].length() + 2, '#', e.getMessage());
					}
					char l = con.charAt(ar[0].length());
					try {
						type = LogicType.fromChar(l);
					} catch (UnsupportedTypeException e) {
						throw new BreakDownException(script, i + ar[0].length() + 1, l, e.getMessage());
					}
				} else {
					throw new BreakDownException(script, i, c[i], "Start parenthesis was expected.");
				}

				// Find nodes
				if (c[i + 1] == '{') {
					i += 2;
					String subScript = "";
					while (c[i] != '}') {
						subScript += c[i];
						i++;
						if (i == c.length) {
							throw new BreakDownException(script, i, c[i], "End parenthesis was expected.");
						}
					}
					n = breakDownScript(subScript);
				} else {
					throw new BreakDownException(script, i, c[i], "Start parenthesis was expected.");
				}

				// Create node
				out.add(new Conditional(new Logic(left, type, right), (Node[]) n.toArray()));
			} else if (c[i] == 'h') {
				// todo
			} else if (c[i] == '^') {
				i++;
				while (c[i] != '^') {
					i++;
					if (i == c.length) {
						throw new BreakDownException(script, i, c[i], "End of comment was expected.");
					}
				}
			}
		}
		return out;
	}

	public static Variable lookUpVariable(String name) throws VariableNameNotFoundException {
		for (Variable var : vars) {
			if (var.name.equals(name)) {
				return var;
			}
		}
		throw new VariableNameNotFoundException(name);
	}

	public static Variable lookUpArgument(String in) throws ArgumentNameNotFoundException {
		int n = Integer.parseInt(in.split("\\[\\]")[1]);
		if (a.length < n) {
			return a[n];
		}
		throw new ArgumentNameNotFoundException(n);
	}

	public static Variable lookUpIterator(String in) throws IteratorNameNotFoundException {
		int n = Integer.parseInt(in.split("\\[\\]")[1]);
		if (i.length < n) {
			return i[n];
		}
		throw new IteratorNameNotFoundException(n);
	}
}
