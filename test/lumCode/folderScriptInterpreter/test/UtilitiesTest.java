package lumCode.folderScriptInterpreter.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;

import lumCode.folderScriptInterpreter.Utilities;
import lumCode.folderScriptInterpreter.exceptions.ScriptErrorException;

class UtilitiesTest {

	@Test
	void cleanAndValidateScriptTest() throws ScriptErrorException {
		assertNotNull(Utilities.cleanAndValidateScript("this command should be approved"));
		assertNotNull(Utilities.cleanAndValidateScript("this \"[\" command should"));
		assertNotNull(Utilities.cleanAndValidateScript("this ^[^ command should"));

		boolean done = false;
		try {
			Utilities.cleanAndValidateScript("this \" command should not");
			done = true;
		} catch (ScriptErrorException e) {
			e.printStackTrace();
		}
		assertFalse(done);

		done = false;
		try {
			Utilities.cleanAndValidateScript("this ^ command should not");
			done = true;
		} catch (ScriptErrorException e) {
			e.printStackTrace();
		}
		assertFalse(done);

		done = false;
		try {
			Utilities.cleanAndValidateScript("this ( command should not");
			done = true;
		} catch (ScriptErrorException e) {
			e.printStackTrace();
		}
		assertFalse(done);

		done = false;
		try {
			Utilities.cleanAndValidateScript("this } command should not");
			done = true;
		} catch (ScriptErrorException e) {
			e.printStackTrace();
		}
		assertFalse(done);

		done = false;
		try {
			Utilities.cleanAndValidateScript("this [ command should not");
			done = true;
		} catch (ScriptErrorException e) {
			e.printStackTrace();
		}
		assertFalse(done);
	}

	@Test
	void inputSplitterTest() throws ScriptErrorException {
		assertNotNull(Utilities.sectionSplitter(
				"this command \"even ( with , this } madness\", should be (even with, weird stuff, like this), approved"));

		boolean done = false;
		try {
			Utilities.sectionSplitter("this, should, error,");
			done = true;
		} catch (ScriptErrorException e) {
			e.printStackTrace();
		}
		assertFalse(done);
	}

}
