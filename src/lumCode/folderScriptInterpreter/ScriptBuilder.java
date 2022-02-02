package lumCode.folderScriptInterpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lumCode.folderScriptInterpreter.exceptions.BreakDownException;
import lumCode.folderScriptInterpreter.exceptions.CommandErrorException;
import lumCode.folderScriptInterpreter.exceptions.IncorrentParameterAmountException;
import lumCode.folderScriptInterpreter.exceptions.ScriptErrorException;
import lumCode.folderScriptInterpreter.exceptions.arrayExceptions.NotArrayException;
import lumCode.folderScriptInterpreter.exceptions.nameNotFoundExceptions.VariableNameNotFoundException;
import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedTypeException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.ResultantNode;
import lumCode.folderScriptInterpreter.handlers.arithmetic.Arithmetic;
import lumCode.folderScriptInterpreter.handlers.arithmetic.ArithmeticType;
import lumCode.folderScriptInterpreter.handlers.command.Command;
import lumCode.folderScriptInterpreter.handlers.command.CommandType;
import lumCode.folderScriptInterpreter.handlers.declaring.Declaration;
import lumCode.folderScriptInterpreter.handlers.declaring.DeclarationType;
import lumCode.folderScriptInterpreter.handlers.iteration.BreakNode;
import lumCode.folderScriptInterpreter.handlers.iteration.Iteration;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.Variable;

public class ScriptBuilder {

	public static List<Node> buildNodeTree(String script)
			throws ScriptErrorException, VariableNameNotFoundException, BreakDownException, UnsupportedTypeException,
			IncorrentParameterAmountException, CommandErrorException, NotArrayException {
		List<String> sec = Utilities.charSplitter(script, ',');

		ArrayList<Node> out = new ArrayList<Node>();

		for (String s : sec) {
			out.add(breakDownScript(s));
		}

		return out;
	}

//	public static List<Node> buildNodeTree(ScriptSection script)
//			throws ScriptErrorException, BreakDownException, VariableNameNotFoundException {
//		validateScriptSection(script);
//
//		List<Node> out = breakDownScript(script);
//		return out;
//	}

	private static Node breakDownScript(String script)
			throws ScriptErrorException, BreakDownException, VariableNameNotFoundException, UnsupportedTypeException,
			IncorrentParameterAmountException, CommandErrorException, NotArrayException {
		char c = script.charAt(0);

		if (Utilities.isArithmeticExpression(script)) {
			// Arithmetic logic
			List<String> arith = splitArithmetically(script);

			c = arith.get(arith.size() - 1).charAt(0);
			ArithmeticType type = ArithmeticType.fromChar(c);

			String right = "";
			for (int i = 1; i < arith.size() - 1; i++) {
				right += arith.get(i) + c;
			}
			right = right.substring(0, right.length() - 1);

			return breakDownArithmetic(arith.get(0), type, right);
		} else if (c == 'a') {
			// Argument logic
			return breakDownVariable(script);
		} else if (CommandType.valid(c)) {
			// Command logic
			if (script.charAt(1) != '(') {
				throw new ScriptErrorException(script, "Parenthesis should follow after command, but is missing.");
			}
			String inputs = Utilities.extractBracket(script, 1);

			String sub = script.substring(3 + inputs.length());
			if (sub.contains("{") || sub.contains("}")) {
				throw new ScriptErrorException(script, "Commands can not have action type brackets ('{', '}').");
			}

			return breakDownCommand(CommandType.fromChar(c), Utilities.charSplitter(inputs, ','));
		} else if (c == '#') {
			// Variable and Declaration logic
			Pattern p = Pattern.compile("^#[A-Za-z0-9_]{1,}(?=(\\[[0-9]{1,}\\]){0,1})");
			Matcher m = p.matcher(script);
			String name;
			if (m.find()) {
				name = m.group(0);
				String arr = m.group(1);
				if (arr != null) {
					name += arr;
				}
			} else {
				throw new ScriptErrorException(script, "Syntax error; could not interpret variable ('#').");
			}

			if (script.length() > name.length()) {
				char d = script.charAt(name.length());
				if (DeclarationType.valid(d)) {
					return breakDownDeclaration(name, DeclarationType.fromChar(d), script.substring(name.length() + 1));
				} else {
					throw new ScriptErrorException(script, "Invalid character in variable name or declaration.");
				}
			}
			return breakDownVariable(name);
		} else if (c == '?') {
			// Conditional logic
		} else if (c == 'i') {
			// Iteration logic
			int n;
			int ip = script.indexOf('(');
			if (ip != -1) {
				try {
					n = Integer.parseInt(script.substring(1, ip));
					Main.i.put(n, new NumberVariable(0));
				} catch (NumberFormatException e) {
					throw new ScriptErrorException(script, "Syntax error; iteration ('i') malformed.");
				}
				String iterant = Utilities.extractBracket(script, ip);
				return breakDownIteration(n, iterant,
						Utilities.charSplitter(Utilities.extractBracket(script, script.lastIndexOf('}')), ','));
			} else {
				return breakDownVariable(script);
			}
		} else if (c == 'b') {
			// Break logic
			return new BreakNode();
		} else if (c == 'h') {
			// Help logic
			// TODO
		} else if (script.startsWith("\"") || script.startsWith("$") || script.matches("^-{0,1}[0-9]{1,}$")) {
			// Data logic
			if (script.startsWith("\"") && script.endsWith("\"")) {
				script = script.substring(1, script.length() - 1);
			}
			return Variable.fromString(script);
		}

		return null;
	}

