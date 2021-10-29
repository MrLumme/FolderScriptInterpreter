package lumCode.folderScriptInterpreter;

public enum BracketType {
	INPUT('(', ')'), ARRAY('[', ']'), COMMAND('{', '}');

	public final char begin;
	public final char end;

	BracketType(char begin, char end) {
		this.begin = begin;
		this.end = end;
	}

	public static BracketType getType(char bracket) {
		for (BracketType c : values()) {
			if (c.begin == bracket || c.end == bracket) {
				return c;
			}
		}
		return null;
	}
}
