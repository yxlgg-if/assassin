/**
 * 
 */
package app.base;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

/**
 * @author yxlgg-if
 * @date 2021年4月19日 下午7:22:11
 */
public class LoadIni {

	public static String clientHost = null;
	public static String serverHost = null;
	public static Integer serverPort = null;
	public static String aesBaseKey = null;
	public static String autographsKey = null;
	public static Map<String, String> map = null;
	public static Wini ini = null;
	
	
	public static void redIni() {
		try {
			ini = new Wini(new File("conf/assassin.ini"));
		} catch (InvalidFileFormatException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		Ini.Section publicSec = ini.get("public");
		clientHost = publicSec.get("clientHost").replaceAll("(?:\"|')", "");
		serverHost = publicSec.get("serverHost").replaceAll("(?:\"|')", "");
		serverPort = Integer.parseInt(publicSec.get("serverPort").replaceAll("(?:\"|')", ""));
		aesBaseKey = publicSec.get("aesBaseKey").replaceAll("(?:\"|')", "");
		autographsKey = publicSec.get("autographsKey").replaceAll("(?:\"|')", "");
		
		map = ini.get("map");
		
	}

}
