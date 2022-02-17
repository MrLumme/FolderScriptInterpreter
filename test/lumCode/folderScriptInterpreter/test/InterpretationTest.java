package lumCode.folderScriptInterpreter.test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;

public class InterpretationTest {
	private static final File tf2 = new File("resources/interpretationTest/test2");
	private static final File tf8 = new File("resources/interpretationTest/test8");

	@BeforeEach
	void preTest() throws InterpreterException {
		// Pre-test
		assertTrue(new File(tf2.getAbsolutePath() + "/dok1.rtf").exists());
		assertTrue(new File(tf2.getAbsolutePath() + "/dok2.txt").exists());
		assertTrue(new File(tf2.getAbsolutePath() + "/dok3.xml").exists());

		File list = new File(tf2.getAbsolutePath() + "/list.txt");
		list.delete();
		assertFalse(list.exists());

		assertTrue(new File(tf8.getAbsolutePath() + "/script.fs").exists());

		clear();
		System.out.println();
		System.out.println();
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
		Main.main(new String[] { "#var=45,w(#var,$)" });
	}

	@Test
	void test2() throws InterpreterException {
		// Test2 - List filenames into a new text file
		System.out.println("Test2");
		File list = new File(tf2.getAbsolutePath() + "/list.txt");
		Main.main(
				new String[] { "i0(a[0]){w(n(i0)+e(i0)+\"\n\",a[1])}", tf2.getAbsolutePath(), list.getAbsolutePath() });
		assertTrue(list.exists());
	}

	@Test
	void test3() throws InterpreterException {
		// Test3 - List an array of different variables
		System.out.println("Test3");
		Main.main(new String[] { "#var[0]=45,#var[1]=\"eeeeE\",#var[2]!1,i0(#var){w(i0+\"\n\",$)}" });
	}

	@Test
	void test4() throws InterpreterException {
		// Test4 - Output numbers from 0 to 100 but break the loop after 5th output
		System.out.println("Test4");
		Main.main(new String[] { "i0(100){?(i0=5){b},w(i0+\", \",$)}" });
	}

	@Test
	void test5() throws InterpreterException {
		// Test5 - Output numbers from 0 to 100 that are divisible with both 5 and 3
		System.out.println("Test5");
		Main.main(new String[] { "i0(100){?(i0%5=0 & i0%3=0){w(i0+\": ding! \",$)}}" });
		System.out.println();
	}

	@Test
	void test6() throws InterpreterException {
		// Test6 - Output numbers from 0 to 100 but also output ding just before 50
		System.out.println("Test6");
		Main.main(new String[] { "i0(100){?(i0=5=2+3){w(\"Ding! \",$)},w(i0+\", \",$)}" });
	}

	@Test
	void test7() throws InterpreterException {
		// Test7 - Output the result of 3 + 2 * 5 (Should output 13, not 25)
		System.out.println("Test7");
		Main.main(new String[] { "w(3+2*5,$)" });
	}

	@Test
	void test8() throws InterpreterException {
		// Test8 - Load a script file and execute it
		System.out.println("Test8");
		Main.main(new String[] { tf8.getAbsolutePath() + "/script.fs" });
	}

	@Test
	void test9() throws InterpreterException {
		// Test9 - Conditional if-else
		System.out.println("Test9");
		Main.main(new String[] {
				"i0(10){?(i0%3=0){w(i0+\" is divisible with 3.\",$):w(i0+\" is not divisible with 3.\",$)}}" });
	}

}
