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
	private static final File tf10 = new File("resources/interpretationTest/test10");

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
		assertTrue(new File(tf10.getAbsolutePath() + "/script.fs").exists());

		Main.clear();

		System.out.println();
		System.out.println();
	}

	@Test
	void test01() throws InterpreterException {
		// Test1 - Declare and print variable to screen
		System.out.println("Test1");
		Main.main(new String[] { "#var=45w(#var,$)" });
	}

	@Test
	void test02() throws InterpreterException {
		// Test2 - List filenames into a new text file
		System.out.println("Test2");
		File list = new File(tf2.getAbsolutePath() + "/list.txt");
		Main.main(
				new String[] { "i0(a[0]){w(n(i0)+e(i0)+\"\n\",a[1])}", tf2.getAbsolutePath(), list.getAbsolutePath() });
		assertTrue(list.exists());
	}

	@Test
	void test03() throws InterpreterException {
		// Test3 - List an array of different variables
		System.out.println("Test3");
		Main.main(new String[] { "#var[0]=45 #var[1]=\"eeeeE\" #var[2]!1 i0(#var){w(i0+\"\n\",$)}" });
	}

	@Test
	void test04() throws InterpreterException {
		// Test4 - Output numbers from 0 to 100 but break the loop after 5th output
		System.out.println("Test4");
		Main.main(new String[] { "i0(100){?(i0=5){b} w(i0+\", \",$)}" });
	}

	@Test
	void test05() throws InterpreterException {
		// Test5 - Output numbers from 0 to 100 that are divisible with both 5 and 3
		System.out.println("Test5");
		Main.main(new String[] { "i0(100){?(i0%5=0 & i0%3=0){w(i0+\": ding! \",$)}}" });
		System.out.println();
	}

	@Test
	void test06() throws InterpreterException {
		// Test6 - Output numbers from 0 to 100 but also output ding just before 50
		System.out.println("Test6");
		Main.main(new String[] { "i0(100){?(i0=5=2+3){w(\"Ding! \",$)} w(i0+\", \",$)}" });
	}

	@Test
	void test07() throws InterpreterException {
		// Test7 - Output the result of 3 + 2 * 5 (Should output 13, not 25)
		System.out.println("Test7");
		Main.main(new String[] { "w(3+2*5,$)" });
	}

	@Test
	void test08() throws InterpreterException {
		// Test8 - Load a script file and execute it
		System.out.println("Test8");
		Main.main(new String[] { tf8.getAbsolutePath() + "/script.fs" });
	}

	@Test
	void test09() throws InterpreterException {
		// Test9 - Conditional if-else
		System.out.println("Test9");
		Main.main(new String[] {
				"i0(10){?(i0%3=0){w(i0+\" is divisible with 3.\",$):w(i0+\" is not divisible with 3.\",$)}}" });
	}

	@Test
	void test10() throws InterpreterException {
		// Test10 - None Comma Separation
		System.out.println("Test10");
		Main.main(new String[] { tf10.getAbsolutePath() + "/script.fs" });
	}

	@Test
	void test11() throws InterpreterException {
		// Test11 - Escape Character Test
		System.out.println("Test11");
		Main.main(new String[] {
				"w(\"\\{This\\} \\'script\\'\\ncontains\\r\\\"all\\\" the\\tescape \\ characters!\",$)" });
	}

	@Test
	void test12() throws InterpreterException {
		// Test12 - Array set with variable
		System.out.println("Test12");
		Main.main(new String[] { "#num=5#array[#num]=\"Tjek\"w(\"num:\"+#array[#num], $)" });
	}

	@Test
	void test13() throws InterpreterException {
		// Test13 - Modulo with logic test
		System.out.println("Test13");
		Main.main(new String[] { "?(4%7=3){w(\"Succes!\",$):w(\"Failed :(\",$)}" });
	}

	@Test
	void test14() throws InterpreterException {
		// Test14 - Logic with variable
		System.out.println("Test14");
		Main.main(new String[] { "#num=5?(#num>3){w(\"Succes!\",$):w(\"Failed :(\",$)}" });
	}

//	@Test
//	void test11() throws InterpreterException {
//		// Test11 - Test command result as conditional query
//		System.out.println("Test11");
//		Main.main(new String[] { "?(){w(\"OK\",$}" });
//	}

}
