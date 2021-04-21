/**
 * 
 */
package app.model;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import app.base.ConvertTools;
import app.others.MD5SignatureModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 加密消息模型
 * @author yxlgg-if
 * @date 2021年3月27日 上午13:33:39
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EncryptMsgModel extends MsgModel{

    // 时间戳
    private Long timestamp;
    
    // 签名
    private String autograph;
    
    // MsgModel消息模型的jsonString的加密值
    private String encryptMsgModelJsonString;
    
    // 字符编码
    private String charset = StandardCharsets.UTF_8.name();
    /**
     * 
     */
    public EncryptMsgModel(MsgModel msgModel) {
        // TODO 自动生成的构造函数存根
        super(msgModel);
    }
    
    /**
     * 加密MsgModel的toJSONString()方法返回的Json字符串
     * @author yxlgg-if
     * @date 2021年3月29日 下午6:24:50
     * @param aesBaseKey
     * @throws Exception
     */
    public void encryptMsg(String aesBaseKey) throws Exception {
        String encryptBase64 = ConvertTools.encryptAndConvertToBase64(aesBaseKey, super.toJSONString(), charset);
        this.encryptMsgModelJsonString = encryptBase64;
    }
    
    /**
     * 
     * @author yxlgg-if
     * @date 2021年3月29日 下午6:54:39
     * @param aesBaseKey
     * @throws Exception
     */
    public void decryptMsg(String aesBaseKey) throws Exception {
        byte[] decryptBase64 = ConvertTools.decryptEncryptStringByAes(aesBaseKey, encryptMsgModelJsonString);
        String msgModelJsonString = new String(decryptBase64, charset);
        MsgModel msgModel = JSON.parseObject(msgModelJsonString, MsgModel.class);
        super.setValue(msgModel);
    }
    
    public void autographMsg(String autographsKey) {
        String md5Signature = MD5SignatureModel.md5Signature(Charset.forName(charset),
                autographsKey, timestamp.toString(), encryptMsgModelJsonString, charset);
        this.autograph = md5Signature;
    }
    
    public boolean checkAutographMsg(String autographsKey) {
        String md5Signature = MD5SignatureModel.md5Signature(Charset.forName(charset),
                autographsKey, timestamp.toString(), encryptMsgModelJsonString, charset);
        return StringUtils.equals(this.autograph, md5Signature);
    }
    
    public void setEncryptValue(String aesBaseKey,String autographsKey) throws Exception {
        this.timestamp = System.currentTimeMillis();
        this.encryptMsg(aesBaseKey);
        this.autographMsg(autographsKey);
        
    }
    
    @Override
    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("charset", charset);
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("encryptMsgModelJsonString", encryptMsgModelJsonString);
        jsonObject.put("autograph", autograph);
        return jsonObject.toJSONString();
    }

}
