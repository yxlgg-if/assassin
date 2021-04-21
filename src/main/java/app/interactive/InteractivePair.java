/**
 * 
 */
package app.interactive;

import java.io.IOException;
import java.net.Socket;

import app.api.IAssassinThread;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ������������
 * @author yxlgg-if
 * @date 2021��3��31�� ����13:49:39
 */
@Slf4j
public class InteractivePair implements IAssassinThread{
    
    private InteractiveDo inAndOut;
    private InteractiveDo outAndIn;
    
    @Setter
    private Socket sendSocket;
    
    @Setter
    private Socket rcvdSocket;
    // �����С
    @Setter
    private int streamCacheSize = 4096;
    
    private boolean isAlive = false;
    
    @Setter
    private String socketPairKey;
    
    @Setter
    private IAssassinThread assassinThread;

    /**
     * 
     */
    public InteractivePair() {
        // TODO �Զ����ɵĹ��캯�����
    }
    
    public boolean createPair() {
        if (this.isAlive) {
            return true;
        }
        this.isAlive = true;
        try {
            outAndIn = new InteractiveDo();
            outAndIn.setRcvdSocket(rcvdSocket);
            outAndIn.setSendSocket(sendSocket);
            outAndIn.setInteractivePairThread(this);
            outAndIn.setStreamCacheSize(streamCacheSize);
            
            inAndOut = new InteractiveDo();
            inAndOut.setRcvdSocket(sendSocket);
            inAndOut.setSendSocket(rcvdSocket);
            inAndOut.setInteractivePairThread(this);
            inAndOut.setStreamCacheSize(streamCacheSize);
            
            // ��ʼ����
            outAndIn.start();
            inAndOut.start();
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Input and output execute exception.", e);
            close();
            stopUpperLevelPro();
            return false;
        }
    }

    public void close() {
        log.debug("Will close the socketPair[{}].", this.socketPairKey);
        this.isAlive = false;
        if (rcvdSocket != null) {
            try {
                rcvdSocket.close();
            } catch (IOException e) {
                log.debug("RcvdSocket closed exception.");
            }
            
            rcvdSocket = null;
        }
        
        if (sendSocket != null) {
            try {
                sendSocket.close();
            } catch (IOException e) {
                log.debug("SendSocket closed exception.");
            }
            
            sendSocket = null;
        }
        
        if (outAndIn != null) {
            outAndIn.closeSocket();
            outAndIn = null;
        }
        
        if (inAndOut != null) {
            inAndOut.closeSocket();
            inAndOut = null;
        }
        
        log.debug("Closed the socketPair[{}].", this.socketPairKey);
    }

    @Override
    public void stopUpperLevelPro() {
        if (assassinThread != null) {
            assassinThread.stopInteractive(socketPairKey);
            assassinThread = null;
        }
    }
}
