package lumCode.folderScriptInterpreter.test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;

public class InterpretationTest {
	private static final File tf1 = new File("resources/interpretationTest/test2");

	@BeforeEach
	void preTest() throws InterpreterException {
		// Pre-test
		assertTrue(new File(tf1.getAbsolutePath() + "/dok1.rtf").exists());
		assertTrue(new File(tf1.getAbsolutePath() + "/dok2.txt").exists());
		assertTrue(new File(tf1.getAbsolutePath() + "/dok3.xml").exists());

		File list = new File(tf1.getAbsolutePath() + "/list.txt");
		list.delete();
		assertFalse(list.exists());

		clear();
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
		Main.main(new String[] { "#var=45,o(#var,$)" });
		System.out.println();
	}

	@Test
	void test2() throws InterpreterException {
		// Test2 - List filenames into a new text file
		System.out.println("Test2");
		File list = new File(tf1.getAbsolutePath() + "/list.txt");
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

	@Test
	void test4() throws InterpreterException {
		// Test4 - Output numbers from 0 to 100 but break the loop after first output
		System.out.println("Test4");
		clear();
		Main.main(new String[] { "i0(100){?(i0=50){b},o(i0+\", \",$)}" });
		System.out.println();
	}

	@Test
	void test5() throws InterpreterException {
		// Test5 - Output numbers from 0 to 100 that are divisible with both 5 and 3
		System.out.println("Test5");
		clear();
		Main.main(new String[] { "i0(100){?(i0%5=0&i0%3=0){o(i0+\": ding!\",$)}}" });
		System.out.println();
	}

}
