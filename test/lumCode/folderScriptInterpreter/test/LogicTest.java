package lumCode.folderScriptInterpreter.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.logic.Logic;
import lumCode.folderScriptInterpreter.handlers.logic.LogicType;
import lumCode.folderScriptInterpreter.variables.Variable;

class LogicTest {

	@Test
	void logicTest() throws InterpreterException {
		Logic l = new Logic(Variable.fromString("15"), LogicType.GREATER, Variable.fromString("12"));
		l.action();
		assertTrue(l.result());
		Logic l2 = new Logic(Variable.fromString("String"), LogicType.EQUAL, Variable.fromString("String"));
		l2.action();
		assertTrue(l2.result());
		Logic l3 = new Logic(Variable.fromString("String"), LogicType.EQUAL, Variable.fromString("Type"));
		l3.action();
		assertTrue(!l3.result());
	}

}
