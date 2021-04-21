package app.interactive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import app.api.IAssassinThread;
import app.base.ThreadPool;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ִ�����������
 * @author yxlgg-if
 * @date 2021��3��31�� ����12:51:27
 */
@Slf4j
public class InteractiveDo implements Runnable{
    
    @Setter
    private Socket sendSocket;
    
    @Setter
    private Socket rcvdSocket;
    
    @Setter
    private IAssassinThread interactivePairThread;
    
    // �����С
    @Setter
    private int streamCacheSize = 4096;
    
    private boolean isAlive = false;

    public InteractiveDo() {
        // TODO �Զ����ɵĹ��캯�����
    }

    /**
     * 
     */
    @Override
    public void run() {
        // try(revd){} �Զ�����socket��close�����ر���
        try (InputStream inputStream = rcvdSocket.getInputStream();
                OutputStream outputStream = sendSocket.getOutputStream()) {
            int len = -1;
            byte[] byteArray = new byte[streamCacheSize];
            while (isAlive && (len = inputStream.read(byteArray)) > 0) {
//                System.out.println("��������" + Arrays.toString(byteArray));
//                System.out.println(new String(byteArray));
                outputStream.write(byteArray, 0, len);
                outputStream.flush();
            }
        } catch (Exception e) {
            log.debug("InteractiveDo thread exception.");
        }
        // ������ɹر�socket
        closeSocket();
        stopInteractivePairThread();
    }
    
    public void start() {
        if (!isAlive) {
            this.isAlive = true;
            ThreadPool.excuteCachedThreadpool(this);
        }
    }
    
    public void closeSocket() {
        this.isAlive = false;
        try {
            this.sendSocket.close();
            this.rcvdSocket.close();
        } catch (IOException e) {

        }
        
    }
    
    public void stopInteractivePairThread() {
        if (interactivePairThread != null) {
            interactivePairThread.stopUpperLevelPro();
        }
    }

}
