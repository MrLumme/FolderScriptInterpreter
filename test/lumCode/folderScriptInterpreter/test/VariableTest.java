package lumCode.folderScriptInterpreter.test;

import lumCode.folderScriptInterpreter.Main;
import lumCode.folderScriptInterpreter.exceptions.InterpreterException;
import lumCode.folderScriptInterpreter.exceptions.notFoundExceptions.AttributeNotFoundException;
import lumCode.folderScriptInterpreter.variables.lookUps.AttributeType;
import lumCode.folderScriptInterpreter.variables.lookUps.EnvironmentLookUp;
import lumCode.folderScriptInterpreter.variables.lookUps.EnvironmentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.NumberVariable;
import lumCode.folderScriptInterpreter.variables.SpecialVariable;
import lumCode.folderScriptInterpreter.variables.TextVariable;
import lumCode.folderScriptInterpreter.variables.Variable;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class VariableTest {
	private static final File doc = new File("resources/variableTest/doc1.xml");

	@BeforeAll
	static void preTest() throws InterpreterException {
		assertTrue(doc.exists());
	}

	@Test
	void conversionTest() {
		assertTrue(Variable.fromString("-390586") instanceof NumberVariable);
		assertTrue(Variable.fromString("fpotpk") instanceof TextVariable);
		assertTrue(Variable.fromString("c:/users/fsi/test.pdf") instanceof FileVariable);
		assertTrue(Variable.fromString("c:/users/fsi/") instanceof FolderVariable);
		assertTrue(Variable.fromString("$") instanceof SpecialVariable);
	}

	@Test
	void environmentVariableTest() throws InterpreterException {
		EnvironmentLookUp env1 = new EnvironmentLookUp(EnvironmentType.EXECUTION_LOCATION);
		env1.action();
		assertEquals(env1.result().toString(), System.getProperty("user.dir"));

		EnvironmentLookUp env2 = new EnvironmentLookUp(EnvironmentType.TEMP_LOCATION);
		env2.action();
		assertEquals(env2.result().toString(), Main.tempDir.getAbsolutePath());

		EnvironmentLookUp env3 = new EnvironmentLookUp(EnvironmentType.LOG_FILE);
		env3.action();
		assertEquals(env3.result().toString(), Main.logFile.getAbsolutePath());

		EnvironmentLookUp env4 = new EnvironmentLookUp(EnvironmentType.SYSTEM_TIME);
		env4.action();
		String approx = "" + System.currentTimeMillis();
		approx = approx.substring(0, approx.length() - 3);
		assertTrue(env4.result().toString().startsWith(approx));

		EnvironmentLookUp env5 = new EnvironmentLookUp(EnvironmentType.DATE_TIME);
		env5.action();
		String time = LocalDateTime.now().format(Main.dateFormater).substring(0, 17);
		assertTrue(env5.result().toString().startsWith(time));
	}

	@Test
	void attributeVariableTest() throws AttributeNotFoundException, IOException {
		FileVariable file = new FileVariable(doc);

		Variable hidden = file.getAttribute(AttributeType.IS_HIDDEN);
		assertEquals(hidden.toString(), "1");
		Variable link = file.getAttribute(AttributeType.IS_LINK);
		assertEquals(link.toString(), "0");
		Variable modified = file.getAttribute(AttributeType.LAST_MODIFIED_TIME);
		assertTrue(modified.toString().matches("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"));
		Variable access = file.getAttribute(AttributeType.LAST_ACCESS_TIME);
		assertTrue(access.toString().matches("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"));
		Variable creation = file.getAttribute(AttributeType.CREATION_TIME);
		assertTrue(creation.toString().matches("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"));
	}

}
