package lumCode.folderScriptInterpreter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import lumCode.folderScriptInterpreter.exceptions.ScriptErrorException;

public class Utilities {

	public static String getMD5(File fil) {
		String md5;
		try {
			InputStream is = Files.newInputStream(Paths.get(fil.getAbsolutePath()));
			md5 = DigestUtils.md5Hex(is);
		} catch (IOException e) {
			// e.printStackTrace();
			md5 = "";
		}
		return md5;
	}

	public static List<File> listFiles(File dir, int level) {
		List<File> out = new ArrayList<File>();
		for (File f : dir.listFiles()) {
			if (f.isFile() || level == 0) {
				out.add(f);
			} else {
				out.addAll(listFiles(f, level - 1));
			}
		}
		return out;
	}

	public static String extractBracket(String str, int position) {
		char bracket = str.charAt(position);
		BracketType type = BracketType.getType(bracket);
		if (type == null) {
			return null;
		}

		boolean backward = false;
		if (type.end == bracket) {
			backward = true;
		}

		String out = "";
		int level = 0;
		if (!backward) {
			while (position < str.length() - 1) {
				position++;
				char c = str.charAt(position);
				if (level == 0 && c == type.end) {
					return out;
				} else {
					out += c;
					if (c == type.begin) {
						level++;
					} else if (c == type.end) {
						level--;
					}
				}
			}
		} else {
			while (position > 0) {
				position--;
				char c = str.charAt(position);
				if (level == 0 && str.charAt(position) == type.begin) {
					return out;
				} else {
					out = c + out;
					if (c == type.end) {
						level++;
					} else if (c == type.begin) {
						level--;
					}
				}
			}
		}
		return null;
	}

	public static String cleanAndValidateScript(String script) throws ScriptErrorException {
		String rem = "";
		String[] spl = script.split("\\^");
		if (spl.length % 2 == 0) {
			throw new ScriptErrorException(script, "The script contains an uneven amount of comment characters ('^').");
		}
		for (int i = 0; i < spl.length; i++) {
			if (i % 2 == 0) {
				rem += spl[i];
			}
		}

		int inputB = 0;
		int inputE = 0;
		int comB = 0;
		int comE = 0;
		int arrayB = 0;
		int arrayE = 0;

		boolean inString = false;
		for (int i = 0; i < rem.length(); i++) {
			if (rem.charAt(i) == '\"') {
				inString = !inString;
			} else if (!inString) {
				if (rem.charAt(i) == ' ' || rem.charAt(i) == '\n' || rem.charAt(i) == '\t' || rem.charAt(i) == '\r') {
					rem = rem.substring(0, i) + rem.substring(i + 1);
					i--;
				} else if (rem.charAt(i) == BracketType.INPUT.begin) {
					inputB++;
				} else if (rem.charAt(i) == BracketType.INPUT.end) {
					inputE++;
				} else if (rem.charAt(i) == BracketType.ARRAY.begin) {
					arrayB++;
				} else if (rem.charAt(i) == BracketType.ARRAY.end) {
					arrayE++;
				} else if (rem.charAt(i) == BracketType.COMMAND.begin) {
					comB++;
				} else if (rem.charAt(i) == BracketType.COMMAND.end) {
					comE++;
				}
			}
		}
		if (inString == true) {
			throw new ScriptErrorException(script, "The script contains uneven amount of quotes ('\"').");
		}

		if (inputB != inputE) {
			throw new ScriptErrorException(script, "The script contains unfinished of input backets ('(', ')').");
		} else if (comB != comE) {
			throw new ScriptErrorException(script, "The script contains unfinished of command backets ('{', '}').");
		} else if (arrayB != arrayE) {
			throw new ScriptErrorException(script, "The script contains unfinished of array backets ('[', ']').");
		}

		return rem;
	}

	public static List<String> charSplitter(String string, char c) throws ScriptErrorException {
		ArrayList<String> out = new ArrayList<String>();

		if (string.charAt(string.length() - 1) == c) {
			throw new ScriptErrorException(string, "Section malformed; ending with splitter character ('" + c + "')");
		}

		String cur = "";
		boolean inString = false;
		for (int i = 0; i < string.length(); i++) {
			if (!inString && string.charAt(i) == c) {
				out.add(cur);
				cur = "";
				i++;
			}

			cur += string.charAt(i);

			if (string.charAt(i) == '\"') {
				inString = !inString;
			} else if (!inString && (string.charAt(i) == BracketType.INPUT.begin
					|| string.charAt(i) == BracketType.COMMAND.begin || string.charAt(i) == BracketType.ARRAY.begin)) {
				String br = extractBracket(string, i);
				cur += br;
				i += br.length() + 1;
			}
		}
		out.add(cur);

		return out;
	}
}
