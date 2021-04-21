/**
 * 
 */
package app.servercls.config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

import app.channel.SocketChannel;


/**
 * ��������
 * @author yxlgg-if
 * @param <R>
 * @param <W>
 * @date 2021��4��2�� ����12:18:34
 */
public interface IServiceConfig<R, W> {
    
    Integer getExposePort();

    String getAssassinClientHost();

    Integer getAssassinPort();

    String getAssassinServiceHost();

    Integer getAssassinServerPort();

    String getAesBaseKey();

    String getAutographsKey();

    Charset getCharset();
    
    SocketChannel<? extends R, ? super W> newSocketChannel(Socket listenSocket) throws IOException;
    
    default public ServerSocket newServerSocket() throws IOException {
        return new ServerSocket(this.getExposePort());
    }
}
