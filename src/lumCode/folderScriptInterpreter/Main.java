package lumCode.folderScriptInterpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lumCode.folderScriptInterpreter.exceptions.ArgumentNameNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.BreakDownException;
import lumCode.folderScriptInterpreter.exceptions.CommandErrorException;
import lumCode.folderScriptInterpreter.exceptions.IncorrentParameterAmountException;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.IteratorNameNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.ScriptErrorException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedTypeException;
import lumCode.folderScriptInterpreter.exceptions.VariableNameNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.VariableNotArrayException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.command.Command;
import lumCode.folderScriptInterpreter.handlers.command.CommandType;
import lumCode.folderScriptInterpreter.handlers.declaring.Declaration;
import lumCode.folderScriptInterpreter.handlers.declaring.DeclarationType;
import lumCode.folderScriptInterpreter.variables.ArrayVariable;
import lumCode.folderScriptInterpreter.variables.Variable;

public class Main {
	public static boolean overwrite = false;
	public static boolean caseSensitive = false;
	public static String script = "";
	public static final HashMap<Integer, Variable> a = new HashMap<Integer, Variable>();
	public static final HashMap<Integer, Variable> i = new HashMap<Integer, Variable>();
	public static final HashMap<String, Variable> v = new HashMap<String, Variable>();
	public static final ArrayList<Node> nodes = new ArrayList<>();

	public static void main(String[] args) throws InterpreterException {
		script = args[0];
		for (int i = 1; i < args.length; i++) {
			a.put(i - 1, Variable.fromString(args[i]));
		}
		cleanScript();
		for (String[] s : splitScript(script)) {
			nodes.addAll(breakDownScript(s));
		}
	}

	private static void cleanScript() {
		String rem = "";
		String[] spl = script.split("\\^");
		for (int i = 0; i < spl.length; i++) {
			if (i % 2 == 0) {
				rem += spl[i];
			}
		}
		boolean inString = false;
		for (int i = 0; i < rem.length(); i++) {
			if (rem.charAt(i) == '"') {
				inString = !inString;
			} else if (!inString && (rem.charAt(i) == ' ' && rem.charAt(i) == '\n' && rem.charAt(i) == '\t'
					&& rem.charAt(i) == '\r')) {
				rem = rem.substring(0, i) + rem.substring(i + 1);
				i--;
			}
		}

		script = rem;
	}

	private static List<String[]> splitScript(String script) throws ScriptErrorException {
		List<String[]> out = new ArrayList<String[]>();

		char[] c = script.replace('\t', ' ').toCharArray();

		if (script.matches("^[^A-IK-Xza-ik-xz#?^]")) {
			throw new ScriptErrorException(script,
					"The start of the script contains invalid or non-functional instructions.");
		}
		String head = "";
		String com = "";
		String act = "";
		int cPar = 0;
		int aPar = 0;
		boolean done = false;
		for (int i = 0; i < c.length; i++) {
			// Check parenthesis
			if (c[i] == '(' && aPar == 0) {
				cPar++;
			} else if (c[i] == ')' && aPar == 0) {
				cPar--;
				if (cPar == 0) {
					if ((i + 1 < c.length && c[i + 1] != '{') || i + 1 >= c.length) {
						done = true;
					}
				}
			} else if (c[i] == '{' && cPar == 0) {
				aPar++;
			} else if (c[i] == '}' && cPar == 0) {
				aPar--;
				if (aPar == 0) {
					done = true;
				}
			} else if (c[i] == ',' && cPar == 0 && aPar == 0) {
				// if (!head.equals("") && !com.equals("")) {
				done = true;
				// }
			} else if ((c[i] == ' ' || c[i] == ';' || c[i] == '\n' || c[i] == '\r') && cPar == 0 && aPar == 0) {
				done = true;
			}
			// Add char
			if (cPar > 0) {
				com += c[i];
			} else if (aPar > 0) {
				act += c[i];
			} else if (!done) {
				head += c[i];
			}

			// Reset if done
			if (done) {
				while (head.startsWith("(") || head.startsWith("{") || head.startsWith(",")) {
					head = head.substring(1);
				}
				while (com.startsWith("(") || com.startsWith("{") || com.startsWith(",")) {
					com = com.substring(1);
				}
				while (act.startsWith("(") || act.startsWith("{") || act.startsWith(",")) {
					act = act.substring(1);
				}

				head.trim();
				com.trim();
				act.trim();

				out.add(new String[] { head.equals("") ? null : head, com.equals("") ? null : com,
						act.equals("") ? null : act });

				head = "";
				com = "";
				act = "";
				done = false;
			}
		}

		head.trim();
		com.trim();
		act.trim();
		out.add(new String[] { head.equals("") ? null : head, com.equals("") ? null : com,
				act.equals("") ? null : act });

		return out;
	}

