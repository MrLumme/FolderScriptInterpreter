package lumCode.folderScriptInterpreter.test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.handlers.logic.Logic;
import lumCode.folderScriptInterpreter.handlers.logic.LogicType;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.Variable;

class LogicTest {

	@Test
	void logicTest() throws InterpreterException {
		Logic l = new Logic(Variable.fromString("15"), LogicType.GREATER, Variable.fromString("12"));
		l.action();
		assertTrue(((NumberVariable) l.result()).asBoolean());
		Logic l2 = new Logic(Variable.fromString("String"), LogicType.EQUAL, Variable.fromString("String"));
		l2.action();
		assertTrue(((NumberVariable) l2.result()).asBoolean());
		Logic l3 = new Logic(Variable.fromString("String"), LogicType.EQUAL, Variable.fromString("Type"));
		l3.action();
		assertFalse(((NumberVariable) l3.result()).asBoolean());
		Logic l4 = new Logic(Variable.fromString("1"), LogicType.AND, Variable.fromString("0"));
		l4.action();
		assertTrue(((NumberVariable) l4.result()).getVar() == 0);
		Logic l5 = new Logic(Variable.fromString("1"), LogicType.OR, Variable.fromString("0"));
		l5.action();
		assertTrue(((NumberVariable) l5.result()).getVar() == 1);
	}

}
