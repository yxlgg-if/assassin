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
 * ������Ϣģ��
 * @author yxlgg-if
 * @date 2021��3��27�� ����13:33:39
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EncryptMsgModel extends MsgModel{

	// ʱ���
	private Long timestamp;
	
	// ǩ��
	private String autograph;
	
	// MsgModel��Ϣģ�͵�jsonString�ļ���ֵ
	private String encryptMsgModelJsonString;
	
	// �ַ�����
	private String charset = StandardCharsets.UTF_8.name();
	/**
	 * 
	 */
	public EncryptMsgModel(MsgModel msgModel) {
		// TODO �Զ����ɵĹ��캯�����
		super(msgModel);
	}
	
	/**
	 * ����MsgModel��toJSONString()�������ص�Json�ַ���
	 * @author yxlgg-if
	 * @date 2021��3��29�� ����6:24:50
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
	 * @date 2021��3��29�� ����6:54:39
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
