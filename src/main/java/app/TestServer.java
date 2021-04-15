package app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.midi.Track;

import app.channel.EncryptChannel;
import app.interactive.InteractivePair;
import app.model.EncryptMsgModel;
import app.model.MsgModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestServer implements Runnable {
	
	private ServerSocket serverSocket;
	private ServerSocket exposeServerSocket;
	private ServerSocket loaclServerSocket;
	
	
	private void serverPairStart() {
		try {
			exposeServerSocket = new ServerSocket(8888);
			loaclServerSocket = new ServerSocket(10001);
			
			while (true) {
				// 获取第三方请求
				Socket otherSocket = exposeServerSocket.accept();
				System.out.println("服务端-监听到8888的socket");
				
				// 启动客户端
				TestClient client = new TestClient();
				client.testClientPair();
				
				// 获取客户端请求
				Socket clientSocket = loaclServerSocket.accept();
				System.out.println("服务端-监听到10001的socket");
					
//				Socket sendSocket = new Socket("127.0.0.1", 8080);
//				Socket rcvdSocket = new Socket("192.168.17.155", 10001);
				
				
				InteractivePair interactivePair = new InteractivePair();
				interactivePair.setSendSocket(clientSocket);
				interactivePair.setRcvdSocket(otherSocket);
				interactivePair.createPair();
				System.out.println("lalalalal");
			}
			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			System.out.println("异常3");
			e.printStackTrace();
		}

		
		
	}

	public static void testLog() {
		log.info("lalalalalalal");
	}
	public TestServer() {
		// TODO 自动生成的构造函数存根
	}

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		// 测试加密隧道
		TestServer server = new TestServer();
		server.start();
//		TestServer testServer = new TestServer();
//		testServer.serverPairStart();
	}

	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(9999);
			this.serverSocket = serverSocket;
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				getMsg(socket);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}		
	}
	
	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public void getMsg(Socket socket) {
		EncryptChannel encryptChannel = new EncryptChannel();
		try {
			encryptChannel.setsocket(socket);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		encryptChannel.setAutographsKey(PubConfig.autographsKey);
		encryptChannel.setAesBaseKey(PubConfig.aesBaseKey);
		try {
			MsgModel read = encryptChannel.read();
			System.out.println(read.getMsgData());
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

}
