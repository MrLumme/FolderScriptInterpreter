package lumCode.folderScriptInterpreter.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;

public class InterpretationTest {
	private static final File tf1 = new File("resources/interpretationTest/test2");

	@Test
	void preTest() throws InterpreterException {
		// Pre-test
		assertTrue(new File(tf1.getAbsolutePath() + "/dok1.rtf").exists());
		assertTrue(new File(tf1.getAbsolutePath() + "/dok2.txt").exists());
		assertTrue(new File(tf1.getAbsolutePath() + "/dok3.xml").exists());
	}

	private void clear() {
		Main.a.clear();
		Main.i.clear();
		Main.v.clear();
		Main.nodes.clear();
	}

	@Test
	void test1() throws InterpreterException {
		// Test1 - Declare and print variable to screen
		System.out.println("Test1");
		clear();
		Main.main(new String[] { "#var=45,o(#var,$)" });
		System.out.println();
	}

	@Test
	void test2() throws InterpreterException {
		// Test2 - List filenames into a new text file
		System.out.println("Test2");
		File list = new File(tf1.getAbsolutePath() + "/list.txt");
		if (list.exists()) {
			list.delete();
		}
		clear();
		Main.main(
				new String[] { "i0(a[0]){o(n(i0)+e(i0)+\"\n\",a[1])}", tf1.getAbsolutePath(), list.getAbsolutePath() });
		assertTrue(list.exists());
		System.out.println();
	}

	@Test
	void test3() throws InterpreterException {
		// Test3 - List an array of different variables
		System.out.println("Test3");
		clear();
		Main.main(new String[] { "#var[0]=45,#var[1]=\"eeeeE\",#var[2]!1,i0(#var){o(i0+\"\n\",$)}" });
		System.out.println();
	}

}
