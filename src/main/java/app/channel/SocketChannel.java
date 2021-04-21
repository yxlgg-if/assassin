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
 * @date 2021年3月30日 上午13:23:19
 */
public abstract class SocketChannel<R, W> implements IChannel<R, W> {

    /**
     * 获取socket
     * @author yxlgg-if
     * @date 2021年3月30日 上午13:33:00
     * @return
     */
    public abstract Socket getSocket();
    
    /**
     * 设置socket
     * @author yxlgg-if
     * @date 2021年3月30日 上午13:33:13
     * @param socket
     * @throws Exception
     */
    public abstract void setsocket(Socket socket) throws Exception;
    
    /**
     * 关闭socket
     * @author yxlgg-if
     * @date 2021年3月30日 上午13:33:34
     * @throws Exception
     */
    public abstract void closeSocket() throws IOException;

    @Override
    public void close() throws IOException {
        this.closeSocket();
    }
}
