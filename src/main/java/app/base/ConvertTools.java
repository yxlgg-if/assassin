/**
 * 
 */
package app.base;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 工具类
 * @author yxlgg-if
 * @date 2021年3月15日 下午8:16:35
 */
public class ConvertTools {

	/**
	 * 
	 */
	public ConvertTools() {
		// TODO 自动生成的构造函数存根
	}
	
	/**
	 * integer 转换为 byte[]，与0xff进行与运算作用 参考：http://www.litao0501.com/article-140.html
	 * @author yxlgg-if
	 * @date 2021年3月15日 下午8:18:50
	 * @param intNumber
	 * @return
	 */
	public static byte[] intToBytes(int intNumber) {
		return new byte[] { 
				(byte) ((intNumber >> 24) & 0xff),
				(byte) ((intNumber >> 16) & 0xff),
                (byte) ((intNumber >> 8) & 0xff), 
                (byte) (intNumber & 0xff) 
                };
	}
	
	/**
	 * byte[] 转换为 integer
	 * @author yxlgg-if
	 * @date 2021年3月15日 下午8:19:29
	 * @param byteArray
	 * @return
	 */
	public static int bytesToInt(byte[] byteArrays) {
        int intNumber = 0;

        for (int i = 0; i < 4; ++i) {
        	intNumber <<= 8;
        	intNumber |= byteArrays[i] & 0xff;
        }

        return intNumber;
	}
	
	/**
	 * 将比特数组转换为16进制字符串
	 * @author yxlgg-if
	 * @date 2021年3月29日 下午6:38:55
	 * @param byteArray
	 * @param hexBase
	 * @return
	 */
	public static String byteToHex(byte[] byteArray, String hexBase) {
		StringBuffer stringBuffer = new StringBuffer(byteArray.length << 1);
		
		for (byte byteValue:byteArray) {
			stringBuffer.append(hexBase.charAt(byteValue >> 4 & 0xf));
			stringBuffer.append(hexBase.charAt(byteValue & 0xf));
		}
		return stringBuffer.toString();
	}
	
	/**
	 * 根据base64获取AES密钥
	 * @author yxlgg-if
	 * @date 2021年3月29日 上午13:18:12
	 * @param aesBaseKey
	 * @return
	 */
	public static Key createAesBaseKeyByBase64(String aesBaseKey) {
		byte[] keyByteArray = Base64.getDecoder().decode(aesBaseKey);
		Key key = new SecretKeySpec(keyByteArray, "AES");
		return key;
	}
	
	
	/**
	 * 对字符串进行AES加密并转换为base64
	 * @author yxlgg-if
	 * @date 2021年3月29日 上午13:34:40
	 * @param aesBaseKey
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public static String encryptAndConvertToBase64(String aesBaseKey, String string, String charset) throws Exception {
		byte[] encryptStringByAes = encryptStringByAes(aesBaseKey, string, charset);
		return Base64.getEncoder().encodeToString(encryptStringByAes);
	}
	
	/**
	 * 对字符串进行AES加密
	 * @author yxlgg-if
	 * @date 2021年3月29日 上午13:19:23
	 * @param aesBaseKey
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptStringByAes(String aesBaseKey, String string, String charset) throws Exception {
		byte[] byteArray = string.getBytes(charset);
		Key base64AesKey = createAesBaseKeyByBase64(aesBaseKey);
		return encryptByteArrayByAes(base64AesKey, byteArray);
	}
	
	/**
	 * 对比特数组进行AES加密
	 * @author yxlgg-if
	 * @date 2021年3月29日 上午12:47:16
	 * @param key
	 * @param byteArray
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByteArrayByAes(Key base64AesKey, byte[] byteArray) throws Exception {
		return encryptByteArray(base64AesKey, byteArray, "AES/ECB/PKCS5Padding");
	}
	
	/**
	 * 对比特数组进行加密
	 * @author yxlgg-if
	 * @date 2021年3月29日 上午13:20:55
	 * @param key
	 * @param byteArray
	 * @param transformation
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByteArray(Key key, byte[] byteArray, String transformation) throws Exception {
		// 获取Cipher类的对象，参数按"算法/模式/填充模式" 例如：AES/ECB/PKCS5Padding
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		// 调用数据转换：cipher.doFinal(content)，其中content是一个byte数组
		return cipher.doFinal(byteArray);
	}
	
	/**
	 * 解密base64编码的aes加密字符串
	 * @author yxlgg-if
	 * @date 2021年3月29日 上午13:55:44
	 * @param aesBaseKey
	 * @param encryptStringByAes
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptEncryptStringByAes(String aesBaseKey, String encryptStringByAes) throws Exception {
		Key base64AesKey = createAesBaseKeyByBase64(aesBaseKey);
		byte[] decode = Base64.getDecoder().decode(encryptStringByAes);
		return decryptByteArray(base64AesKey, decode, "AES/ECB/PKCS5Padding");
	}
	
	/**
	 * 对加密比特数组进行解密
	 * @author yxlgg-if
	 * @date 2021年3月29日 上午13:42:00
	 * @param key
	 * @param encryptByteArray
	 * @param transformation
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByteArray(Key key, byte[] encryptByteArray, String transformation) throws Exception {
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(encryptByteArray);
	}

}
