package lumCode.folderScriptInterpreter.test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;

public class OptionsTest {
	private static final File fs = new File("resources/optionsTest/");

	@BeforeEach
	void preTest() throws InterpreterException {
		// Pre-test
		File file = new File(fs.getAbsolutePath() + "/file.txt");
		file.delete();
		assertFalse(file.exists());
	}

	@Test
	void debugTest() throws InterpreterException {
		File file = new File(fs.getAbsolutePath() + "/file.txt");
		Main.main(new String[] { "w(\"This file is close to useless.\",a[0]) o(0,1) d(a[0])", file.getAbsolutePath() });
		assertTrue(file.exists());
	}

	@Test
	void writeToLogTest() throws InterpreterException {
		Main.logFile.delete();
		Main.main(new String[] { "o(4,1) w(\"This is written to log.\", $)" });
		assertTrue(Main.logFile.exists());
		assertTrue(Main.logFile.length() == 23);
	}
}
