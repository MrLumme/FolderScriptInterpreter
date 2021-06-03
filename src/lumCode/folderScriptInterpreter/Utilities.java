package lumCode.folderScriptInterpreter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

	public static List<File> listFiles(File dir, int level) {
		List<File> out = new ArrayList<File>();
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				if (level == 0) {
					out.add(f);
				} else {

				}
			} else if (f.isFile()) {

			}
		}
	}
}