	private static ArrayList<Node> breakDownScript(String[] splScript) throws InterpreterException {
		ArrayList<Node> out = new ArrayList<Node>();
		char c = splScript[0].charAt(0);

		if (CommandType.valid(c)) {
			// Command logic
			if (splScript.length > 2 && splScript[2] != null) {
				throw new ScriptErrorException(splScript[0] + splScript[1] + splScript[2],
						"Syntax error; command type operations can not contain '{ }' parenthesis.");
			}
			CommandType t;
			try {
				t = CommandType.fromChar(c);
			} catch (UnsupportedTypeException e) {
				throw new BreakDownException(splScript[0], c, e.getMessage());
			}
			List<String[]> arg = splitScript(splScript[1]);
			List<Node> ins = new ArrayList<Node>();
			for (String[] s : arg) {
				try {
					Variable var = Variable.fetch(s[0]);
					if (var != null) {
						out.add(var);
					} else {
						throw new ScriptErrorException(s[0], "Could not be resolved to a value");
					}
				} catch (VariableNameNotFoundException | ArgumentNameNotFoundException
						| IteratorNameNotFoundException e) {
					out = breakDownScript(s);
				}
				if (out.size() > 1) {
					throw new ScriptErrorException(s[0] + s[1] + s[2],
							"Syntax error; can not perform multiple unrelated commands in command inputs.");
				}
			}
			try {
				out.add(new Command(t, ins));
			} catch (IncorrentParameterAmountException | CommandErrorException e) {
				throw new BreakDownException(splScript[0] + splScript[1], c, e.getMessage());
			}
		} else if (c == '#') {
			// Declaration logic
			Pattern p = Pattern.compile("^#[A-Za-z0-9_]{1,}(?=(\\[[0-9]{1,}\\]){0,1}(=|!))");
			Matcher m = p.matcher(splScript[0]);
			String name;
			if (m.find()) {
				name = m.group(0);
			} else {
				throw new ScriptErrorException(splScript[0], "Syntax error; could not interpret '#' declaration.");
			}
			char d = splScript[0].charAt(splScript[0].indexOf(name) + name.length());
			DeclarationType t;
			try {
				t = DeclarationType.fromChar(d);
			} catch (UnsupportedTypeException e) {
				throw new BreakDownException(splScript[0], d, e.getMessage());
			}
			String res = splScript[0].substring(splScript[0].indexOf(name) + name.length() + 1);

			Variable value = null;
			if (res.matches("([0-9]{1,}|\\$|\".{0,}\")")) {
				if (res.startsWith("\"")) {
					res = res.substring(1, res.length() - 1);
				}
				value = Variable.fromString(res);
				out.add(new Declaration(name, t, value));
			} else {
				List<String[]> spl = splitScript(res);
				if (spl.size() > 1) {
					throw new ScriptErrorException(res,
							"Syntax error; can not perform multiple unrelated commands in declaration.");
				}
				out = breakDownScript(spl.get(0));
				if (out.size() > 1) {
					throw new ScriptErrorException(res,
							"Syntax error; can not perform multiple unrelated commands in declaration.");
				}
				Node node = out.get(0);
				out.add(new Declaration(name, t, node));
			}
		} else if (c == '?') {
			// Conditional logic
		} else if (c == 'i') {
			// Iteration logic
		} else if (c == 'b') {
			// Break loop logic
		} else if (c == 'h') {
			// Help logic
		}

		return out;
	}

//	private static ArrayList<Node> breakDownScript(String script) throws BreakDownException {
//		char[] c = script.toCharArray();
//		ArrayList<Node> out = new ArrayList<Node>();
//
//		for (int i = 0; i < c.length; i++) {
//			if (CommandType.valid(c[i])) {
//				CommandType t;
//				List<List<Node>> params = null;
//				// Find command type
//				try {
//					t = CommandType.fromChar(c[i]);
//				} catch (UnsupportedTypeException e) {
//					throw new BreakDownException(script, i, c[i], e.getMessage());
//				}
//
//				// Find params
//				if (c[i + 1] == '(') {
//					i += 2;
//					while (c[i] != ')') {
//						params += c[i];
//						i++;
//						if (i == c.length) {
//							throw new BreakDownException(script, i, c[i], "End parenthesis was expected.");
//						}
//					}
//				} else {
//					throw new BreakDownException(script, i, c[i], "Start parenthesis was expected.");
//				}
//
//				// Create node
//				try {
//					out.add(new Command(t, params));
//				} catch (UnsupportedCommandTypeException | IncorrentParameterAmountException
//						| IncorrectParameterTypeException | NameNotFoundException e) {
//					throw new BreakDownException(script, i, c[i], e.getMessage());
//				}
//			} else if (ArithmeticType.valid(c[i])) {
//				ArithmeticType t;
//				// Find command type
//				try {
//					t = ArithmeticType.fromChar(c[i]);
//				} catch (UnsupportedTypeException e) {
//					throw new BreakDownException(script, i, c[i], e.getMessage());
//				}
//			} else if (c[i] == '#') {
//				String name = "" + c[i];
//				while (c[i + 1] == '=' || c[i + 1] == '!' || c[i + 1] == ' ') {
//					i++;
//					name += c[i];
//				}
//
//			} else if (c[i] == 'i') {
//				String number = "";
//				String rule = "";
//				ArrayList<Node> n;
//				// Find iteration number
//				do {
//					i++;
//					number += c[i];
//				} while (c[i + 1] == '(');
//				// Find rule
//				if (c[i + 1] == '(') {
//					i += 2;
//					while (c[i] != ')') {
//						rule += c[i];
//						i++;
//						if (i == c.length) {
//							throw new BreakDownException(script, i, c[i], "End parenthesis was expected.");
//						}
//					}
//				} else {
//					throw new BreakDownException(script, i, c[i], "Start parenthesis was expected.");
//				}
//
//				// Find nodes
//				if (c[i + 1] == '{') {
//					i += 2;
//					String subScript = "";
//					while (c[i] != '}') {
//						subScript += c[i];
//						i++;
//						if (i == c.length) {
//							throw new BreakDownException(script, i, c[i], "End parenthesis was expected.");
//						}
//					}
//					n = breakDownScript(subScript);
//				} else {
//					throw new BreakDownException(script, i, c[i], "Start parenthesis was expected.");
//				}
//
//				// Create node
//				try {
//					out.add(new Iteration(i, Variable.interpret(rule), (Node[]) n.toArray()));
//				} catch (IteratorTypeException | InfiniteLoopException | NameNotFoundException e) {
//					throw new BreakDownException(script, i, c[i], e.getMessage());
//				}
//			} else if (c[i] == '?') {
//				Variable left, right;
//				LogicType type;
//				ArrayList<Node> n;
//				// Find condition
//				if (c[i + 1] == '(') {
//					i += 2;
//					String con = "";
//					while (c[i] != ')') {
//						con += c[i];
//						i++;
//						if (i == c.length) {
//							throw new BreakDownException(script, i, c[i], "End parenthesis was expected.");
//						}
//					}
//
//					String[] ar = con.split("=<>!");
//					try {
//						left = Variable.interpret(ar[0]);
//					} catch (NameNotFoundException e) {
//						throw new BreakDownException(script, i + 1, '#', e.getMessage());
//					}
//					try {
//						right = Variable.interpret(ar[1]);
//					} catch (NameNotFoundException e) {
//						throw new BreakDownException(script, i + ar[0].length() + 2, '#', e.getMessage());
//					}
//					char l = con.charAt(ar[0].length());
//					try {
//						type = LogicType.fromChar(l);
//					} catch (UnsupportedTypeException e) {
//						throw new BreakDownException(script, i + ar[0].length() + 1, l, e.getMessage());
//					}
//				} else {
//					throw new BreakDownException(script, i, c[i], "Start parenthesis was expected.");
//				}
//
//				// Find nodes
//				if (c[i + 1] == '{') {
//					i += 2;
//					String subScript = "";
//					while (c[i] != '}') {
//						subScript += c[i];
//						i++;
//						if (i == c.length) {
//							throw new BreakDownException(script, i, c[i], "End parenthesis was expected.");
//						}
//					}
//					n = breakDownScript(subScript);
//				} else {
//					throw new BreakDownException(script, i, c[i], "Start parenthesis was expected.");
//				}
//
//				// Create node
//				out.add(new Conditional(new Logic(left, type, right), (Node[]) n.toArray()));
//			} else if (c[i] == 'h') {
//				// TODO
//			} else if (c[i] == '^') {
//				i++;
//				while (c[i] != '^') {
//					i++;
//					if (i == c.length) {
//						throw new BreakDownException(script, i, c[i], "End of comment was expected.");
//					}
//				}
//			}
//		}
//		return out;
//	}

