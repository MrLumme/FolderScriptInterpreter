package lumCode.folderScriptInterpreter.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
			// Do nothing
		}
		assertFalse(done);

		done = false;
		try {
			Utilities.cleanAndValidateScript("this ^ command should not");
			done = true;
		} catch (ScriptErrorException e) {
			// Do nothing
		}
		assertFalse(done);

		done = false;
		try {
			Utilities.cleanAndValidateScript("this ( command should not");
			done = true;
		} catch (ScriptErrorException e) {
			// Do nothing
		}
		assertFalse(done);

		done = false;
		try {
			Utilities.cleanAndValidateScript("this } command should not");
			done = true;
		} catch (ScriptErrorException e) {
			// Do nothing
		}
		assertFalse(done);

		done = false;
		try {
			Utilities.cleanAndValidateScript("this [ command should not");
			done = true;
		} catch (ScriptErrorException e) {
			// Do nothing
		}
		assertFalse(done);
	}

	@Test
	void charSplitterTest() throws ScriptErrorException {
		assertNotNull(Utilities.charSplitter(
				"this command \"even ( with , this } madness\", should be (even with, weird stuff, like this), approved",
				','));

		boolean done = false;
		try {
			Utilities.charSplitter("this, should, error,", ',');
			done = true;
		} catch (ScriptErrorException e) {
			// Do nothing
		}
		assertFalse(done);

		assertNotNull(Utilities.charSplitter("i0(l(a[0],0)){o(i0+\"\\r\",a[1]),o(i0+\"\\r\",$)}", ','));
	}

	@Test
	void charOutsideBracketsTest() {
		assertTrue(Utilities.charOutsideBrackets("n(i0)+\"\\n\",a[1]", '+'));

		assertTrue(Utilities.charOutsideBrackets("n(+)+(-)", '+'));
		assertFalse(Utilities.charOutsideBrackets("n(+)-(-)", '+'));
	}

}
