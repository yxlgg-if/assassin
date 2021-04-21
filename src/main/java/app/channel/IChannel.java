/**
 * 
 */
package app.channel;

import java.io.Closeable;
import java.nio.charset.Charset;

/**
 * 隧道接口
 * @author yxlgg-if
 * @date 2021年3月26日 上午12:47:04
 */
public interface IChannel<R, W> extends Closeable {
    
    /**
     * 隧道读取输入流方法    
     * @author yxlgg-if
     * @date 2021年3月26日 上午12:50:41
     * @return
     * @throws Exception
     */
    R read() throws Exception;
    

    /**
     * 隧道写入输出流方法
     * @author yxlgg-if
     * @date 2021年3月26日 上午12:52:31
     * @param value
     * @throws Exception
     */
    void write(W value) throws Exception;
    
    /**
     * 刷入磁盘
     * @author yxlgg-if
     * @date 2021年3月26日 上午12:54:56
     * @throws Exception
     */
    void flush() throws Exception;
    
    void writeAndFlush(W value) throws Exception;
    
    default public void setCharset(Charset charset) {
        throw new IllegalStateException("不支持的操作");
    };

}
