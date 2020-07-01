package lumCode.folderScriptInterpreter;

import java.io.File;
import java.util.ArrayList;

import lumCode.folderScriptInterpreter.variables.DoubleVariable;
import lumCode.folderScriptInterpreter.variables.FileVariable;
import lumCode.folderScriptInterpreter.variables.FolderVariable;
import lumCode.folderScriptInterpreter.variables.IntVariable;
import lumCode.folderScriptInterpreter.variables.StringVariable;
import lumCode.folderScriptInterpreter.variables.Variable;

public class Main {
	public ArrayList<Variable> vars = new ArrayList<>();
	public boolean overwrite = false;

	public static String script = "";
	public static Variable[] a;

	public static void main(String[] args) {
		script = args[0];
		a = new Variable[args.length - 1];
		for (int i = 1; i < args.length; i++) {
			a[i - 1] = Variable.fromString(args[i]);
		}
	}

			}
		}
	}
}
