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
 * MD5ɢ��ǩ��ģ��
 * @author yxlgg-if
 * @date 2021��3��29�� ����6:50:35
 */
public class MD5SignatureModel {
    
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private static final String HEXBASE = "0123456789abcdef";

    /**
     * 
     */
    public MD5SignatureModel() {
        // TODO �Զ����ɵĹ��캯�����
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
            // ����ʵ��ָ��ժҪ�㷨�� MessageDigest����
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // TODO �Զ����ɵ� catch ��
            return null;
        }
        byte[] digest = md5.digest(stringBuffer.toString().getBytes(charset));
        
        return ConvertTools.byteToHex(digest, HEXBASE);
    }

}
