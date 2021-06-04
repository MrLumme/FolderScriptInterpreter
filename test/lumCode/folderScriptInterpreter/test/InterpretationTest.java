package lumCode.folderScriptInterpreter.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;

public class InterpretationTest {
	private static final File tf1 = new File("resources/interpretationTest/test1");

	@Test
	void interpretationTest() throws InterpreterException {
		// Pre-test
		assertTrue(new File(tf1.getAbsolutePath() + "/dok1.rtf").exists());
		assertTrue(new File(tf1.getAbsolutePath() + "/dok2.txt").exists());
		assertTrue(new File(tf1.getAbsolutePath() + "/dok3.xml").exists());

		// Test0 - Declare and print variable to screen
		Main.main(new String[] { "#var=45 o(#var,$)" });

		// Test1 - List filenames into a new text file
		File list = new File(tf1.getAbsolutePath() + "/list.txt");
		Main.main(new String[] { "i0(a[0]){o(n(i0)+\"\\n\",a[1])}", tf1.getAbsolutePath(), list.getAbsolutePath() });
		assertTrue(list.exists());
	}

}
