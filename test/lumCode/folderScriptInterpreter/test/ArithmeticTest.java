package lumCode.folderScriptInterpreter.test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lumCode.folderScriptInterpreter.Main;
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
	FileVariable fil, fil2;
	FolderVariable fol, fol2;
	TextVariable txt, txt2, txt3, txt4;
	NumberVariable num, num2;
	SpecialVariable spe;
	ArrayVariable arr, arr2;

	@BeforeEach
	void preTest() throws InterpreterException {
		fil = new FileVariable(new File("C:/temp/docs/dok1.rtf"));
		fil2 = new FileVariable(new File("C:/temp/code/dok4.txt"));
		fol = new FolderVariable(new File("C:/temp/code"));
		fol2 = new FolderVariable(new File("C:/temp"));
		txt = new TextVariable("test TEXT");
		txt2 = new TextVariable("dok");
		txt3 = new TextVariable("ode");
		txt4 = new TextVariable("t T");
		num = new NumberVariable(14);
		num2 = new NumberVariable(2);
		spe = SpecialVariable.getInstance();
		arr = new ArrayVariable();
		arr2 = new ArrayVariable();

		arr.setVar(0, new FileVariable(new File("C:/temp/docs/dok1.rtf")));
		arr.setVar(1, new FolderVariable(new File("C:/temp/code")));
		arr.setVar(2, new TextVariable("test TEXT"));
		arr.setVar(3, new NumberVariable(14));
		arr.setVar(4, SpecialVariable.getInstance());

		arr2.setVar(0, new FileVariable(new File("C:/temp/docs/dok1.rtf")));
		arr2.setVar(1, new FolderVariable(new File("C:/temp/code")));
		arr2.setVar(2, new TextVariable("test TEXT"));
		arr2.setVar(3, new NumberVariable(15));
		arr2.setVar(4, SpecialVariable.getInstance());
	}

	@Test
	void additionTest() throws InterpreterException {
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
		f6.action();
		assertTrue(((FileVariable) f6.result()).getVar().getAbsolutePath()
				.equals(Main.tempDir.getAbsolutePath() + "\\dok1.rtf"));

		boolean fr5 = false;
		try {
			f5.action();
			fr5 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(fr5);

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
		d6.action();
		assertTrue(((FolderVariable) d6.result()).getVar().getAbsolutePath()
				.equals(Main.tempDir.getAbsolutePath() + "\\code"));

		boolean dr5 = false;
		try {
			d5.action();
			dr5 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(dr5);

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
				.equals("test TEXTC:\\temp\\docs\\dok1.rtf, C:\\temp\\code, test TEXT, 14, $"));
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
		Arithmetic s1 = new Arithmetic(spe, ArithmeticType.ADDITION, fil);
		Arithmetic s2 = new Arithmetic(spe, ArithmeticType.ADDITION, fol);
		Arithmetic s3 = new Arithmetic(spe, ArithmeticType.ADDITION, txt);
		Arithmetic s4 = new Arithmetic(spe, ArithmeticType.ADDITION, num);
		Arithmetic s5 = new Arithmetic(spe, ArithmeticType.ADDITION, arr);
		Arithmetic s6 = new Arithmetic(spe, ArithmeticType.ADDITION, spe);

		s1.action();
		assertTrue(((FileVariable) s1.result()).getVar().getAbsolutePath()
				.equals(Main.tempDir.getAbsolutePath() + "\\dok1.rtf"));
		s2.action();
		assertTrue(((FolderVariable) s2.result()).getVar().getAbsolutePath()
				.equals(Main.tempDir.getAbsolutePath() + "\\code"));
		s3.action();
		assertTrue(((TextVariable) s3.result()).getVar().equals("$test TEXT"));

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

		boolean sr6 = false;
		try {
			s6.action();
			sr6 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(sr6);
	}

	@Test
	void subtractionTest() throws InterpreterException {
		// File
		Arithmetic f1 = new Arithmetic(fil, ArithmeticType.SUBTRACTION, fil2);
		Arithmetic f2 = new Arithmetic(fil, ArithmeticType.SUBTRACTION, fol2);
		Arithmetic f3 = new Arithmetic(fil, ArithmeticType.SUBTRACTION, txt2);
		Arithmetic f4 = new Arithmetic(fil, ArithmeticType.SUBTRACTION, num2);
		Arithmetic f5 = new Arithmetic(fil, ArithmeticType.SUBTRACTION, arr);
		Arithmetic f6 = new Arithmetic(fil, ArithmeticType.SUBTRACTION, spe);

		f2.action();
		assertTrue(((FileVariable) f2.result()).getVar().getAbsolutePath().equals("C:\\docs\\dok1.rtf"));
		f3.action();
		assertTrue(((FileVariable) f3.result()).getVar().getAbsolutePath().equals("C:\\temp\\docs\\1.rtf"));
		f4.action();
		assertTrue(((FileVariable) f4.result()).getVar().getAbsolutePath().equals("C:\\temp\\docs\\do.rtf"));

		boolean fr1 = false;
		try {
			f1.action();
			fr1 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(fr1);

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
		Arithmetic d1 = new Arithmetic(fol, ArithmeticType.SUBTRACTION, fil);
		Arithmetic d2 = new Arithmetic(fol, ArithmeticType.SUBTRACTION, fol2);
		Arithmetic d3 = new Arithmetic(fol, ArithmeticType.SUBTRACTION, txt3);
		Arithmetic d4 = new Arithmetic(fol, ArithmeticType.SUBTRACTION, num2);
		Arithmetic d5 = new Arithmetic(fol, ArithmeticType.SUBTRACTION, arr);
		Arithmetic d6 = new Arithmetic(fol, ArithmeticType.SUBTRACTION, spe);

		d2.action();
		assertTrue(((FolderVariable) d2.result()).getVar().getAbsolutePath().equals("C:\\code"));
		d3.action();
		assertTrue(((FolderVariable) d3.result()).getVar().getAbsolutePath().equals("C:\\temp\\c"));
		d4.action();
		assertTrue(((FolderVariable) d4.result()).getVar().getAbsolutePath().equals("C:\\temp\\co"));

		boolean dr1 = false;
		try {
			d1.action();
			dr1 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(dr1);

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
		Arithmetic t1 = new Arithmetic(txt, ArithmeticType.SUBTRACTION, fil);
		Arithmetic t2 = new Arithmetic(txt, ArithmeticType.SUBTRACTION, fol);
		Arithmetic t3 = new Arithmetic(txt, ArithmeticType.SUBTRACTION, txt4);
		Arithmetic t4 = new Arithmetic(txt, ArithmeticType.SUBTRACTION, num2);
		Arithmetic t5 = new Arithmetic(txt, ArithmeticType.SUBTRACTION, arr);
		Arithmetic t6 = new Arithmetic(txt, ArithmeticType.SUBTRACTION, spe);

		t1.action();
		assertTrue(((TextVariable) t1.result()).getVar().equals("test TEXT"));
		t2.action();
		assertTrue(((TextVariable) t2.result()).getVar().equals("test TEXT"));
		t3.action();
		assertTrue(((TextVariable) t3.result()).getVar().equals("tesEXT"));
		t4.action();
		assertTrue(((TextVariable) t4.result()).getVar().equals("test TE"));
		t5.action();
		assertTrue(((TextVariable) t5.result()).getVar().equals(""));
		t6.action();
		assertTrue(((TextVariable) t6.result()).getVar().equals("test TEXT"));

		// Number
		Arithmetic n1 = new Arithmetic(num, ArithmeticType.SUBTRACTION, fil);
		Arithmetic n2 = new Arithmetic(num, ArithmeticType.SUBTRACTION, fol);
		Arithmetic n3 = new Arithmetic(num, ArithmeticType.SUBTRACTION, txt);
		Arithmetic n4 = new Arithmetic(num, ArithmeticType.SUBTRACTION, num2);
		Arithmetic n5 = new Arithmetic(num, ArithmeticType.SUBTRACTION, arr);
		Arithmetic n6 = new Arithmetic(num, ArithmeticType.SUBTRACTION, spe);

		n4.action();
		assertTrue(((NumberVariable) n4.result()).getVar() == 12);

		boolean nr1 = false;
		try {
			n1.action();
			nr1 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(nr1);

		boolean nr2 = false;
		try {
			n2.action();
			nr2 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(nr2);

		boolean nr3 = false;
		try {
			n3.action();
			nr3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(nr3);

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
		Arithmetic a1 = new Arithmetic(arr, ArithmeticType.SUBTRACTION, fil);
		Arithmetic a2 = new Arithmetic(arr, ArithmeticType.SUBTRACTION, fol);
		Arithmetic a3 = new Arithmetic(arr, ArithmeticType.SUBTRACTION, txt);
		Arithmetic a4 = new Arithmetic(arr, ArithmeticType.SUBTRACTION, num);
		Arithmetic a5 = new Arithmetic(arr, ArithmeticType.SUBTRACTION, arr2);
		Arithmetic a6 = new Arithmetic(arr, ArithmeticType.SUBTRACTION, spe);

		a1.action();
		assertFalse(((ArrayVariable) a1.result()).contains(fil));
		a2.action();
		assertFalse(((ArrayVariable) a2.result()).contains(fol));
		a3.action();
		assertFalse(((ArrayVariable) a3.result()).contains(txt));
		a4.action();
		assertFalse(((ArrayVariable) a4.result()).contains(num));
		a6.action();
		assertFalse(((ArrayVariable) a6.result()).contains(spe));
		a5.action();
		assertFalse(((ArrayVariable) a5.result()).contains(new NumberVariable(15)));

		// Array
		Arithmetic s1 = new Arithmetic(spe, ArithmeticType.SUBTRACTION, fil);
		Arithmetic s2 = new Arithmetic(spe, ArithmeticType.SUBTRACTION, fol);
		Arithmetic s3 = new Arithmetic(spe, ArithmeticType.SUBTRACTION, txt);
		Arithmetic s4 = new Arithmetic(spe, ArithmeticType.SUBTRACTION, num);
		Arithmetic s5 = new Arithmetic(spe, ArithmeticType.SUBTRACTION, arr);
		Arithmetic s6 = new Arithmetic(spe, ArithmeticType.SUBTRACTION, spe);

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

		boolean sr3 = false;
		try {
			s3.action();
			sr3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(sr3);

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

		boolean sr6 = false;
		try {
			s6.action();
			sr6 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(sr6);
	}

	@Test
	void multiplicationTest() throws InterpreterException {
		// File
		Arithmetic f1 = new Arithmetic(fil, ArithmeticType.MULTIPLICATION, fil2);
		Arithmetic f2 = new Arithmetic(fil, ArithmeticType.MULTIPLICATION, fol);
		Arithmetic f3 = new Arithmetic(fil, ArithmeticType.MULTIPLICATION, txt);
		Arithmetic f4 = new Arithmetic(fil, ArithmeticType.MULTIPLICATION, num);
		Arithmetic f5 = new Arithmetic(fil, ArithmeticType.MULTIPLICATION, arr);
		Arithmetic f6 = new Arithmetic(fil, ArithmeticType.MULTIPLICATION, spe);

		boolean fr1 = false;
		try {
			f1.action();
			fr1 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(fr1);

		boolean fr2 = false;
		try {
			f2.action();
			fr2 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(fr2);

		boolean fr3 = false;
		try {
			f3.action();
			fr3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(fr3);

		boolean fr4 = false;
		try {
			f4.action();
			fr4 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(fr4);

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
		Arithmetic d1 = new Arithmetic(fol, ArithmeticType.MULTIPLICATION, fil);
		Arithmetic d2 = new Arithmetic(fol, ArithmeticType.MULTIPLICATION, fol);
		Arithmetic d3 = new Arithmetic(fol, ArithmeticType.MULTIPLICATION, txt);
		Arithmetic d4 = new Arithmetic(fol, ArithmeticType.MULTIPLICATION, num);
		Arithmetic d5 = new Arithmetic(fol, ArithmeticType.MULTIPLICATION, arr);
		Arithmetic d6 = new Arithmetic(fol, ArithmeticType.MULTIPLICATION, spe);

		boolean dr1 = false;
		try {
			d1.action();
			dr1 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(dr1);

		boolean dr2 = false;
		try {
			d2.action();
			dr2 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(dr2);

		boolean dr3 = false;
		try {
			d3.action();
			dr3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(dr3);

		boolean dr4 = false;
		try {
			d4.action();
			dr4 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(dr4);

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
		Arithmetic t1 = new Arithmetic(txt, ArithmeticType.MULTIPLICATION, fil);
		Arithmetic t2 = new Arithmetic(txt, ArithmeticType.MULTIPLICATION, fol);
		Arithmetic t3 = new Arithmetic(txt, ArithmeticType.MULTIPLICATION, txt);
		Arithmetic t4 = new Arithmetic(txt, ArithmeticType.MULTIPLICATION, num);
		Arithmetic t5 = new Arithmetic(txt, ArithmeticType.MULTIPLICATION, arr);
		Arithmetic t6 = new Arithmetic(txt, ArithmeticType.MULTIPLICATION, spe);

		boolean tr1 = false;
		try {
			t1.action();
			tr1 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(tr1);

		boolean tr2 = false;
		try {
			t2.action();
			tr2 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(tr2);

		boolean tr3 = false;
		try {
			t3.action();
			tr3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(tr3);

		boolean tr4 = false;
		try {
			t4.action();
			tr4 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(tr4);

		boolean tr5 = false;
		try {
			t5.action();
			tr5 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(tr5);

		boolean tr6 = false;
		try {
			t6.action();
			tr6 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(tr6);

		// Number
		Arithmetic n1 = new Arithmetic(num, ArithmeticType.MULTIPLICATION, fil);
		Arithmetic n2 = new Arithmetic(num, ArithmeticType.MULTIPLICATION, fol);
		Arithmetic n3 = new Arithmetic(num, ArithmeticType.MULTIPLICATION, txt);
		Arithmetic n4 = new Arithmetic(num, ArithmeticType.MULTIPLICATION, num);
		Arithmetic n5 = new Arithmetic(num, ArithmeticType.MULTIPLICATION, arr);
		Arithmetic n6 = new Arithmetic(num, ArithmeticType.MULTIPLICATION, spe);

		n4.action();
		assertTrue(((NumberVariable) n4.result()).getVar() == 196);

		boolean nr1 = false;
		try {
			n1.action();
			nr1 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(nr1);

		boolean nr2 = false;
		try {
			n2.action();
			nr2 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(nr2);

		boolean nr3 = false;
		try {
			n3.action();
			nr3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(nr3);

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
		Arithmetic a1 = new Arithmetic(arr, ArithmeticType.MULTIPLICATION, fil);
		Arithmetic a2 = new Arithmetic(arr, ArithmeticType.MULTIPLICATION, fol);
		Arithmetic a3 = new Arithmetic(arr, ArithmeticType.MULTIPLICATION, txt);
		Arithmetic a4 = new Arithmetic(arr, ArithmeticType.MULTIPLICATION, num);
		Arithmetic a5 = new Arithmetic(arr, ArithmeticType.MULTIPLICATION, arr2);
		Arithmetic a6 = new Arithmetic(arr, ArithmeticType.MULTIPLICATION, spe);

		boolean ar1 = false;
		try {
			a1.action();
			ar1 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(ar1);

		boolean ar2 = false;
		try {
			a2.action();
			ar2 = true;
		} catch (InterpreterException e) { // Do nothing
		}
		assertFalse(ar2);

		boolean ar3 = false;
		try {
			a3.action();
			ar3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(ar3);

		boolean ar4 = false;
		try {
			a4.action();
			ar4 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(ar4);

		boolean ar5 = false;
		try {
			a5.action();
			ar5 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(ar5);

		boolean ar6 = false;
		try {
			a6.action();
			ar6 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(ar6);

		// Special
		Arithmetic s1 = new Arithmetic(spe, ArithmeticType.MULTIPLICATION, fil);
		Arithmetic s2 = new Arithmetic(spe, ArithmeticType.MULTIPLICATION, fol);
		Arithmetic s3 = new Arithmetic(spe, ArithmeticType.MULTIPLICATION, txt);
		Arithmetic s4 = new Arithmetic(spe, ArithmeticType.MULTIPLICATION, num);
		Arithmetic s5 = new Arithmetic(spe, ArithmeticType.MULTIPLICATION, arr);
		Arithmetic s6 = new Arithmetic(spe, ArithmeticType.MULTIPLICATION, spe);

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

		boolean sr3 = false;
		try {
			s3.action();
			sr3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(sr3);

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

		boolean sr6 = false;
		try {
			s6.action();
			sr6 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(sr6);
	}

	@Test
	void divisionTest() throws InterpreterException {
		// File
		Arithmetic f1 = new Arithmetic(fil, ArithmeticType.DIVISION, fil2);
		Arithmetic f2 = new Arithmetic(fil, ArithmeticType.DIVISION, fol);
		Arithmetic f3 = new Arithmetic(fil, ArithmeticType.DIVISION, txt);
		Arithmetic f4 = new Arithmetic(fil, ArithmeticType.DIVISION, num2);
		Arithmetic f5 = new Arithmetic(fil, ArithmeticType.DIVISION, arr);
		Arithmetic f6 = new Arithmetic(fil, ArithmeticType.DIVISION, spe);

		f4.action();
		assertTrue(((FolderVariable) f4.result()).getVar().getAbsolutePath().equals("C:\\temp"));

		boolean fr1 = false;
		try {
			f1.action();
			fr1 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(fr1);

		boolean fr2 = false;
		try {
			f2.action();
			fr2 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(fr2);

		boolean fr3 = false;
		try {
			f3.action();
			fr3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(fr3);

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
		Arithmetic d1 = new Arithmetic(fol, ArithmeticType.DIVISION, fil);
		Arithmetic d2 = new Arithmetic(fol, ArithmeticType.DIVISION, fol);
		Arithmetic d3 = new Arithmetic(fol, ArithmeticType.DIVISION, txt);
		Arithmetic d4 = new Arithmetic(fol, ArithmeticType.DIVISION, num2);
		Arithmetic d5 = new Arithmetic(fol, ArithmeticType.DIVISION, arr);
		Arithmetic d6 = new Arithmetic(fol, ArithmeticType.DIVISION, spe);

		d4.action();
		assertTrue(((FolderVariable) d4.result()).getVar().getAbsolutePath().equals("C:\\"));

		boolean dr1 = false;
		try {
			d1.action();
			dr1 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(dr1);

		boolean dr2 = false;
		try {
			d2.action();
			dr2 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(dr2);

		boolean dr3 = false;
		try {
			d3.action();
			dr3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(dr3);

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
		Arithmetic t1 = new Arithmetic(txt, ArithmeticType.DIVISION, fil);
		Arithmetic t2 = new Arithmetic(txt, ArithmeticType.DIVISION, fol);
		Arithmetic t3 = new Arithmetic(txt, ArithmeticType.DIVISION, txt);
		Arithmetic t4 = new Arithmetic(txt, ArithmeticType.DIVISION, num);
		Arithmetic t5 = new Arithmetic(txt, ArithmeticType.DIVISION, arr);
		Arithmetic t6 = new Arithmetic(txt, ArithmeticType.DIVISION, spe);

		boolean tr1 = false;
		try {
			t1.action();
			tr1 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(tr1);

		boolean tr2 = false;
		try {
			t2.action();
			tr2 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(tr2);

		boolean tr3 = false;
		try {
			t3.action();
			tr3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(tr3);

		boolean tr4 = false;
		try {
			t4.action();
			tr4 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(tr4);

		boolean tr5 = false;
		try {
			t5.action();
			tr5 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(tr5);

		boolean tr6 = false;
		try {
			t6.action();
			tr6 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(tr6);

		// Number
		Arithmetic n1 = new Arithmetic(num, ArithmeticType.DIVISION, fil);
		Arithmetic n2 = new Arithmetic(num, ArithmeticType.DIVISION, fol);
		Arithmetic n3 = new Arithmetic(num, ArithmeticType.DIVISION, txt);
		Arithmetic n4 = new Arithmetic(num, ArithmeticType.DIVISION, num2);
		Arithmetic n5 = new Arithmetic(num, ArithmeticType.DIVISION, arr);
		Arithmetic n6 = new Arithmetic(num, ArithmeticType.DIVISION, spe);

		n4.action();
		assertTrue(((NumberVariable) n4.result()).getVar() == 7);

		boolean nr1 = false;
		try {
			n1.action();
			nr1 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(nr1);

		boolean nr2 = false;
		try {
			n2.action();
			nr2 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(nr2);

		boolean nr3 = false;
		try {
			n3.action();
			nr3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(nr3);

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
		Arithmetic a1 = new Arithmetic(arr, ArithmeticType.DIVISION, fil);
		Arithmetic a2 = new Arithmetic(arr, ArithmeticType.DIVISION, fol);
		Arithmetic a3 = new Arithmetic(arr, ArithmeticType.DIVISION, txt);
		Arithmetic a4 = new Arithmetic(arr, ArithmeticType.DIVISION, num);
		Arithmetic a5 = new Arithmetic(arr, ArithmeticType.DIVISION, arr2);
		Arithmetic a6 = new Arithmetic(arr, ArithmeticType.DIVISION, spe);

		boolean ar1 = false;
		try {
			a1.action();
			ar1 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(ar1);

		boolean ar2 = false;
		try {
			a2.action();
			ar2 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(ar2);

		boolean ar3 = false;
		try {
			a3.action();
			ar3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(ar3);

		boolean ar4 = false;
		try {
			a4.action();
			ar4 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(ar4);

		boolean ar5 = false;
		try {
			a5.action();
			ar5 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(ar5);

		boolean ar6 = false;
		try {
			a6.action();
			ar6 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(ar6);

		// Array
		Arithmetic s1 = new Arithmetic(spe, ArithmeticType.DIVISION, fil);
		Arithmetic s2 = new Arithmetic(spe, ArithmeticType.DIVISION, fol);
		Arithmetic s3 = new Arithmetic(spe, ArithmeticType.DIVISION, txt);
		Arithmetic s4 = new Arithmetic(spe, ArithmeticType.DIVISION, num);
		Arithmetic s5 = new Arithmetic(spe, ArithmeticType.DIVISION, arr);
		Arithmetic s6 = new Arithmetic(spe, ArithmeticType.DIVISION, spe);

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

		boolean sr3 = false;
		try {
			s3.action();
			sr3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(sr3);

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

		boolean sr6 = false;
		try {
			s6.action();
			sr6 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(sr6);
	}

	@Test
	void moduloTest() throws InterpreterException {
		// File
		Arithmetic f1 = new Arithmetic(fil, ArithmeticType.MODULO, fil2);
		Arithmetic f2 = new Arithmetic(fil, ArithmeticType.MODULO, fol);
		Arithmetic f3 = new Arithmetic(fil, ArithmeticType.MODULO, txt);
		Arithmetic f4 = new Arithmetic(fil, ArithmeticType.MODULO, num);
		Arithmetic f5 = new Arithmetic(fil, ArithmeticType.MODULO, arr);
		Arithmetic f6 = new Arithmetic(fil, ArithmeticType.MODULO, spe);

		boolean fr1 = false;
		try {
			f1.action();
			fr1 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(fr1);

		boolean fr2 = false;
		try {
			f2.action();
			fr2 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(fr2);

		boolean fr3 = false;
		try {
			f3.action();
			fr3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(fr3);

		boolean fr4 = false;
		try {
			f4.action();
			fr4 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(fr4);

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
		Arithmetic d1 = new Arithmetic(fol, ArithmeticType.MODULO, fil);
		Arithmetic d2 = new Arithmetic(fol, ArithmeticType.MODULO, fol);
		Arithmetic d3 = new Arithmetic(fol, ArithmeticType.MODULO, txt);
		Arithmetic d4 = new Arithmetic(fol, ArithmeticType.MODULO, num);
		Arithmetic d5 = new Arithmetic(fol, ArithmeticType.MODULO, arr);
		Arithmetic d6 = new Arithmetic(fol, ArithmeticType.MODULO, spe);

		boolean dr1 = false;
		try {
			d1.action();
			dr1 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(dr1);

		boolean dr2 = false;
		try {
			d2.action();
			dr2 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(dr2);

		boolean dr3 = false;
		try {
			d3.action();
			dr3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(dr3);

		boolean dr4 = false;
		try {
			d4.action();
			dr4 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(dr4);

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
		Arithmetic t1 = new Arithmetic(txt, ArithmeticType.MODULO, fil);
		Arithmetic t2 = new Arithmetic(txt, ArithmeticType.MODULO, fol);
		Arithmetic t3 = new Arithmetic(txt, ArithmeticType.MODULO, txt);
		Arithmetic t4 = new Arithmetic(txt, ArithmeticType.MODULO, num);
		Arithmetic t5 = new Arithmetic(txt, ArithmeticType.MODULO, arr);
		Arithmetic t6 = new Arithmetic(txt, ArithmeticType.MODULO, spe);

		boolean tr1 = false;
		try {
			t1.action();
			tr1 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(tr1);

		boolean tr2 = false;
		try {
			t2.action();
			tr2 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(tr2);

		boolean tr3 = false;
		try {
			t3.action();
			tr3 = true;
		} catch (InterpreterException e) {
			// Do nothing

		}
		assertFalse(tr3);

		boolean tr4 = false;
		try {
			t4.action();
			tr4 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(tr4);

		boolean tr5 = false;
		try {
			t5.action();
			tr5 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(tr5);

		boolean tr6 = false;
		try {
			t6.action();
			tr6 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(tr6);

		// Number
		Arithmetic n1 = new Arithmetic(num, ArithmeticType.MODULO, fil);
		Arithmetic n2 = new Arithmetic(num, ArithmeticType.MODULO, fol);
		Arithmetic n3 = new Arithmetic(num, ArithmeticType.MODULO, txt);
		Arithmetic n4 = new Arithmetic(num, ArithmeticType.MODULO, num2);
		Arithmetic n5 = new Arithmetic(num, ArithmeticType.MODULO, arr);
		Arithmetic n6 = new Arithmetic(num, ArithmeticType.MODULO, spe);

		n4.action();
		assertTrue(((NumberVariable) n4.result()).getVar() == 0);

		boolean nr1 = false;
		try {
			n1.action();
			nr1 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(nr1);

		boolean nr2 = false;
		try {
			n2.action();
			nr2 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(nr2);

		boolean nr3 = false;
		try {
			n3.action();
			nr3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(nr3);

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
		Arithmetic a1 = new Arithmetic(arr, ArithmeticType.MODULO, fil);
		Arithmetic a2 = new Arithmetic(arr, ArithmeticType.MODULO, fol);
		Arithmetic a3 = new Arithmetic(arr, ArithmeticType.MODULO, txt);
		Arithmetic a4 = new Arithmetic(arr, ArithmeticType.MODULO, num);
		Arithmetic a5 = new Arithmetic(arr, ArithmeticType.MODULO, arr2);
		Arithmetic a6 = new Arithmetic(arr, ArithmeticType.MODULO, spe);

		boolean ar1 = false;
		try {
			a1.action();
			ar1 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(ar1);

		boolean ar2 = false;
		try {
			a2.action();
			ar2 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(ar2);

		boolean ar3 = false;
		try {
			a3.action();
			ar3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(ar3);

		boolean ar4 = false;
		try {
			a4.action();
			ar4 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(ar4);

		boolean ar5 = false;
		try {
			a5.action();
			ar5 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(ar5);

		boolean ar6 = false;
		try {
			a6.action();
			ar6 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(ar6);

		// Array
		Arithmetic s1 = new Arithmetic(spe, ArithmeticType.MODULO, fil);
		Arithmetic s2 = new Arithmetic(spe, ArithmeticType.MODULO, fol);
		Arithmetic s3 = new Arithmetic(spe, ArithmeticType.MODULO, txt);
		Arithmetic s4 = new Arithmetic(spe, ArithmeticType.MODULO, num);
		Arithmetic s5 = new Arithmetic(spe, ArithmeticType.MODULO, arr);
		Arithmetic s6 = new Arithmetic(spe, ArithmeticType.MODULO, spe);

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

		boolean sr3 = false;
		try {
			s3.action();
			sr3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(sr3);

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

		boolean sr6 = false;
		try {
			s6.action();
			sr6 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(sr6);
	}
}
