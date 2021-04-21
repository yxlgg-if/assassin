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
                // ��ȡ����������
                Socket otherSocket = exposeServerSocket.accept();
                System.out.println("�����-������8888��socket");
                
                // �����ͻ���
                TestClient client = new TestClient();
                client.testClientPair();
                
                // ��ȡ�ͻ�������
                Socket clientSocket = loaclServerSocket.accept();
                System.out.println("�����-������10001��socket");
                    
//                Socket sendSocket = new Socket("127.0.0.1", 8080);
//                Socket rcvdSocket = new Socket("192.168.17.155", 10001);
                
                
                InteractivePair interactivePair = new InteractivePair();
                interactivePair.setSendSocket(clientSocket);
                interactivePair.setRcvdSocket(otherSocket);
                interactivePair.createPair();
                System.out.println("lalalalal");
            }
            
        } catch (IOException e) {
            // TODO �Զ����ɵ� catch ��
            System.out.println("�쳣3");
            e.printStackTrace();
        }

        
        
    }

    public static void testLog() {
        log.info("lalalalalalal");
    }
    public TestServer() {
        // TODO �Զ����ɵĹ��캯�����
    }

    public static void main(String[] args) {
        // TODO �Զ����ɵķ������
        // ���Լ������
        TestServer server = new TestServer();
        server.start();
//        TestServer testServer = new TestServer();
//        testServer.serverPairStart();
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            this.serverSocket = serverSocket;
        } catch (IOException e) {
            // TODO �Զ����ɵ� catch ��
            e.printStackTrace();
        }
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                getMsg(socket);
            } catch (IOException e) {
                // TODO �Զ����ɵ� catch ��
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
            // TODO �Զ����ɵ� catch ��
            e.printStackTrace();
        }
        encryptChannel.setAutographsKey(PubConfig.autographsKey);
        encryptChannel.setAesBaseKey(PubConfig.aesBaseKey);
        try {
            MsgModel read = encryptChannel.read();
            System.out.println(read.getMsgData());
        } catch (Exception e) {
            // TODO �Զ����ɵ� catch ��
            e.printStackTrace();
        }
    }

}
