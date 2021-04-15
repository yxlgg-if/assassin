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
		// TODO �Զ����ɵĹ��캯�����
	}
	
	public void testClientChannel() {
		try {
			Socket socket = new Socket("127.0.0.1", 22222);
			EncryptChannel encryptChannel = new EncryptChannel();
			try {
				MsgModel msgModel = MsgModel.getMsgModelObject(MsgTypeEnum.HEART_REPLY, new ExposePortModel(8888));
//				MsgModel msgModel = MsgModel.getMsgModelObject("3333333333333333333333",
//						MsgTypeEnum.MSG_REPLY, ReplyCodeModel.replaySuccess());
				encryptChannel.setsocket(socket);
				encryptChannel.setAesBaseKey(PubConfig.aesBaseKey);
				encryptChannel.setAutographsKey(PubConfig.autographsKey);
				encryptChannel.writeAndFlush(msgModel);
				MsgModel read = encryptChannel.read();
				System.out.println(read);
				encryptChannel.close();
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				System.out.println("ddddd");
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}

	public void testClientPair() {
		try {
			Socket rcvdSocket = new Socket("127.0.0.1", 8080);
			Socket sendSocket = new Socket("192.168.17.155", 10001);
//			Socket sendSocket = new Socket("127.0.0.1", 10001);
			InteractivePair interactivePair = new InteractivePair();
			interactivePair.setSendSocket(sendSocket);
			interactivePair.setRcvdSocket(rcvdSocket);
			interactivePair.createPair();
//			Thread.sleep(100000);
		} catch (UnknownHostException e) {
			// TODO �Զ����ɵ� catch ��
			System.out.println("�쳣1");
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			System.out.println("�쳣2");
		} 
	}
	public static void main(String[] args) {
//		TestClient.testClientPair();
//		System.out.println("�ͻ��˵ȴ�");
//		try {
//			Thread.sleep(100000);
//		} catch (InterruptedException e) {
//			// TODO �Զ����ɵ� catch ��
//			e.printStackTrace();
//		}
		
		// ������Ϣ�������
		TestClient client = new TestClient();
		client.testClientChannel();
	}

}
