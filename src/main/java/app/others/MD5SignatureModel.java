/**
 * 
 */
package app.others;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import app.base.ConvertTools;

/**
 * MD5散列签名模型
 * @author yxlgg-if
 * @date 2021年3月29日 下午6:50:35
 */
public class MD5SignatureModel {
	
	private static final Charset CHARSET = StandardCharsets.UTF_8;

    private static final String HEXBASE = "0123456789abcdef";

	/**
	 * 
	 */
	public MD5SignatureModel() {
		// TODO 自动生成的构造函数存根
	}
	
    public static String md5Signature(String... params) {
        return md5Signature(CHARSET, params);
    }
    
	public static String md5Signature(Charset charset, String... params) {
		Arrays.sort(params);
		StringBuffer stringBuffer =  new StringBuffer();
		for (int i = 0; i < 4; ++i) {
			stringBuffer.append(params[i]);
		}
		
		MessageDigest md5;
		try {
			// 返回实现指定摘要算法的 MessageDigest对象
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO 自动生成的 catch 块
			return null;
		}
		byte[] digest = md5.digest(stringBuffer.toString().getBytes(charset));
		
		return ConvertTools.byteToHex(digest, HEXBASE);
	}

}
