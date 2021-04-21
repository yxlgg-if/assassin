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
        // TODO 自动生成的构造函数存根
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
        // TODO 自动生成的方法存根
//        for (StatusCodeEnum e:StatusCodeEnum.values()) {
//            System.out.println(e.name());
//        }
        
        // 测试
//        String dog = "狗";
//        String aesBaseKey = "U2FsdGVkX1/5RWtUxy/o63igqCuAqNWHxKAPG9JSSPw=";
//        byte[] byteArray = dog.getBytes();
//        String charset = StandardCharsets.UTF_8.name();
//        System.out.println("狗的比特数组：" + Arrays.toString(byteArray));
//        try {
//            String encode = ConvertTools.encryptAndConvertToBase64(aesBaseKey, dog, charset);
//            System.out.println("加密后：" + encode);
//            byte[] decode = ConvertTools.decryptEncryptStringByAes(aesBaseKey, encode);
//            System.out.println("解密后：" + Arrays.toString(decode));
//        } catch (Exception e) {
//            // TODO 自动生成的 catch 块
//            e.printStackTrace();
//        }
        
        // 测试
//        MsgModel msgModel = MsgModel.getMsgModelObject(MsgTypeEnum.CLIENT_CONTROL, new ExposePortModel(666));
//        
//        System.out.println(msgModel.toJSONString());
//        
//        // 加密模型
//        EncryptMsgModel encryptMsgModel = new EncryptMsgModel(msgModel);
//        encryptMsgModel.setCharset(StandardCharsets.UTF_8.name());
//        try {
//            encryptMsgModel.setEncryptValue(PubConfig.aesBaseKey, PubConfig.autographsKey);
//            System.out.println(encryptMsgModel.toJSONString());
//        } catch (Exception e) {
//            // TODO 自动生成的 catch 块
//            e.printStackTrace();
//        }
//        
//        // 解密模型
//        try {
//            JSONObject jsonObject = JSON.parseObject(encryptMsgModel.toJSONString());
//            EncryptMsgModel encryptMsgModel2 = jsonObject.toJavaObject(EncryptMsgModel.class);
//            boolean checkAutographMsg1 = encryptMsgModel2.checkAutographMsg(PubConfig.autographsKey);
//            System.out.println(checkAutographMsg1);
//            System.out.println(encryptMsgModel2.getEncryptMsgModelJsonString());
//            encryptMsgModel2.decryptMsg(PubConfig.aesBaseKey);
//            System.out.println(encryptMsgModel2.getMsgData());
//        } catch (Exception e) {
//            // TODO 自动生成的 catchen 块
//            e.printStackTrace();
//        }
        
        Test test = new Test();
        try {
            test.testEx();
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            System.out.println("异常");
            e.printStackTrace();
            return;
        }
        System.out.println("还在执行吗？");
    }

}
