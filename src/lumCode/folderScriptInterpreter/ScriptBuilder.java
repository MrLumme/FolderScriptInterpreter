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
import lumCode.folderScriptInterpreter.variables.Variable;

public class ScriptBuilder {
	public static List<Node> buildNodeTree(ScriptSection script)
			throws ScriptErrorException, BreakDownException, VariableNameNotFoundException {
		validateScriptSection(script);

		List<Node> out = breakDownScript(script);

		return out;
	}

	private static List<Node> breakDownScript(ScriptSection script)
			throws ScriptErrorException, BreakDownException, VariableNameNotFoundException {
		List<Node> out = new ArrayList<Node>();
		char c = script.getHead().charAt(0);

		if (CommandType.valid(c)) {
			// Command logic
			if (script.getCommand() != null) {
				throw new ScriptErrorException(script.toString(),
						"Syntax error; command type operations can not contain command brackets ('{', '}').");
			}
			CommandType t;
			try {
				t = CommandType.fromChar(c);
			} catch (UnsupportedTypeException e) {
				throw new BreakDownException(script.getHead(), c, e.getMessage());
			}
			List<Node> ins = new ArrayList<Node>();
			for (ScriptSection input : script.getInput()) {
				ins.addAll(breakDownScript(input));
			}
			try {
				out.add(new Command(t, ins));
			} catch (IncorrentParameterAmountException | CommandErrorException e) {
				throw new BreakDownException(script.toString(), c, e.getMessage());
			}
		} else if (c == '#') {
			// Variable name
			Pattern p = Pattern.compile("^#[A-Za-z0-9_]{1,}(?=(\\[[0-9]{1,}\\]){0,1})");
			Matcher m = p.matcher(script.getHead());
			String name;
			if (m.find()) {
				name = m.group(0);
			} else {
				throw new ScriptErrorException(script.toString(),
						"Syntax error; could not interpret variable with name ('#').");
			}

			char d = script.getHead().charAt(name.length());
			if (d == '=' || d == '!') {
				// Declaration logic
				DeclarationType t;
				try {
					t = DeclarationType.fromChar(d);
				} catch (UnsupportedTypeException e) {
					throw new BreakDownException(script.getHead(), d, e.getMessage());
				}
				String res = script.getHead().substring(name.length() + 1);

				Variable value = null;
				if (res.matches("([0-9]{1,}|\\$|\".{0,}\")")) {
					if (res.startsWith("\"")) {
						res = res.substring(1, res.length() - 1);
					}
					value = Variable.fromString(res);
					out.add(new Declaration(name, t, value));
				} else {
					ScriptSection scr = new ScriptSection(res);
					Node node = breakDownScript(scr).get(0);
					if (!(node instanceof ResultantNode)) {
						throw new ScriptErrorException(script.toString(),
								"Syntax error; declaration must be capable of setting a value.");
					}
					out.add(new Declaration(name, t, node));
				}
			} else {
				// Variable lookup
				if (Variable.exists(name)) {
					out.add(new VariableLookUp(name));
				} else {
					throw new VariableNameNotFoundException(name);
				}
			}
		} else if (c == '?') {
			// Conditional logic
		} else if (c == 'i') {
			// Iteration logic
			int num = Integer.parseInt(script.getHead().substring(1));

			if (script.getInput().size() != 1) {
				throw new ScriptErrorException(script.toString(),
						"Syntax error; iteration ('i') must have exactly one input.");
			}
			Node node = breakDownScript(script.getInput().get(0)).get(0);
			if (!(node instanceof ResultantNode)) {
				throw new ScriptErrorException(script.toString(),
						"Syntax error; iteration ('i') must have an input capable of giving a result.");
			}
			ResultantNode var = (ResultantNode) node;

			List<Node> com = new ArrayList<Node>();
			for (ScriptSection s : script.getCommand()) {
				com.addAll(breakDownScript(s));
			}
			out.add(new Iteration(num, var, com));
		} else if (c == 'b') {
			// Break loop logic
		} else if (c == 'h') {
			// Help logic
		}

		return out;
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
