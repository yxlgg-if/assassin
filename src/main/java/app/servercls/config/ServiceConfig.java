package app.servercls.config;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import app.channel.EncryptChannel;
import app.channel.SocketChannel;
import app.model.MsgModel;
import lombok.Data;

/**
 * 服务配置
 * @author yxlgg-if
 * @date 2021年4月1日 下午6:41:02
 */
@Data
public class ServiceConfig implements IServiceConfig<MsgModel, MsgModel>{
    
    private Integer exposePort;
    
    private String assassinClientHost;
    
    private Integer assassinPort;
    
    private String assassinServiceHost;
    
    private Integer assassinServerPort;
    
    private String aesBaseKey;
    
    private String autographsKey;
    
    private Charset charset = StandardCharsets.UTF_8;
    
    @Override
    public SocketChannel<? extends MsgModel, ? super MsgModel> newSocketChannel(Socket socket) throws IOException {
        EncryptChannel encryptChannel = new EncryptChannel();
        encryptChannel.setAesBaseKey(aesBaseKey);
        encryptChannel.setCharset(charset);
        encryptChannel.setAutographsKey(autographsKey);
        encryptChannel.setsocket(socket);
        return encryptChannel;
    }
    
    public ServiceConfig() {
        // TODO 自动生成的构造函数存根
    }

}
