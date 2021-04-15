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
 * ��������
 * @author yxlgg-if
 * @date 2021��4��1�� ����6:41:02
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
		// TODO �Զ����ɵĹ��캯�����
	}

}
