package lumCode.folderScriptInterpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lumCode.folderScriptInterpreter.exceptions.BreakDownException;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.ScriptErrorException;
import lumCode.folderScriptInterpreter.exceptions.notFoundExceptions.VariableNotFoundException;
import lumCode.folderScriptInterpreter.handlers.BooleanNode;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.ResultantNode;
import lumCode.folderScriptInterpreter.handlers.arithmetic.Arithmetic;
import lumCode.folderScriptInterpreter.handlers.arithmetic.ArithmeticType;
import lumCode.folderScriptInterpreter.handlers.command.BooleanCommand;
import lumCode.folderScriptInterpreter.handlers.command.Command;
import lumCode.folderScriptInterpreter.handlers.command.CommandType;
import lumCode.folderScriptInterpreter.handlers.conditional.Conditional;
import lumCode.folderScriptInterpreter.handlers.declaring.Declaration;
import lumCode.folderScriptInterpreter.handlers.declaring.DeclarationType;
import lumCode.folderScriptInterpreter.handlers.iteration.Break;
import lumCode.folderScriptInterpreter.handlers.iteration.Iteration;
import lumCode.folderScriptInterpreter.handlers.logic.Logic;
import lumCode.folderScriptInterpreter.handlers.logic.LogicType;
import lumCode.folderScriptInterpreter.handlers.test.Test;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.lookUps.EnvironmentLookUp;
import lumCode.folderScriptInterpreter.variables.lookUps.EnvironmentType;
import lumCode.folderScriptInterpreter.variables.lookUps.VariableLookUp;

public class ScriptBuilder {

	public static List<Node> buildNodeTree(String script) throws InterpreterException {
		List<String> sec = Utilities.commandSplitter(script);

		ArrayList<Node> out = new ArrayList<Node>();

		for (String s : sec) {
			Node n = breakDownScript(s);
			if (n != null) {
				out.add(n);
			}
		}

		return out;
	}

	private static Node breakDownScript(String script) throws InterpreterException {
		char c = script.charAt(0);

		if (Utilities.isLogicExpression(script)) {
			// Logic logic
			List<String> logic = Utilities.splitLogically(script);

			c = logic.get(logic.size() - 1).charAt(0);
			LogicType type = LogicType.fromChar(c);

			String right = "";
			for (int i = 1; i < logic.size() - 1; i++) {
				right += logic.get(i) + c;
			}
			right = right.substring(0, right.length() - 1);

			return breakDownLogic(logic.get(0), type, right);
		} else if (Utilities.isArithmeticExpression(script)) {
			// Arithmetic logic
			List<String> arith = Utilities.splitArithmetically(script);

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
			String query = Utilities.extractBracket(script, script.indexOf('('));
			return breakDownConditional(query, Utilities.extractBracket(script, script.lastIndexOf('}')));
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
						Utilities.commandSplitter(Utilities.extractBracket(script, script.lastIndexOf('}'))));
			} else {
				return breakDownVariable(script);
			}
		} else if (c == 'b') {
			// Break logic
			return new Break();
		} else if (c == 't') {
			// Test logic
			if (script.charAt(1) != '{') {
				throw new ScriptErrorException(script, "Test command 't' requires a command segment ('{' and '}').");
			}
			return breakDownTest(Utilities.commandSplitter(Utilities.extractBracket(script, 1)));
		} else if (c == '.') {
			// environment logic
			if (script.substring(1).matches("[0-9]{1,}")) {
				return new EnvironmentLookUp(EnvironmentType.get(Integer.parseInt(script.substring(1))));
			} else {
				throw new ScriptErrorException(script, "Environment variable missing id.");
			}
		} else if (c == '\"' && script.endsWith("\"")) {
			// Text data logic
			script = script.substring(1, script.length() - 1);
			return Variable.fromString(script);
		} else if (script.startsWith("$") || script.matches("^-{0,1}[0-9]{1,}$")) {
			// Other data logic
			return Variable.fromString(script);
		} else {
			throw new ScriptErrorException(script,
					"Could not break down script. Perhaps it has a syntax error or missing parenthesis?");
		}
	}

	private static Node breakDownConditional(String query, String script) throws InterpreterException {
		Node node = breakDownScript(query);
		if (!(node instanceof BooleanNode)) {
			throw new ScriptErrorException(script,
					"Syntax error; conditional ('?') must have an input capable of giving a boolean result (true / false).");
		}

		List<String> spl = Utilities.charSplitter(script, ':');

		if (spl.size() > 2) {
			throw new BreakDownException(script, '?', "Conditional contains more than one else separator (':').");
		}

		List<String> splT = Utilities.commandSplitter(spl.get(0));
		List<Node> sct = new ArrayList<Node>();
		for (String s : splT) {
			sct.add(breakDownScript(s));
		}

		if (spl.size() > 1) {
			List<String> splF = Utilities.commandSplitter(spl.get(1));
			List<Node> scf = new ArrayList<Node>();
			for (String s : splF) {
				scf.add(breakDownScript(s));
			}
			return new Conditional((BooleanNode) node, sct, scf);
		} else {
			return new Conditional((BooleanNode) node, sct);
		}
	}

	private static Arithmetic breakDownArithmetic(String left, ArithmeticType type, String right)
			throws InterpreterException {
		Node ln = breakDownScript(left);
		Node rn = breakDownScript(right);

		if (ln instanceof ResultantNode && rn instanceof ResultantNode) {
			return new Arithmetic((ResultantNode) ln, type, (ResultantNode) rn);
		} else {
			throw new ScriptErrorException(left + type.getChar() + right,
					"Arithmetic function requires result giving inputs only");
		}
	}

	private static Node breakDownLogic(String left, LogicType type, String right) throws InterpreterException {
		Node ln = breakDownScript(left);
		Node rn = breakDownScript(right);

		if (ln instanceof ResultantNode && rn instanceof ResultantNode) {
			return new Logic((ResultantNode) ln, type, (ResultantNode) rn);
		} else {
			throw new ScriptErrorException(left + type.getChar() + right,
					"Logic function requires result giving inputs only");
		}
	}

	private static Node breakDownTest(List<String> script) throws InterpreterException {
		List<Node> com = new ArrayList<Node>();
		for (String s : script) {
			com.add(breakDownScript(s));
		}
		return new Test(com);
	}

	private static Iteration breakDownIteration(int n, String iterant, List<String> script)
			throws InterpreterException {
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

	private static Node breakDownVariable(String name) throws VariableNotFoundException {
		if (Variable.exists(name)) {
			return new VariableLookUp(name);
		} else {
			throw new VariableNotFoundException(name);
		}
	}

	private static Declaration breakDownDeclaration(String name, DeclarationType d, String script)
			throws InterpreterException {
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

	private static Command breakDownCommand(CommandType c, List<String> inputs) throws InterpreterException {
		List<Node> ins = new ArrayList<Node>();
		for (String input : inputs) {
			ins.add(breakDownScript(input));
		}
		if (c.isBooleanOutput()) {
			return new BooleanCommand(c, ins);
		}
		return new Command(c, ins);
	}
}
