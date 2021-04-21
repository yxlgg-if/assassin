/**
 * 
 */
package app.channel;

import java.io.IOException;
import java.net.Socket;

/**
 * @author yxlgg-if
 * @param <R>
 * @param <W>
 * @date 2021��3��30�� ����13:23:19
 */
public abstract class SocketChannel<R, W> implements IChannel<R, W> {

    /**
     * ��ȡsocket
     * @author yxlgg-if
     * @date 2021��3��30�� ����13:33:00
     * @return
     */
    public abstract Socket getSocket();
    
    /**
     * ����socket
     * @author yxlgg-if
     * @date 2021��3��30�� ����13:33:13
     * @param socket
     * @throws Exception
     */
    public abstract void setsocket(Socket socket) throws Exception;
    
    /**
     * �ر�socket
     * @author yxlgg-if
     * @date 2021��3��30�� ����13:33:34
     * @throws Exception
     */
    public abstract void closeSocket() throws IOException;

    @Override
    public void close() throws IOException {
        this.closeSocket();
    }
}
