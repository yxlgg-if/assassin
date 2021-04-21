/**
 * 
 */
package app.base;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * ������
 * @author yxlgg-if
 * @date 2021��3��15�� ����8:16:35
 */
public class ConvertTools {

    /**
     * 
     */
    public ConvertTools() {
        // TODO �Զ����ɵĹ��캯�����
    }
    
    /**
     * integer ת��Ϊ byte[]����0xff�������������� �ο���http://www.litao0501.com/article-140.html
     * @author yxlgg-if
     * @date 2021��3��15�� ����8:18:50
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
     * byte[] ת��Ϊ integer
     * @author yxlgg-if
     * @date 2021��3��15�� ����8:19:29
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
     * ����������ת��Ϊ16�����ַ���
     * @author yxlgg-if
     * @date 2021��3��29�� ����6:38:55
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
     * ����base64��ȡAES��Կ
     * @author yxlgg-if
     * @date 2021��3��29�� ����13:18:12
     * @param aesBaseKey
     * @return
     */
    public static Key createAesBaseKeyByBase64(String aesBaseKey) {
        byte[] keyByteArray = Base64.getDecoder().decode(aesBaseKey);
        Key key = new SecretKeySpec(keyByteArray, "AES");
        return key;
    }
    
    
    /**
     * ���ַ�������AES���ܲ�ת��Ϊbase64
     * @author yxlgg-if
     * @date 2021��3��29�� ����13:34:40
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
     * ���ַ�������AES����
     * @author yxlgg-if
     * @date 2021��3��29�� ����13:19:23
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
     * �Ա����������AES����
     * @author yxlgg-if
     * @date 2021��3��29�� ����12:47:16
     * @param key
     * @param byteArray
     * @return
     * @throws Exception
     */
    public static byte[] encryptByteArrayByAes(Key base64AesKey, byte[] byteArray) throws Exception {
        return encryptByteArray(base64AesKey, byteArray, "AES/ECB/PKCS5Padding");
    }
    
    /**
     * �Ա���������м���
     * @author yxlgg-if
     * @date 2021��3��29�� ����13:20:55
     * @param key
     * @param byteArray
     * @param transformation
     * @return
     * @throws Exception
     */
    public static byte[] encryptByteArray(Key key, byte[] byteArray, String transformation) throws Exception {
        // ��ȡCipher��Ķ��󣬲�����"�㷨/ģʽ/���ģʽ" ���磺AES/ECB/PKCS5Padding
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // ��������ת����cipher.doFinal(content)������content��һ��byte����
        return cipher.doFinal(byteArray);
    }
    
    /**
     * ����base64�����aes�����ַ���
     * @author yxlgg-if
     * @date 2021��3��29�� ����13:55:44
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
     * �Լ��ܱ���������н���
     * @author yxlgg-if
     * @date 2021��3��29�� ����13:42:00
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