	public static Variable lookUpVariable(String name) throws VariableNameNotFoundException {
		if (name.contains("[")) {
			int numb = Integer.parseInt(name.substring(name.indexOf('[') + 1, name.indexOf(']')));
			Variable var = v.get(name.substring(0, name.indexOf('[')));
			if (var != null && var instanceof ArrayVariable) {
				return ((ArrayVariable) var).getVar(numb);
			}
		} else {
			Variable var = v.get(name);
			if (var != null) {
				return var;
			}
		}
		throw new VariableNameNotFoundException(name);
	}

	public static Variable lookUpArgument(String name) throws ArgumentNameNotFoundException {
		int n = Integer.parseInt(name.split("\\[\\]")[1]);
		Variable var = a.get(n);
		if (var != null) {
			return var;
		}
		throw new ArgumentNameNotFoundException(n);
	}

	public static Variable lookUpIterator(String name) throws IteratorNameNotFoundException {
		int n = Integer.parseInt(name.split("\\[\\]")[1]);
		Variable var = i.get(n);
		if (var != null) {
			return var;
		}
		throw new IteratorNameNotFoundException(n);
	}

	public static void setVariable(String name, String value) {
		v.put(name, Variable.fromString(value));
	}

	public static void setVariable(String name, int number, String value) throws VariableNotArrayException {
		Variable arr = v.get(name);
		if (arr != null && !(arr instanceof ArrayVariable)) {
			throw new VariableNotArrayException(name);
		}
		if (arr == null) {
			arr = new ArrayVariable();
			v.put(name, arr);
		}
		((ArrayVariable) arr).setVar(number, Variable.fromString(value));
	}
}
