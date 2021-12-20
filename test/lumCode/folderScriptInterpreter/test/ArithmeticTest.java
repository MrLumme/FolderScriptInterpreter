package lumCode.folderScriptInterpreter.test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.arithmetic.Arithmetic;
import lumCode.folderScriptInterpreter.handlers.arithmetic.ArithmeticType;
import lumCode.folderScriptInterpreter.variables.ArrayVariable;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.SpecialVariable;
import lumCode.folderScriptInterpreter.variables.TextVariable;

public class ArithmeticTest {

	@Test
	void preTest() throws InterpreterException {
		// Pre-test
	}

	@Test
	void additionTest() throws InterpreterException {
		FileVariable fil = new FileVariable(new File("C:/temp/docs/dok1.rtf"));
		FileVariable fil2 = new FileVariable(new File("C:/temp/code/dok4.txt"));
		FolderVariable fol = new FolderVariable(new File("C:/temp/code"));
		TextVariable txt = new TextVariable("test TEXT");
		NumberVariable num = new NumberVariable(14);
		SpecialVariable spe = new SpecialVariable();
		ArrayVariable arr = new ArrayVariable();
		arr.setVar(0, new FileVariable(new File("C:/temp/docs/dok1.rtf")));
		arr.setVar(1, new FolderVariable(new File("C:/temp/code")));
		arr.setVar(2, new TextVariable("test TEXT"));
		arr.setVar(3, new NumberVariable(15));
		arr.setVar(4, new SpecialVariable());
		ArrayVariable arr2 = new ArrayVariable();
		arr2.setVar(0, new FileVariable(new File("C:/temp/docs/dok1.rtf")));
		arr2.setVar(1, new FolderVariable(new File("C:/temp/code")));
		arr2.setVar(2, new TextVariable("test TEXT"));
		arr2.setVar(3, new NumberVariable(15));
		arr2.setVar(4, new SpecialVariable());

		// File
		Arithmetic f1 = new Arithmetic(fil, ArithmeticType.ADDITION, fil2);
		Arithmetic f2 = new Arithmetic(fil, ArithmeticType.ADDITION, fol);
		Arithmetic f3 = new Arithmetic(fil, ArithmeticType.ADDITION, txt);
		Arithmetic f4 = new Arithmetic(fil, ArithmeticType.ADDITION, num);
		Arithmetic f5 = new Arithmetic(fil, ArithmeticType.ADDITION, arr);
		Arithmetic f6 = new Arithmetic(fil, ArithmeticType.ADDITION, spe);

		f1.action();
		assertTrue(((FileVariable) f1.result()).getVar().getAbsolutePath().equals("C:\\temp\\docs\\dok4.txt"));
		f2.action();
		assertTrue(((FileVariable) f2.result()).getVar().getAbsolutePath().equals("C:\\temp\\code\\dok1.rtf"));
		f3.action();
		assertTrue(((FileVariable) f3.result()).getVar().getAbsolutePath().equals("C:\\temp\\docs\\dok1test TEXT.rtf"));
		f4.action();
		assertTrue(((FileVariable) f4.result()).getVar().getAbsolutePath().equals("C:\\temp\\docs\\dok114.rtf"));

		boolean fr5 = false;
		try {
			f5.action();
			fr5 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(fr5);

		boolean fr6 = false;
		try {
			f6.action();
			fr6 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(fr6);

		// Folder
		Arithmetic d1 = new Arithmetic(fol, ArithmeticType.ADDITION, fil);
		Arithmetic d2 = new Arithmetic(fol, ArithmeticType.ADDITION, fol);
		Arithmetic d3 = new Arithmetic(fol, ArithmeticType.ADDITION, txt);
		Arithmetic d4 = new Arithmetic(fol, ArithmeticType.ADDITION, num);
		Arithmetic d5 = new Arithmetic(fol, ArithmeticType.ADDITION, arr);
		Arithmetic d6 = new Arithmetic(fol, ArithmeticType.ADDITION, spe);

		d1.action();
		assertTrue(((FileVariable) d1.result()).getVar().getAbsolutePath().equals("C:\\temp\\code\\dok1.rtf"));
		d2.action();
		assertTrue(((FolderVariable) d2.result()).getVar().getAbsolutePath().equals("C:\\temp\\code\\code"));
		d3.action();
		assertTrue(((FolderVariable) d3.result()).getVar().getAbsolutePath().equals("C:\\temp\\codetest TEXT"));
		d4.action();
		assertTrue(((FolderVariable) d4.result()).getVar().getAbsolutePath().equals("C:\\temp\\code14"));

		boolean dr5 = false;
		try {
			d5.action();
			dr5 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(dr5);

		boolean dr6 = false;
		try {
			d6.action();
			dr6 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(dr6);

		// Text
		Arithmetic t1 = new Arithmetic(txt, ArithmeticType.ADDITION, fil);
		Arithmetic t2 = new Arithmetic(txt, ArithmeticType.ADDITION, fol);
		Arithmetic t3 = new Arithmetic(txt, ArithmeticType.ADDITION, txt);
		Arithmetic t4 = new Arithmetic(txt, ArithmeticType.ADDITION, num);
		Arithmetic t5 = new Arithmetic(txt, ArithmeticType.ADDITION, arr);
		Arithmetic t6 = new Arithmetic(txt, ArithmeticType.ADDITION, spe);

		t1.action();
		assertTrue(((TextVariable) t1.result()).getVar().equals("test TEXTC:\\temp\\docs\\dok1.rtf"));
		t2.action();
		assertTrue(((TextVariable) t2.result()).getVar().equals("test TEXTC:\\temp\\code"));
		t3.action();
		assertTrue(((TextVariable) t3.result()).getVar().equals("test TEXTtest TEXT"));
		t4.action();
		assertTrue(((TextVariable) t4.result()).getVar().equals("test TEXT14"));
		t5.action();
		assertTrue(((TextVariable) t5.result()).getVar()
				.equals("test TEXTC:\\temp\\docs\\dok1.rtf, C:\\temp\\code, test TEXT, 15, $"));
		t6.action();
		assertTrue(((TextVariable) t6.result()).getVar().equals("test TEXT$"));

		// Number
		Arithmetic n1 = new Arithmetic(num, ArithmeticType.ADDITION, fil);
		Arithmetic n2 = new Arithmetic(num, ArithmeticType.ADDITION, fol);
		Arithmetic n3 = new Arithmetic(num, ArithmeticType.ADDITION, txt);
		Arithmetic n4 = new Arithmetic(num, ArithmeticType.ADDITION, num);
		Arithmetic n5 = new Arithmetic(num, ArithmeticType.ADDITION, arr);
		Arithmetic n6 = new Arithmetic(num, ArithmeticType.ADDITION, spe);

		n1.action();
		assertTrue(((FileVariable) n1.result()).getVar().getAbsolutePath().equals("C:\\temp\\docs\\14dok1.rtf"));
		n2.action();
		assertTrue(((FolderVariable) n2.result()).getVar().getAbsolutePath().equals("C:\\temp\\14code"));
		n3.action();
		assertTrue(((TextVariable) n3.result()).getVar().equals("14test TEXT"));
		n4.action();
		assertTrue(((NumberVariable) n4.result()).getVar() == 28);

		boolean nr5 = false;
		try {
			n5.action();
			nr5 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(nr5);

		boolean nr6 = false;
		try {
			n6.action();
			nr6 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(nr6);

		// Array
		Arithmetic a1 = new Arithmetic(arr, ArithmeticType.ADDITION, fil);
		Arithmetic a2 = new Arithmetic(arr, ArithmeticType.ADDITION, fol);
		Arithmetic a3 = new Arithmetic(arr, ArithmeticType.ADDITION, txt);
		Arithmetic a4 = new Arithmetic(arr, ArithmeticType.ADDITION, num);
		Arithmetic a5 = new Arithmetic(arr, ArithmeticType.ADDITION, arr2);
		Arithmetic a6 = new Arithmetic(arr, ArithmeticType.ADDITION, spe);

		a1.action();
		assertTrue(((FileVariable) ((ArrayVariable) a1.result()).getVar(5)).getVar().getAbsolutePath()
				.equals("C:\\temp\\docs\\dok1.rtf"));
		a2.action();
		assertTrue(((FolderVariable) ((ArrayVariable) a2.result()).getVar(6)).getVar().getAbsolutePath()
				.equals("C:\\temp\\code"));
		a3.action();
		assertTrue(((TextVariable) ((ArrayVariable) a3.result()).getVar(7)).getVar().equals("test TEXT"));
		a4.action();
		assertTrue(((NumberVariable) ((ArrayVariable) a4.result()).getVar(8)).getVar() == 14);
		a5.action();
		assertTrue(((FileVariable) ((ArrayVariable) a5.result()).getVar(9)).getVar().getAbsolutePath()
				.equals("C:\\temp\\docs\\dok1.rtf"));
		a6.action();
		assertTrue(((ArrayVariable) a6.result()).getVar(14) instanceof SpecialVariable);

		// Array
		Arithmetic s1 = new Arithmetic(arr, ArithmeticType.ADDITION, fil);
		Arithmetic s2 = new Arithmetic(arr, ArithmeticType.ADDITION, fol);
		Arithmetic s3 = new Arithmetic(arr, ArithmeticType.ADDITION, txt);
		Arithmetic s4 = new Arithmetic(arr, ArithmeticType.ADDITION, num);
		Arithmetic s5 = new Arithmetic(arr, ArithmeticType.ADDITION, arr2);
		Arithmetic s6 = new Arithmetic(arr, ArithmeticType.ADDITION, spe);

		s3.action();
		assertTrue(((TextVariable) s3.result()).getVar().equals("$test TEXT"));
		s6.action();
		assertTrue(s6.result() instanceof SpecialVariable);

		boolean sr1 = false;
		try {
			s1.action();
			sr1 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(sr1);

		boolean sr2 = false;
		try {
			s2.action();
			sr2 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(sr2);

		boolean sr4 = false;
		try {
			s4.action();
			sr4 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(sr4);

		boolean sr5 = false;
		try {
			s5.action();
			sr5 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(sr5);
	}

}
