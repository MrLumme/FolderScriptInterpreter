package lumCode.folderScriptInterpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import lumCode.folderScriptInterpreter.exceptions.ScriptErrorException;
import lumCode.folderScriptInterpreter.exceptions.arrayExceptions.DisallowedDataInArrayException;
import lumCode.folderScriptInterpreter.exceptions.typeExceptions.UnsupportedVariableTypeException;
import lumCode.folderScriptInterpreter.handlers.arithmetic.ArithmeticType;
import lumCode.folderScriptInterpreter.handlers.command.CommandType;
import lumCode.folderScriptInterpreter.handlers.declaring.DeclarationType;
import lumCode.folderScriptInterpreter.handlers.logic.LogicType;
import lumCode.folderScriptInterpreter.variables.ArrayVariable;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.TextVariable;
import lumCode.folderScriptInterpreter.variables.Variable;
import lumCode.folderScriptInterpreter.variables.VariableType;
import lumCode.folderScriptInterpreter.variables.comparator.VariableSizeComparator;
import lumCode.folderScriptInterpreter.variables.comparator.VariableTypeComparator;
import lumCode.folderScriptInterpreter.variables.comparator.VariableValueComparator;

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

	public static String getMD5(String txt) {
		return DigestUtils.md5Hex(txt);
	}

	public static List<File> listFolder(File fol, int depth, boolean dirs) {
		ArrayList<File> out = new ArrayList<File>();
		for (File f : fol.listFiles()) {
			if (f.isDirectory() && depth > 0) {
				out.addAll(listFolder(f, depth - 1, dirs));
			}

			if (f.isFile() && !dirs) {
				out.add(f);
			} else if (f.isDirectory() && dirs) {
				out.add(f);
			}
		}
		return out;
	}

	public static ArrayVariable sortArray(ArrayVariable array, int mode) throws DisallowedDataInArrayException {
		ArrayVariable out = new ArrayVariable();
		ArrayList<Variable> l = new ArrayList<Variable>(array.getAll().values());
		if (mode == 0) {
			Collections.sort(l, new VariableSizeComparator<Variable>());
			Collections.sort(l, new VariableTypeComparator());
		} else if (mode == 1) {
			Collections.sort(l, new VariableValueComparator<Variable>());
		} else if (mode == -1) {
			Collections.reverse(l);
		}
		for (Variable e : l) {
			out.setNextVar(e);
		}
		return out;
	}

	public static long varSize(Variable var) throws UnsupportedVariableTypeException, FileNotFoundException {
		if (var.type == VariableType.NUMBER) {
			return Math.abs(((NumberVariable) var).getVar());
		} else if (var.type == VariableType.ARRAY) {
			return ((ArrayVariable) var).getAll().size();
		} else if (var.type == VariableType.FOLDER) {
			return folderSize(((FolderVariable) var).getVar());
		} else if (var.type == VariableType.FILE) {
			return fileSize(((FileVariable) var).getVar());
		} else if (var.type == VariableType.TEXT) {
			return ((TextVariable) var).getVar().length();
		} else if (var.type == VariableType.SPECIAL) {
			return Main.script.length();
		}
		throw new UnsupportedVariableTypeException(var.type);
	}

	public static long fileSize(File file) throws FileNotFoundException {
		if (!file.exists()) {
			throw new FileNotFoundException("File '" + file.getAbsolutePath() + "' does not exist.");
		}
		return file.length();
	}

	public static long folderSize(File file) throws FileNotFoundException {
		if (!file.exists()) {
			throw new FileNotFoundException("Folder '" + file.getAbsolutePath() + "' does not exist.");
		}
		return file.list().length;
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

		String noText = "";
		boolean inString = false;
		for (int i = 0; i < rem.length(); i++) {
			if (rem.charAt(i) == '\"' && (i < 1 || rem.charAt(i - 1) != '\\')) {
				inString = !inString;
			} else if (!inString) {
				if (rem.charAt(i) == ' ' || rem.charAt(i) == '\n' || rem.charAt(i) == '\t' || rem.charAt(i) == '\r'
						|| rem.charAt(i) == '\f') {
					rem = rem.substring(0, i) + rem.substring(i + 1);
					i--;
				} else {
					noText += rem.charAt(i);
				}
			}
		}
		if (inString == true) {
			throw new ScriptErrorException(script, "The script contains uneven amount of quotes ('\"').");
		}

		int inputB = 0;
		int inputE = 0;
		int comB = 0;
		int comE = 0;
		int arrayB = 0;
		int arrayE = 0;

		for (int i = 0; i < noText.length(); i++) {
			if (noText.charAt(i) == BracketType.INPUT.begin) {
				inputB++;
			} else if (noText.charAt(i) == BracketType.INPUT.end) {
				inputE++;
			} else if (noText.charAt(i) == BracketType.ARRAY.begin) {
				arrayB++;
			} else if (noText.charAt(i) == BracketType.ARRAY.end) {
				arrayE++;
			} else if (noText.charAt(i) == BracketType.COMMAND.begin) {
				comB++;
			} else if (noText.charAt(i) == BracketType.COMMAND.end) {
				comE++;
			}
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

			if (string.charAt(i) == '\"' && (i < 1 || string.charAt(i - 1) != '\\')) {
				inString = !inString;
			} else if (!inString
					&& (string.charAt(i) == BracketType.INPUT.begin || string.charAt(i) == BracketType.COMMAND.begin)) {
				String br = extractBracket(string, i);
				cur += br;
				i += br.length();
			}
		}
		out.add(cur);

		return out;
	}

	public static String collapseEscapeCharacters(String var) {
		String out = "";
		for (int i = 0; i < var.length(); i++) {
			if (var.charAt(i) == '\\' && var.length() > i) {
				if (var.charAt(i + 1) == '{') {
					out += '{';
				} else if (var.charAt(i + 1) == '}') {
					out += '}';
				} else if (var.charAt(i + 1) == 't') {
					out += '\t';
				} else if (var.charAt(i + 1) == 'r') {
					out += '\r';
				} else if (var.charAt(i + 1) == 'n') {
					out += '\n';
				} else if (var.charAt(i + 1) == 'f') {
					out += '\f';
				} else if (var.charAt(i + 1) == '\'') {
					out += '\'';
				} else {
					out += '\\';
					if (var.charAt(i + 1) != '\\') {
						i -= 1;
					}
				}
				i += 1;
			} else {
				out += var.charAt(i);
			}
		}
		return out;
	}

	public static List<String> commandSplitter(String script) throws ScriptErrorException {
		ArrayList<String> out = new ArrayList<String>();

		String cur = "";
		boolean inText = false;
		for (int i = 0; i < script.length(); i++) {
			if (!inText && !cur.isEmpty()) {
				if (i + 1 < script.length() && isCharFolderScriptOperator(script.charAt(i))
						&& (script.charAt(i + 1) == BracketType.INPUT.begin
								|| script.charAt(i + 1) == BracketType.COMMAND.begin)
						&& (i > 1 && (!LogicType.valid(script.charAt(i - 1))
								&& !ArithmeticType.valid(script.charAt(i - 1))))) {
					out.add(cur);
					cur = "";
				} else if (script.charAt(i) == 'i') {
					for (int j = i + 1; j < script.length(); j++) {
						if (script.charAt(j) < 48 || script.charAt(j) > 57) {
							if (script.charAt(j) == BracketType.INPUT.begin) {
								out.add(cur);
								cur = "";
							}
							break;
						}
					}
				} else if (script.charAt(i) == 'b') {
					out.add(cur);
					cur = "";
				} else if (i > 0 && script.charAt(i) == '#' && !LogicType.valid(script.charAt(i - 1))
						&& !ArithmeticType.valid(script.charAt(i - 1))) {
					out.add(cur);
					cur = "";
				}
			}

			cur += script.charAt(i);

			if (script.charAt(i) == '\"' && (i < 1 || script.charAt(i - 1) != '\\')) {
				inText = !inText;
				if (!inText) {
					out.add(cur);
					cur = "";
				}
			} else if (!inText && (script.charAt(i) == BracketType.INPUT.begin
					|| script.charAt(i) == BracketType.COMMAND.begin || script.charAt(i) == BracketType.ARRAY.begin)) {
				String br = extractBracket(script, i);
				cur += br;
				i += br.length();
			}
		}
		out.add(cur);

		return out;

	}

	private static boolean isCharFolderScriptOperator(char c) {
		if (CommandType.valid(c) || ArithmeticType.valid(c) || LogicType.valid(c) || c == '?' || c == 'i' || c == 't'
				|| c == 'b' || c == '#' || c == 'a') {
			return true;
		}
		return false;
	}

	public static boolean charOutsideBrackets(String string, char c) {
		boolean inString = false;
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == '\"' && (i < 1 || string.charAt(i - 1) != '\\')) {
				inString = !inString;
			} else if (!inString && (string.charAt(i) == BracketType.INPUT.begin
					|| string.charAt(i) == BracketType.COMMAND.begin || string.charAt(i) == BracketType.ARRAY.begin)) {
				String br = extractBracket(string, i);
				i += br.length() + 1;
			}

			if (!inString && string.charAt(i) == c) {
				return true;
			}
		}
		return false;
	}

	public static boolean isArithmeticExpression(String string) {
		boolean inString = false;
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == '\"' && (i < 1 || string.charAt(i - 1) != '\\')) {
				inString = !inString;
			} else if (!inString && (string.charAt(i) == BracketType.INPUT.begin
					|| string.charAt(i) == BracketType.COMMAND.begin || string.charAt(i) == BracketType.ARRAY.begin)) {
				String br = extractBracket(string, i);
				i += br.length() + 1;
			}
			if (!inString && i > 0 && DeclarationType.valid(string.charAt(i))) {
				return false;
			} else if (!inString && i > 0 && (string.charAt(i) == '+' || string.charAt(i) == '-'
					|| string.charAt(i) == '/' || string.charAt(i) == '*' || string.charAt(i) == '%')) {
				return true;
			}
		}
		return false;
	}

	public static boolean isLogicExpression(String string) {
		boolean inString = false;
		if (string.charAt(0) == '#') {
			return false;
		}
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == '\"' && (i < 1 || string.charAt(i - 1) != '\\')) {
				inString = !inString;
			} else if (!inString && (string.charAt(i) == BracketType.INPUT.begin
					|| string.charAt(i) == BracketType.COMMAND.begin || string.charAt(i) == BracketType.ARRAY.begin)) {
				String br = extractBracket(string, i);
				i += br.length() + 1;
			}

			if (!inString && (string.charAt(i) == '!' || string.charAt(i) == '=' || string.charAt(i) == '<'
					|| string.charAt(i) == '>')) {
				return true;
			}
		}
		return false;
	}

	public static List<String> splitArithmetically(String script) throws ScriptErrorException {
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

	public static List<String> splitLogically(String script) throws ScriptErrorException {
		List<String> and = Utilities.charSplitter(script, '&');
		if (and.size() > 1) {
			and.add("&");
			return and;
		}
		List<String> or = Utilities.charSplitter(script, '|');
		if (or.size() > 1) {
			or.add("|");
			return or;
		}
		if (script.charAt(0) == '!') {
			script = "1" + script;
		}
		List<String> not = Utilities.charSplitter(script, '!');
		if (not.size() > 1) {
			not.add("!");
			return not;
		}
		List<String> equ = Utilities.charSplitter(script, '=');
		if (equ.size() > 1) {
			equ.add("=");
			return equ;
		}
		List<String> gre = Utilities.charSplitter(script, '>');
		if (gre.size() > 1) {
			gre.add(">");
			return gre;
		}
		List<String> les = Utilities.charSplitter(script, '<');
		if (les.size() > 1) {
			les.add("<");
			return les;
		}
		return null;
	}

	public static List<String> cleanArguments(Variable var) {
		ArrayList<String> out = new ArrayList<String>();

		switch (var.type) {
		case ARRAY:
			for (Variable v : ((ArrayVariable) var).getAll().values()) {
				out.addAll(cleanArguments(v));
			}
			break;
		case FILE:
		case FOLDER:
		case NUMBER:
		case SPECIAL:
			out.add(var.toString());
			break;
		default:
		case TEXT:
			String str = var.toString();
			String cur = "";
			boolean inString = false;
			for (int i = 0; i < str.length(); i++) {
				if (!inString && str.charAt(i) == ' ') {
					out.add(cur);
					cur = "";
				}
				cur += str.charAt(i);
				if (str.charAt(i) == '\"' && (i < 1 || str.charAt(i - 1) != '\\')) {
					inString = !inString;
				}
			}
			out.add(cur);
			break;
		}

		return out;
	}
}
