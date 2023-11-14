package lumCode.folderScriptInterpreter.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.Node;
import lumCode.folderScriptInterpreter.handlers.breaking.Break;
import lumCode.folderScriptInterpreter.handlers.conditional.Conditional;
import lumCode.folderScriptInterpreter.handlers.iteration.Iteration;
import lumCode.folderScriptInterpreter.handlers.logic.Logic;
import lumCode.folderScriptInterpreter.handlers.logic.LogicType;
import lumCode.folderScriptInterpreter.variables.ArrayVariable;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.SpecialVariable;
import lumCode.folderScriptInterpreter.variables.TextVariable;
import lumCode.folderScriptInterpreter.variables.lookUps.VariableLookUp;

public class IterationTest {
	private static final File fs = new File("resources/iterationTest/");

	private static FolderVariable fol;
	private static TextVariable txt;
	private static NumberVariable num;
	private static SpecialVariable spe;
	private static ArrayVariable arr;

	@BeforeEach
	void preTest() throws InterpreterException {
		fol = new FolderVariable(fs);
		txt = new TextVariable("test TEXT");
		num = new NumberVariable(14);
		spe = SpecialVariable.getInstance();
		arr = new ArrayVariable();

		arr.setVar(0, new FileVariable(new File("C:/temp/docs/dok1.rtf")));
		arr.setVar(1, new FolderVariable(new File("C:/temp/code")));
		arr.setVar(2, new TextVariable("test TEXT"));
		arr.setVar(3, new NumberVariable(14));
		arr.setVar(4, SpecialVariable.getInstance());
	}

	@Test
	void iterationTest() throws InterpreterException {
		ArrayList<Node> s = new ArrayList<Node>();

		// Number
		Iteration in = new Iteration(0, num, s);
		in.action();
		assertTrue(((NumberVariable) Main.lookUpIterator("i0")).getVar() == num.getVar());

		// Text
		Iteration it = new Iteration(0, txt, s);
		it.action();
		assertTrue(((TextVariable) Main.lookUpIterator("i0")).getVar().equals("T"));

		// Array
		Iteration ia = new Iteration(0, arr, s);
		ia.action();
		assertTrue(Main.lookUpIterator("i0").toString().equals("$"));

		// Folder
		Iteration io = new Iteration(0, fol, s);
		io.action();
		assertTrue(((FolderVariable) Main.lookUpIterator("i0")).getVar().getName().equals("fileTwo.txt"));

		// Special
		ArrayList<Node> s2 = new ArrayList<Node>();
		s2.add(new Conditional(new Logic(new VariableLookUp("i0"), LogicType.EQUAL, new NumberVariable(32)),
				Arrays.asList(new Break())));
		Iteration is = new Iteration(0, spe, s2);
		is.action();
		assertTrue(Main.lookUpIterator("i0").toString().equals("32"));
	}
}