	private static Arithmetic breakDownArithmetic(String left, ArithmeticType type, String right)
			throws VariableNameNotFoundException, ScriptErrorException, BreakDownException, UnsupportedTypeException,
			IncorrentParameterAmountException, CommandErrorException, NotArrayException {
		Node ln = breakDownScript(left);
		Node rn = breakDownScript(right);

		if (ln instanceof ResultantNode && rn instanceof ResultantNode) {
			return new Arithmetic((ResultantNode) ln, type, (ResultantNode) rn);
		} else {
			throw new ScriptErrorException(left + type.getChar() + right,
					"Arithmetic function requires result giving inputs only");
		}
	}

	private static List<String> splitArithmetically(String script) throws ScriptErrorException {
		List<String> plu = Utilities.charSplitter(script, '+');
		if (plu.size() > 1) {
			plu.add("+");
			return plu;
		}
		List<String> min = Utilities.charSplitter(script, '-');
		if (min.size() > 1) {
			min.add("-");
			return min;
		}
		List<String> mul = Utilities.charSplitter(script, '*');
		if (mul.size() > 1) {
			mul.add("*");
			return mul;
		}
		List<String> div = Utilities.charSplitter(script, '/');
		if (div.size() > 1) {
			div.add("/");
			return div;
		}
		List<String> mod = Utilities.charSplitter(script, '%');
		if (mod.size() > 1) {
			mod.add("%");
			return mod;
		}
		return null;
	}

	private static Iteration breakDownIteration(int n, String iterant, List<String> script)
			throws ScriptErrorException, VariableNameNotFoundException, BreakDownException, UnsupportedTypeException,
			IncorrentParameterAmountException, CommandErrorException, NotArrayException {
		Node node = breakDownScript(iterant);
		if (!(node instanceof ResultantNode)) {
			throw new ScriptErrorException(script.toString(),
					"Syntax error; iteration ('i') must have an input capable of giving a result.");
		}
		ResultantNode var = (ResultantNode) node;

		List<Node> com = new ArrayList<Node>();
		for (String s : script) {
			com.add(breakDownScript(s));
		}
		return new Iteration(n, var, com);
	}

	private static Node breakDownVariable(String name) throws VariableNameNotFoundException {
		if (Variable.exists(name)) {
			return new VariableLookUp(name);
		} else {
			throw new VariableNameNotFoundException(name);
		}
	}

	private static Declaration breakDownDeclaration(String name, DeclarationType d, String script)
			throws ScriptErrorException, VariableNameNotFoundException, BreakDownException, UnsupportedTypeException,
			IncorrentParameterAmountException, CommandErrorException, NotArrayException {
		String res = script;

		Variable value = null;
		if (res.matches("([0-9]{1,}|\\$|\".{0,}\")")) {
			if (res.startsWith("\"")) {
				res = res.substring(1, res.length() - 1);
			}
			value = Variable.fromString(res);
			return new Declaration(name, d, value);
		} else {
			Node node = breakDownScript(res);
			if (!(node instanceof ResultantNode)) {
				throw new ScriptErrorException(script.toString(),
						"Syntax error; declaration does not receive a resultant value.");
			}
			return new Declaration(name, d, node);
		}
	}

	private static Command breakDownCommand(CommandType c, List<String> inputs)
			throws VariableNameNotFoundException, ScriptErrorException, BreakDownException,
			IncorrentParameterAmountException, CommandErrorException, UnsupportedTypeException, NotArrayException {
		List<Node> ins = new ArrayList<Node>();
		for (String input : inputs) {
			ins.add(breakDownScript(input));
		}
		return new Command(c, ins);
	}
}
