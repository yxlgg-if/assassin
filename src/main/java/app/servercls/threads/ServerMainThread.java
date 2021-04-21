package app.servercls.threads;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import app.base.ThreadPool;
import app.model.MsgModel;
import app.servercls.config.IServiceConfig;
import app.servercls.literacyevent.IReadMsg;
import app.servercls.literacyevent.ReadMsg;
import lombok.extern.slf4j.Slf4j;

/**
 * �����������
 * @author yxlgg-if
 * @date 2021��3��25�� ����6:52:04
 */
@Slf4j
public final class ServerMainThread implements Runnable {
    
    private boolean isAlive = false;
    private Thread serverMainThread = null;
    private ServerSocket serverSocket;
    private IServiceConfig<MsgModel, MsgModel> config;

    public ServerMainThread(IServiceConfig<MsgModel, MsgModel> config) {
        // TODO �Զ����ɵĹ��캯�����
        this.config = config;
    }
    
    @Override
    public void run() {
        while (isAlive) {
            try {
                // ���տͻ��˵���Ϣ
                Socket clientSocket = serverSocket.accept();
                // ����ͻ��˵���Ϣ
                processClientMsg(clientSocket);
            } catch (Exception e) {
                // TODO: handle exception
                log.warn("Process socket from the client exception,closed the socket.", e);
            }
        }
    }
    
    public void start() {
        log.info("Assassin Service [{}] starting...", this.config.getAssassinServerPort());
        isAlive = true;
        if (serverMainThread == null || !serverMainThread.isAlive()) {
            serverMainThread = new Thread(this);
            serverMainThread.setName("Assassin-server-" + String.valueOf(this.config.getAssassinServerPort()));
            try {
                serverSocket = new ServerSocket(this.config.getAssassinServerPort());
            } catch (Exception e) {
                // TODO: handle exception
                log.error("create ServerSocket [{}] failed.", this.config.getAssassinServerPort());
                this.closeServerMainThread();
                return;
            }
            serverMainThread.start();
            log.info("Assassin Service [{}] successfully started.", this.config.getAssassinServerPort());
        } else {
            log.warn("ServerSocket [{}] is started.", this.config.getAssassinServerPort());
        }
    }
    
    public void closeServerMainThread() {
        isAlive = false;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO �Զ����ɵ� catch ��
                log.error("ServerSocket closed exception.");
            }
            serverSocket = null;
        }
        
        if (serverMainThread != null) {
            serverMainThread.interrupt();
            serverMainThread = null;
        }
    }

    /**
     * ����ͻ��˷�������Ϣ
     * @author yxlgg-if
     * @date 2021��4��13�� ����6:38:56
     * @param clientSocket
     */
    public void processClientMsg(Socket clientSocket) {
        // ȫ���̳߳�
        ThreadPool.excuteCachedThreadpool(new Runnable() {
            
            @Override
            public void run() {
                // TODO �Զ����ɵķ������
                IReadMsg<MsgModel, MsgModel> readMsg = new ReadMsg(config);
                try {
                    readMsg.readClientMsg(clientSocket);
                } catch (Exception e) {
                    log.warn("Process socket from the client exception,will close the socket.", e);
                    try {
                        clientSocket.close();
                    } catch (IOException e1) {
                        log.warn("Closed the socket from the client exception.");
                    }
                }
            }
        });
    }
}
