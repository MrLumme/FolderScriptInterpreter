package lumCode.folderScriptInterpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lumCode.folderScriptInterpreter.exceptions.BreakDownException;
import lumCode.folderScriptInterpreter.exceptions.CommandErrorException;
import lumCode.folderScriptInterpreter.exceptions.IncorrentParameterAmountException;
import lumCode.folderScriptInterpreter.exceptions.ScriptErrorException;
import lumCode.folderScriptInterpreter.exceptions.UnsupportedTypeException;
import lumCode.folderScriptInterpreter.exceptions.VariableNameNotFoundException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.ResultantNode;
import lumCode.folderScriptInterpreter.handlers.command.Command;
import lumCode.folderScriptInterpreter.handlers.command.CommandType;
import lumCode.folderScriptInterpreter.handlers.declaring.Declaration;
import lumCode.folderScriptInterpreter.handlers.declaring.DeclarationType;
import lumCode.folderScriptInterpreter.handlers.iteration.Iteration;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.Variable;

public class ScriptBuilder {

	public static List<Node> buildNodeTree(String script) throws ScriptErrorException, VariableNameNotFoundException,
			BreakDownException, UnsupportedTypeException, IncorrentParameterAmountException, CommandErrorException {
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
			IncorrentParameterAmountException, CommandErrorException {
		char c = script.charAt(0);

		if (c == 'a') {
			return breakDownVariable(script);
		} else if (CommandType.valid(c)) {
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
			Pattern p = Pattern.compile("^#[A-Za-z0-9_]{1,}(?=(\\[[0-9]{1,}\\]){0,1})");
			Matcher m = p.matcher(script);
			String name;
			if (m.find()) {
				name = m.group(0);
			} else {
				throw new ScriptErrorException(script, "Syntax error; could not interpret variable ('#').");
			}

			char d = script.charAt(name.length());
			if (DeclarationType.valid(d)) {
				return breakDownDeclaration(name, DeclarationType.fromChar(d),
						script.substring(script.charAt(name.length())));
			} else {
				return breakDownVariable(name);
			}
		} else if (c == '?') {
			// Conditional logic
		} else if (c == 'i') {
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
			// Break loop logic
		} else if (c == 'h') {
			// Help logic
		}

		return null;
	}

	private static Node breakDownIteration(int n, String iterant, List<String> script)
			throws ScriptErrorException, VariableNameNotFoundException, BreakDownException, UnsupportedTypeException,
			IncorrentParameterAmountException, CommandErrorException {
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

	private static Node breakDownDeclaration(String name, DeclarationType d, String script)
			throws ScriptErrorException, VariableNameNotFoundException, BreakDownException, UnsupportedTypeException,
			IncorrentParameterAmountException, CommandErrorException {
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

	private static Node breakDownCommand(CommandType c, List<String> inputs)
			throws VariableNameNotFoundException, ScriptErrorException, BreakDownException,
			IncorrentParameterAmountException, CommandErrorException, UnsupportedTypeException {
		List<Node> ins = new ArrayList<Node>();
		for (String input : inputs) {
			ins.add(breakDownScript(input));
		}
		return new Command(c, ins);
	}

	private static void validateScriptSection(ScriptSection script) throws ScriptErrorException {
		if (script.getHead().matches("^[^A-Za-z#?^\"]")) {
			throw new ScriptErrorException(script.toString(),
					"The start of the script contains invalid or non-functional instructions.");
		} else if (script.getHead().charAt(0) == 'i'
				&& (script.getHead().charAt(1) < 48 || script.getHead().charAt(1) > 57)) {
			throw new ScriptErrorException(script.toString(),
					"The start of the script contains iterator but no numeric designation.");
		}
	}
}
