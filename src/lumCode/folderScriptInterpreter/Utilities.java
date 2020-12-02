package lumCode.folderScriptInterpreter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.codec.digest.DigestUtils;

public class Utilities {

	public static String getMD5(File fil) {
		String md5;
		try {
			InputStream is = Files.newInputStream(Paths.get(fil.getAbsolutePath()));
			md5 = DigestUtils.md5Hex(is);
		} catch (IOException e) {
			// e.printStackTrace();
			md5 = "";
		}
		return md5;
	}
}
