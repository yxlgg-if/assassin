/**
 * 
 */
package app.channel;

import java.io.Closeable;
import java.nio.charset.Charset;

/**
 * ����ӿ�
 * @author yxlgg-if
 * @date 2021��3��26�� ����12:47:04
 */
public interface IChannel<R, W> extends Closeable {
    
    /**
     * �����ȡ����������    
     * @author yxlgg-if
     * @date 2021��3��26�� ����12:50:41
     * @return
     * @throws Exception
     */
    R read() throws Exception;
    

    /**
     * ���д�����������
     * @author yxlgg-if
     * @date 2021��3��26�� ����12:52:31
     * @param value
     * @throws Exception
     */
    void write(W value) throws Exception;
    
    /**
     * ˢ�����
     * @author yxlgg-if
     * @date 2021��3��26�� ����12:54:56
     * @throws Exception
     */
    void flush() throws Exception;
    
    void writeAndFlush(W value) throws Exception;
    
    default public void setCharset(Charset charset) {
        throw new IllegalStateException("��֧�ֵĲ���");
    };

}
