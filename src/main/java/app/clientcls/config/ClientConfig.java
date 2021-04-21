/**
 * 
 */
package app.clientcls.config;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import app.channel.EncryptChannel;
import app.channel.SocketChannel;
import app.model.MsgModel;
import lombok.Data;

/**
 * 客户端配置
 * @author yxlgg-if
 * @date 2021年4月7日 下午6:53:55
 */
@Data
public class ClientConfig implements IClientConfig<MsgModel, MsgModel>{

    /**
     * 
     */
    private Integer exposePort;
    
    private String assassinClientHost;
    
    private Integer assassinPort;
    
    private String assassinServiceHost;
    
    private Integer assassinServerPort;
    
    private String aesBaseKey;
    
    private String autographsKey;
    
    private Charset charset = StandardCharsets.UTF_8;
    
    @Override
    public SocketChannel<? extends MsgModel, ? super MsgModel> newSocketChannel() {
        EncryptChannel encryptChannel = new EncryptChannel();
        encryptChannel.setAesBaseKey(aesBaseKey);
        encryptChannel.setCharset(charset);
        encryptChannel.setAutographsKey(autographsKey);
        Socket socket;
        try {
            socket = new Socket(this.getAssassinServiceHost(), this.getAssassinServerPort());
            encryptChannel.setsocket(socket);
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            return null;
        } 
        return encryptChannel;
    }

}
