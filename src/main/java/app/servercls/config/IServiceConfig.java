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
 * 服务配置
 * @author yxlgg-if
 * @param <R>
 * @param <W>
 * @date 2021年4月2日 上午12:18:34
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
