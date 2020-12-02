package lumCode.folderScriptInterpreter.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.IntVariable;
import lumCode.folderScriptInterpreter.variables.SpecialVariable;
import lumCode.folderScriptInterpreter.variables.StringVariable;
import lumCode.folderScriptInterpreter.variables.Variable;

class VariableTest {

	@Test
	void interpretationTest() {
		assertTrue(Variable.fromString("-390586") instanceof IntVariable);
		assertTrue(Variable.fromString("fpotpk") instanceof StringVariable);
		assertTrue(Variable.fromString("c:/users/fsi/test.pdf") instanceof FileVariable);
		assertTrue(Variable.fromString("c:/users/fsi/") instanceof FolderVariable);
		assertTrue(Variable.fromString("$") instanceof SpecialVariable);
	}

}
