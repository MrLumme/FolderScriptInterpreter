package lumCode.folderScriptInterpreter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.arithmetic.Arithmetic;
import lumCode.folderScriptInterpreter.handlers.arithmetic.ArithmeticType;
import lumCode.folderScriptInterpreter.handlers.command.Command;
import lumCode.folderScriptInterpreter.handlers.command.CommandType;
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
		FileVariable fil = new FileVariable(new File("C:/dok1.rtf"));
		FolderVariable fol = new FolderVariable(new File("C:/"));
		TextVariable txt = new TextVariable("test TEXT");
		NumberVariable num = new NumberVariable(14);
		SpecialVariable spe = new SpecialVariable();
		ArrayVariable arr = new ArrayVariable();
		arr.setVar(0, new FileVariable(new File("C:/dok1.rtf")));
		arr.setVar(1, new FolderVariable(new File("C:/")));
		arr.setVar(2, new TextVariable("test TEXT"));
		arr.setVar(3, new NumberVariable(15));
		arr.setVar(4, new SpecialVariable());

		Arithmetic n1 = new Arithmetic(num, ArithmeticType.ADDITION, fil);
		Arithmetic n2 = new Arithmetic(num, ArithmeticType.ADDITION, fol);
		Arithmetic n3 = new Arithmetic(num, ArithmeticType.ADDITION, txt);
		Arithmetic n4 = new Arithmetic(num, ArithmeticType.ADDITION, num);
		Arithmetic n5 = new Arithmetic(num, ArithmeticType.ADDITION, spe);
		Arithmetic n6 = new Arithmetic(num, ArithmeticType.ADDITION, arr);

		n1.action();
		assertTrue(((TextVariable) n1.result()).getVar().equals("dok1"));
		n2.action();
		assertTrue(((TextVariable) n2.result()).getVar().equals("fileSet1"));

		boolean nr3 = false;
		try {
			n3.action();
			nr3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(nr3);

		boolean nr4 = false;
		try {
			n4.action();
			nr4 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(nr4);

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

		// Extension command
		Command e1 = new Command(CommandType.EXTENSION, l1);
		Command e2 = new Command(CommandType.EXTENSION, l2);
		Command e3 = new Command(CommandType.EXTENSION, l3);
		Command e4 = new Command(CommandType.EXTENSION, l4);
		Command e5 = new Command(CommandType.EXTENSION, l5);
		Command e6 = new Command(CommandType.EXTENSION, l6);

		e1.action();
		assertTrue(((TextVariable) e1.result()).getVar().equals(".rtf"));

		boolean er2 = false;
		try {
			e2.action();
			er2 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(er2);

		boolean er3 = false;
		try {
			e3.action();
			er3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(er3);

		boolean er4 = false;
		try {
			e4.action();
			er4 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(er4);

		boolean er5 = false;
		try {
			e5.action();
			er5 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(er5);

		boolean er6 = false;
		try {
			e6.action();
			er6 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(er6);

		// Parent command
		Command p1 = new Command(CommandType.PARENT, l1);
		Command p2 = new Command(CommandType.PARENT, l2);
		Command p3 = new Command(CommandType.PARENT, l3);
		Command p4 = new Command(CommandType.PARENT, l4);
		Command p5 = new Command(CommandType.PARENT, l5);
		Command p6 = new Command(CommandType.PARENT, l6);

		p1.action();
		assertTrue(((FolderVariable) p1.result()).getVar().getName().equals("fileSet1"));
		p2.action();
		assertTrue(((FolderVariable) p2.result()).getVar().getName().equals("commandTest"));

		boolean pr3 = false;
		try {
			p3.action();
			pr3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(pr3);

		boolean pr4 = false;
		try {
			p4.action();
			pr4 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(pr4);

		boolean pr5 = false;
		try {
			p5.action();
			pr5 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(pr5);

		boolean pr6 = false;
		try {
			p6.action();
			pr6 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(pr6);

		// isFile command
		Command f1 = new Command(CommandType.IS_FILE, l1);
		Command f2 = new Command(CommandType.IS_FILE, l2);
		Command f3 = new Command(CommandType.IS_FILE, l3);
		Command f4 = new Command(CommandType.IS_FILE, l4);
		Command f5 = new Command(CommandType.IS_FILE, l5);
		Command f6 = new Command(CommandType.IS_FILE, l6);

		f1.action();
		assertEquals(1, ((NumberVariable) f1.result()).getVar());
		f2.action();
		assertEquals(0, ((NumberVariable) f2.result()).getVar());

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

		// isAvailable command
		Command v1 = new Command(CommandType.IS_AVAILABLE, l1);
		Command v2 = new Command(CommandType.IS_AVAILABLE, l2);
		Command v3 = new Command(CommandType.IS_AVAILABLE, l3);
		Command v4 = new Command(CommandType.IS_AVAILABLE, l4);
		Command v5 = new Command(CommandType.IS_AVAILABLE, l5);
		Command v6 = new Command(CommandType.IS_AVAILABLE, l6);
		Command v7 = new Command(CommandType.IS_AVAILABLE, ln1);

		v1.action();
		assertEquals(1, ((NumberVariable) v1.result()).getVar());
		v2.action();
		assertEquals(1, ((NumberVariable) v2.result()).getVar());
		v7.action();
		assertEquals(0, ((NumberVariable) v7.result()).getVar());

		boolean vr3 = false;
		try {
			v3.action();
			vr3 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(vr3);

		boolean vr4 = false;
		try {
			v4.action();
			vr4 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(vr4);

		boolean vr5 = false;
		try {
			v5.action();
			vr5 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(vr5);

		boolean vr6 = false;
		try {
			v6.action();
			vr6 = true;
		} catch (InterpreterException e) {
			// Do nothing
		}
		assertFalse(vr6);
	}

}
