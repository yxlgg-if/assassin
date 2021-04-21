/**
 * 
 */
package app.clientcls.threads;

import java.util.concurrent.TimeUnit;

import app.channel.SocketChannel;
import app.clientcls.config.IClientConfig;
import app.model.MsgModel;
import app.msgenum.MsgTypeEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * ��������
 * @author yxlgg-if
 * @date 2021��4��9�� ����6:50:53
 */
@Slf4j
public class SendHeartbeatThread implements Runnable{
    
    // ��������ʱ����
    private long sendHeartBeatTimeInterval = 10L;
    
    @Getter
    private boolean isAlive = false;
    
    private Thread sendHeartBeatThread = null;
    
    private IClientConfig<MsgModel, MsgModel> config;
    
    private SocketChannel<? extends MsgModel, ? super MsgModel> socketChannel;
    
    private ClientMainThread clientMainThread;
    
    private Integer maxRetry = 20;


    public SendHeartbeatThread(IClientConfig<MsgModel, MsgModel> config,
            SocketChannel<? extends MsgModel, ? super MsgModel> socketChannel,
            ClientMainThread clientMainThread) {
        this.config = config;
        this.socketChannel = socketChannel;
        this.clientMainThread = clientMainThread;
    }

    
    @Override
    public void run() {
        // ��������
        Integer retry=0;
        
        while (isAlive) {
            try {
                TimeUnit.SECONDS.sleep(sendHeartBeatTimeInterval);
            } catch (InterruptedException e) {
                // TODO �Զ����ɵ� catch ��
                this.stopHeartbeatThread();
                return;
            }
            
            try {
                this.sendHeartBeat();
                retry = 0;
            } catch (Exception e) {
//                e.printStackTrace();
                log.error("Send heartbeat Message exception [exposePort:{}]", this.config.getExposePort());
                this.clientMainThread.stopClientThread();
                
                if (isAlive) {
                    retry++;
                    try {
                        // ������������
                        boolean sendClientControlTag = this.clientMainThread.sendClientControl();
                        if (sendClientControlTag) {
                            this.socketChannel = this.clientMainThread.getSocketChannel();
                            this.clientMainThread.start();
                            log.info("The client[{}] reconnected successfully for the {} time.",
                                    this.config.getExposePort(), retry);
                            continue;
                        } else {
                            log.warn("The client[{}] failed to reconnect for the {} time.",
                                    this.config.getExposePort(), retry);
                        }
                    } catch (Exception e2) {
                        log.error("Retry in an unknow exception.");
                    }
                    
                    if (retry >= maxRetry) {
                        log.error("Retry reached the maximum number of times[{}],will close the client.", maxRetry);
                        this.stopHeartbeatThread();
                        this.clientMainThread.stopClientThread();
                        log.info("Client closed successfully.");
                    }
                }
                
            }
            
            
        }
    }
    
    public void sendHeartBeat() throws Exception {
        MsgModel msgModel = MsgModel.getMsgModelObject(MsgTypeEnum.HEART_TEST, null);
        this.socketChannel.writeAndFlush(msgModel);
    }
    
    public void start() {
        isAlive = true;
        log.info("sendHeartBeatThread [{}] starting...", this.config.getExposePort());
        if (sendHeartBeatThread == null || !sendHeartBeatThread.isAlive()) {
            sendHeartBeatThread = new Thread(this);
            sendHeartBeatThread.setName("Assassin-client-{}" + String.valueOf(this.config.getExposePort()));
            sendHeartBeatThread.start();
            log.info("sendHeartBeatThread [{}] started.", this.config.getExposePort());
        }
    }
    
    public void stopHeartbeatThread() {
        this.isAlive = false;
        if (sendHeartBeatThread != null) {
            sendHeartBeatThread.interrupt();
            sendHeartBeatThread = null;
        }
    }

}
