package app;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import app.channel.EncryptChannel;
import app.interactive.InteractivePair;
import app.model.ExposePortModel;
import app.model.MsgModel;
import app.model.ReplyCodeModel;
import app.msgenum.MsgTypeEnum;

public class TestClient {


    public TestClient() {
        // TODO 自动生成的构造函数存根
    }
    
    public void testClientChannel() {
        try {
            Socket socket = new Socket("127.0.0.1", 22222);
            EncryptChannel encryptChannel = new EncryptChannel();
            try {
                MsgModel msgModel = MsgModel.getMsgModelObject(MsgTypeEnum.HEART_REPLY, new ExposePortModel(8888));
//                MsgModel msgModel = MsgModel.getMsgModelObject("3333333333333333333333",
//                        MsgTypeEnum.MSG_REPLY, ReplyCodeModel.replaySuccess());
                encryptChannel.setsocket(socket);
                encryptChannel.setAesBaseKey(PubConfig.aesBaseKey);
                encryptChannel.setAutographsKey(PubConfig.autographsKey);
                encryptChannel.writeAndFlush(msgModel);
                MsgModel read = encryptChannel.read();
                System.out.println(read);
                encryptChannel.close();
            } catch (Exception e) {
                // TODO 自动生成的 catch 块
                System.out.println("ddddd");
                e.printStackTrace();
            }
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
    }

    public void testClientPair() {
        try {
            Socket rcvdSocket = new Socket("127.0.0.1", 8080);
            Socket sendSocket = new Socket("192.168.17.155", 10001);
//            Socket sendSocket = new Socket("127.0.0.1", 10001);
            InteractivePair interactivePair = new InteractivePair();
            interactivePair.setSendSocket(sendSocket);
            interactivePair.setRcvdSocket(rcvdSocket);
            interactivePair.createPair();
//            Thread.sleep(100000);
        } catch (UnknownHostException e) {
            // TODO 自动生成的 catch 块
            System.out.println("异常1");
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            System.out.println("异常2");
        } 
    }
    public static void main(String[] args) {
//        TestClient.testClientPair();
//        System.out.println("客户端等待");
//        try {
//            Thread.sleep(100000);
//        } catch (InterruptedException e) {
//            // TODO 自动生成的 catch 块
//            e.printStackTrace();
//        }
        
        // 发送消息到服务端
        TestClient client = new TestClient();
        client.testClientChannel();
    }

}
