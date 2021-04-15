package app;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import app.base.ConvertTools;
import app.model.EncryptMsgModel;
import app.model.ExposePortModel;
import app.model.MsgModel;
import app.msgenum.MsgTypeEnum;
import app.msgenum.StatusCodeEnum;
import lombok.Setter;

public class Test {

	@Setter
	private Integer i;
	private Map<Integer, Test> map = new HashMap<>(); 
	
	public Test() {
		// TODO �Զ����ɵĹ��캯�����
	}
	
	public void testObj() {
		System.out.print("testObj:");
		System.out.println(i);
	}
	
	public void testEx() throws Exception {
		Integer i=1/1;
		System.out.print("111111111111111111");
	}
	
	public void addMap(Integer i) {
		Test test = new Test();
		test.setI(i);
		map.put(i, test);
	}
	
	public void doIt() {
		for (int i=0;i<100;i++) {
			System.out.println("add:" + i);
			addMap(i);
		}
	}
	
	public void pr() {
		doIt();
		for (Object m:map.keySet()) {
			Test test = map.get(m);
			System.out.println("key:" + m.toString());
			test.testObj();
		}
	}

	public static void main(String[] args) {
		// TODO �Զ����ɵķ������
//		for (StatusCodeEnum e:StatusCodeEnum.values()) {
//			System.out.println(e.name());
//		}
		
		// ����
//		String dog = "��";
//		String aesBaseKey = "U2FsdGVkX1/5RWtUxy/o63igqCuAqNWHxKAPG9JSSPw=";
//		byte[] byteArray = dog.getBytes();
//		String charset = StandardCharsets.UTF_8.name();
//        System.out.println("���ı������飺" + Arrays.toString(byteArray));
//        try {
//			String encode = ConvertTools.encryptAndConvertToBase64(aesBaseKey, dog, charset);
//			System.out.println("���ܺ�" + encode);
//			byte[] decode = ConvertTools.decryptEncryptStringByAes(aesBaseKey, encode);
//			System.out.println("���ܺ�" + Arrays.toString(decode));
//		} catch (Exception e) {
//			// TODO �Զ����ɵ� catch ��
//			e.printStackTrace();
//		}
		
		// ����
//		MsgModel msgModel = MsgModel.getMsgModelObject(MsgTypeEnum.CLIENT_CONTROL, new ExposePortModel(666));
//		
//		System.out.println(msgModel.toJSONString());
//		
//		// ����ģ��
//		EncryptMsgModel encryptMsgModel = new EncryptMsgModel(msgModel);
//		encryptMsgModel.setCharset(StandardCharsets.UTF_8.name());
//		try {
//			encryptMsgModel.setEncryptValue(PubConfig.aesBaseKey, PubConfig.autographsKey);
//			System.out.println(encryptMsgModel.toJSONString());
//		} catch (Exception e) {
//			// TODO �Զ����ɵ� catch ��
//			e.printStackTrace();
//		}
//		
//		// ����ģ��
//		try {
//			JSONObject jsonObject = JSON.parseObject(encryptMsgModel.toJSONString());
//			EncryptMsgModel encryptMsgModel2 = jsonObject.toJavaObject(EncryptMsgModel.class);
//			boolean checkAutographMsg1 = encryptMsgModel2.checkAutographMsg(PubConfig.autographsKey);
//			System.out.println(checkAutographMsg1);
//			System.out.println(encryptMsgModel2.getEncryptMsgModelJsonString());
//			encryptMsgModel2.decryptMsg(PubConfig.aesBaseKey);
//			System.out.println(encryptMsgModel2.getMsgData());
//		} catch (Exception e) {
//			// TODO �Զ����ɵ� catchen ��
//			e.printStackTrace();
//		}
		
		Test test = new Test();
		try {
			test.testEx();
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			System.out.println("�쳣");
			e.printStackTrace();
			return;
		}
		System.out.println("����ִ����");
	}

}
