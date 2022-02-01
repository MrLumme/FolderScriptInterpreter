package lumCode.folderScriptInterpreter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.command.Command;
import lumCode.folderScriptInterpreter.handlers.command.CommandType;
import lumCode.folderScriptInterpreter.variables.ArrayVariable;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.SpecialVariable;
import lumCode.folderScriptInterpreter.variables.TextVariable;

public class CommandTest {
	private static final File fs1 = new File("resources/commandTest/fileSet1");
	private static final File fs2 = new File("resources/commandTest/fileSet2");

	@Before
	void preTest() throws InterpreterException {
		// Pre-test
		assertTrue(new File(fs1.getAbsolutePath() + "/dok1.rtf").exists());
		assertTrue(new File(fs1.getAbsolutePath() + "/dok2.txt").exists());
		assertTrue(new File(fs1.getAbsolutePath() + "/dok3.xml").exists());

		assertFalse(new File(fs2.getAbsolutePath() + "/dok1.txt").exists());
		assertFalse(new File(fs2.getAbsolutePath() + "/dok2.txt").exists());
		assertFalse(new File(fs2.getAbsolutePath() + "/folder/dok3.txt").exists());
	}

	@Test
	void infoTest() throws InterpreterException {
		ArrayList<Node> l1 = new ArrayList<Node>();
		l1.add(new FileVariable(new File(fs1.getAbsolutePath() + "/dok1.rtf")));
		ArrayList<Node> l2 = new ArrayList<Node>();
		l2.add(new FolderVariable(fs1));
		ArrayList<Node> l3 = new ArrayList<Node>();
		l3.add(new TextVariable("test TEXT"));
		ArrayList<Node> l4 = new ArrayList<Node>();
		l4.add(new NumberVariable(14));
		ArrayList<Node> l5 = new ArrayList<Node>();
		l5.add(SpecialVariable.getInstance());
		ArrayVariable arr = new ArrayVariable();
		arr.setVar(0, new FileVariable(new File(fs1.getAbsolutePath() + "/dok1.rtf")));
		arr.setVar(1, new FolderVariable(fs1));
		arr.setVar(2, new TextVariable("test TEXT"));
		arr.setVar(3, new NumberVariable(15));
		arr.setVar(4, SpecialVariable.getInstance());
		ArrayList<Node> l6 = new ArrayList<Node>();
		l6.add(arr);

		ArrayList<Node> ln1 = new ArrayList<Node>();
		ln1.add(new FileVariable(new File(fs1.getAbsolutePath() + "/notHere.rtf")));

		// Name command
		Command n1 = new Command(CommandType.NAME, l1);
		Command n2 = new Command(CommandType.NAME, l2);
		Command n3 = new Command(CommandType.NAME, l3);
		Command n4 = new Command(CommandType.NAME, l4);
		Command n5 = new Command(CommandType.NAME, l5);
		Command n6 = new Command(CommandType.NAME, l6);

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

	@Test
	void ioTest() throws InterpreterException, IOException {
		FileVariable doc1 = new FileVariable(new File(fs2.getAbsolutePath() + "/doc1.txt"));
		FileVariable doc2 = new FileVariable(new File(fs2.getAbsolutePath() + "/doc2.txt"));
		FolderVariable fol = new FolderVariable(new File(fs2.getAbsolutePath() + "/folder"));
		FileVariable doc3 = new FileVariable(new File(fol.getVar().getAbsolutePath() + "/" + doc1.getVar().getName()));
		TextVariable txt = new TextVariable("This is a piece of text which will be outputted into a file.");
		FileUtils.deleteDirectory(fol.getVar());
		doc1.getVar().delete();
		doc2.getVar().delete();
		doc3.getVar().delete();

		// Output
		ArrayList<Node> lo = new ArrayList<Node>();
		lo.add(txt);
		lo.add(doc1);
		Command o = new Command(CommandType.OUTPUT, lo);
		o.action();
		assertTrue(doc1.getVar().exists());

		ArrayList<Node> lo2 = new ArrayList<Node>();
		lo2.add(txt);
		lo2.add(SpecialVariable.getInstance());
		Command o2 = new Command(CommandType.OUTPUT, lo2);
		boolean or = false;
		try {
			o2.action();
			or = true;
		} catch (InterpreterException e) {
			e.printStackTrace();
		}
		assertTrue(or);

		// Copy
		ArrayList<Node> lc = new ArrayList<Node>();
		lc.add(doc1);
		lc.add(doc2);
		Command c = new Command(CommandType.COPY, lc);
		c.action();
		assertTrue(doc2.getVar().exists());

		// Move
		ArrayList<Node> lm = new ArrayList<Node>();
		lm.add(doc1);
		lm.add(fol);
		Command m = new Command(CommandType.MOVE, lm);
		m.action();
		assertTrue(doc3.getVar().exists());

		// List
		ArrayList<Node> ll = new ArrayList<Node>();
		ll.add(new FolderVariable(fs2));
		ll.add(new NumberVariable(0));
		Command l = new Command(CommandType.LIST, ll);
		l.action();
		assertTrue(((ArrayVariable) l.result()).getAll().size() == 2);

		ArrayList<Node> ll2 = new ArrayList<Node>();
		ll2.add(new FolderVariable(fs2));
		ll2.add(SpecialVariable.getInstance());
		Command l2 = new Command(CommandType.LIST, ll2);
		l2.action();
		assertTrue(((ArrayVariable) l2.result()).getAll().size() == 3);

		ArrayList<Node> ll3 = new ArrayList<Node>();
		ll3.add(new NumberVariable(12));
		ll3.add(SpecialVariable.getInstance());
		Command l3 = new Command(CommandType.LIST, ll3);
		l3.action();
		assertTrue(((ArrayVariable) l3.result()).getAll().size() == 12);
		assertTrue(((NumberVariable) ((ArrayVariable) l3.result()).getVar(0)).getVar() == 11);
		assertTrue(((NumberVariable) ((ArrayVariable) l3.result()).getVar(11)).getVar() == 0);

		ArrayList<Node> ll3a = new ArrayList<Node>();
		ll3a.add(new NumberVariable(-12));
		ll3a.add(SpecialVariable.getInstance());
		Command l3a = new Command(CommandType.LIST, ll3a);
		l3a.action();
		assertTrue(((ArrayVariable) l3a.result()).getAll().size() == 12);
		assertTrue(((NumberVariable) ((ArrayVariable) l3a.result()).getVar(0)).getVar() == 0);
		assertTrue(((NumberVariable) ((ArrayVariable) l3a.result()).getVar(11)).getVar() == -11);

		ArrayList<Node> ll4 = new ArrayList<Node>();
		ll4.add(new TextVariable("This is text!"));
		ll4.add(new NumberVariable(6));
		Command l4 = new Command(CommandType.LIST, ll4);
		l4.action();
		assertTrue(((ArrayVariable) l4.result()).getAll().size() == 7);
		assertTrue(((TextVariable) ((ArrayVariable) l4.result()).getVar(0)).getVar().equals("s"));
		assertTrue(((TextVariable) ((ArrayVariable) l4.result()).getVar(6)).getVar().equals("!"));

		ArrayVariable a = new ArrayVariable();
		a.setNextVar(new NumberVariable(13));
		a.setNextVar(new NumberVariable(38));
		a.setNextVar(new NumberVariable(875));
		a.setNextVar(new TextVariable("This is text!"));
		a.setNextVar(new TextVariable("But not this? But it is longest"));
		a.setNextVar(new TextVariable("Yes, it is."));
		a.setNextVar(new FileVariable(new File("C:/folder/file.rtf")));
		a.setNextVar(new FileVariable(new File("C:/folder/fileBig.txt")));

		ArrayList<Node> ll5 = new ArrayList<Node>();
		ll5.add(a);
		ll5.add(new NumberVariable(0));
		Command l5 = new Command(CommandType.LIST, ll5);
		l5.action();
		assertTrue(((ArrayVariable) l5.result()).getAll().size() == 8);
		assertTrue(((FileVariable) ((ArrayVariable) l5.result()).getVar(0)).getVar().getAbsolutePath()
				.equals("C:\\folder\\file.rtf"));
		assertTrue(((TextVariable) ((ArrayVariable) l5.result()).getVar(7)).getVar()
				.equals("But not this? But it is longest"));

		ArrayList<Node> ll5a = new ArrayList<Node>();
		ll5a.add(a);
		ll5a.add(new NumberVariable(1));
		Command l5a = new Command(CommandType.LIST, ll5a);
		l5a.action();
		assertTrue(((ArrayVariable) l5a.result()).getAll().size() == 8);
		assertTrue(((NumberVariable) ((ArrayVariable) l5a.result()).getVar(0)).getVar() == 13);
		assertTrue(((TextVariable) ((ArrayVariable) l5a.result()).getVar(7)).getVar().equals("Yes, it is."));

		// Read
		ArrayList<Node> lr = new ArrayList<Node>();
		lr.add(doc2);
		Command r = new Command(CommandType.READ, lr);
		r.action();
		assertTrue(((TextVariable) r.result()).getVar().equals(txt.getVar()));

		// Delete
		ArrayList<Node> ld = new ArrayList<Node>();
		ld.add(fol);
		Command d = new Command(CommandType.DELETE, ld);
		d.action();
		assertFalse(doc3.getVar().exists());

		ArrayList<Node> ld2 = new ArrayList<Node>();
		ld2.add(doc2);
		Command d2 = new Command(CommandType.DELETE, ld2);
		d2.action();
		assertFalse(doc2.getVar().exists());
	}

}
