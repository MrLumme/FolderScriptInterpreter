package lumCode.folderScriptInterpreter.handlers.iteration;

public enum IterationType {
	TEXT_ITERATION, INTEGER_ITERATION, FOLDER_ITERATION, LIST_ITERATION, SPECIAL_ITERATION
	// Special iteration will repeat until manually broken out of. This can be used
	// to construct while-like loops.
}
