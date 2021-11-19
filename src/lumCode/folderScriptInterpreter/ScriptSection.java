package lumCode.folderScriptInterpreter;

import java.util.ArrayList;
import java.util.List;

import lumCode.folderScriptInterpreter.exceptions.ScriptErrorException;

public class ScriptSection {
	private final String head;
	private final List<ScriptSection> input;
	private final List<ScriptSection> command;

	public ScriptSection(String script) throws ScriptErrorException {
		int in = script.indexOf(BracketType.INPUT.begin);

		if (in == -1 || script.startsWith("#")) {
			this.head = script;
			this.input = null;
			this.command = null;
		} else {
			this.head = script.substring(0, in);
			this.input = new ArrayList<ScriptSection>();
			List<String> i = Utilities.charSplitter(Utilities.extractBracket(script, in), ',');
			for (String s : i) {
				input.add(new ScriptSection(s));
			}

			int com = script.indexOf(BracketType.COMMAND.begin);
			if (com == -1) {
				this.command = null;
			} else {
				this.command = new ArrayList<ScriptSection>();
				List<String> c = Utilities.charSplitter(Utilities.extractBracket(script, com), ',');
				for (String s : c) {
					command.add(new ScriptSection(s));
				}
			}
		}
	}

	public String getHead() {
		return head;
	}

	public List<ScriptSection> getInput() {
		return input;
	}

	public List<ScriptSection> getCommand() {
		return command;
	}

	@Override
	public String toString() {
		return head + (input == null ? ""
				: "(" + input.toString() + ")" + (command == null ? "" : "{" + command.toString() + "}"));
	}
}
