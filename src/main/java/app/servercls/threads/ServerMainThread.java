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
 * 服务端主进程
 * @author yxlgg-if
 * @date 2021年3月25日 下午6:52:04
 */
@Slf4j
public final class ServerMainThread implements Runnable {
    
    private boolean isAlive = false;
    private Thread serverMainThread = null;
    private ServerSocket serverSocket;
    private IServiceConfig<MsgModel, MsgModel> config;

    public ServerMainThread(IServiceConfig<MsgModel, MsgModel> config) {
        // TODO 自动生成的构造函数存根
        this.config = config;
    }
    
    @Override
    public void run() {
        while (isAlive) {
            try {
                // 接收客户端的消息
                Socket clientSocket = serverSocket.accept();
                // 处理客户端的消息
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
                // TODO 自动生成的 catch 块
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
     * 处理客户端发来的消息
     * @author yxlgg-if
     * @date 2021年4月13日 下午6:38:56
     * @param clientSocket
     */
    public void processClientMsg(Socket clientSocket) {
        // 全局线程池
        ThreadPool.excuteCachedThreadpool(new Runnable() {
            
            @Override
            public void run() {
                // TODO 自动生成的方法存根
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
