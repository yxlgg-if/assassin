/**
 * 
 */
package app.clientcls.config;

import java.nio.charset.Charset;

import app.channel.SocketChannel;

/**
 * @author yxlgg-if
 * @param <R>
 * @param <W>
 * @date 2021年4月7日 下午6:56:28
 */
public interface IClientConfig<R, W> {
    
    Integer getExposePort(); 
    
    String getAssassinClientHost();

    Integer getAssassinPort();

    String getAssassinServiceHost();

    Integer getAssassinServerPort();

    String getAesBaseKey();

    String getAutographsKey();

    Charset getCharset();
    
    SocketChannel<? extends R, ? super W> newSocketChannel();
}
