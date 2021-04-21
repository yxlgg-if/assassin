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
 * 执行流输入输出
 * @author yxlgg-if
 * @date 2021年3月31日 上午12:51:27
 */
@Slf4j
public class InteractiveDo implements Runnable{
    
    @Setter
    private Socket sendSocket;
    
    @Setter
    private Socket rcvdSocket;
    
    @Setter
    private IAssassinThread interactivePairThread;
    
    // 缓存大小
    @Setter
    private int streamCacheSize = 4096;
    
    private boolean isAlive = false;

    public InteractiveDo() {
        // TODO 自动生成的构造函数存根
    }

    /**
     * 
     */
    @Override
    public void run() {
        // try(revd){} 自动调用socket的close方法关闭流
        try (InputStream inputStream = rcvdSocket.getInputStream();
                OutputStream outputStream = sendSocket.getOutputStream()) {
            int len = -1;
            byte[] byteArray = new byte[streamCacheSize];
            while (isAlive && (len = inputStream.read(byteArray)) > 0) {
//                System.out.println("比特流：" + Arrays.toString(byteArray));
//                System.out.println(new String(byteArray));
                outputStream.write(byteArray, 0, len);
                outputStream.flush();
            }
        } catch (Exception e) {
            log.debug("InteractiveDo thread exception.");
        }
        // 传输完成关闭socket
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
