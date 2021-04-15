/**
 * 
 */
package app.servercls.literacyevent;

import java.net.Socket;

import app.base.Optional;
import app.channel.SocketChannel;
import app.model.MsgModel;
import app.msgenum.MsgTypeEnum;
import app.msgenum.StatusCodeEnum;
import app.servercls.config.IServiceConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yxlgg-if
 * @param <R>
 * @param <W>
 * @date 2021年4月2日 上午10:14:24
 */
@Slf4j
public class ReadMsg implements IReadMsg<MsgModel, MsgModel> {
	
	private IServiceConfig<MsgModel, MsgModel> config;
	
	private MsgModel msgModel;
	private MsgTypeEnum msgTypeEnum;
	private SocketChannel<? extends MsgModel, ? super MsgModel> socketChannel;
	/**
	 * 
	 */
	public ReadMsg(IServiceConfig<MsgModel, MsgModel> config) {
		// TODO 自动生成的构造函数存根
		this.config = config;
	}
	
	@Override
	public void readClientMsg(Socket clientSocket) throws Exception {
		Optional<? extends MsgModel> optional;
		try {
			socketChannel = this.config.newSocketChannel(clientSocket);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			log.error("Create channel exception.", e);
			throw e;
		}
		
		try {
			MsgModel read = socketChannel.read();
			optional = Optional.of(read);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Failed to read channel.");
			throw e;
		}
		
		setMsgTypeEnumAndModel(optional);
		processMsg(socketChannel, msgModel, msgTypeEnum);
		
	}
	
	/**
	 * 获取消息类型
	 * @author yxlgg-if
	 * @date 2021年4月2日 下午3:35:57
	 * @param optional
	 */
	@Override
	public void setMsgTypeEnumAndModel(Optional<? extends MsgModel> optional) {
		msgModel = optional.getValue();
		msgTypeEnum = MsgTypeEnum.matchEnumName(msgModel.getMsgType());
	}
	
	/**
	 * 处理消息
	 * @author yxlgg-if
	 * @date 2021年4月2日 下午3:54:03
	 * @param msgTypeEnum
	 */
	@Override
	public void processMsg(SocketChannel<? extends MsgModel, ? super MsgModel> socketChannel, MsgModel msgModel, MsgTypeEnum msgTypeEnum) throws Exception{
		// TODO 自动生成的方法存根
		if (MsgTypeEnum.CLIENT_CONTROL.equals(msgTypeEnum)) {
			IProcessMsg processTheClientControlMsg = new ProcessTheClientControlMsg(msgTypeEnum);
			processTheClientControlMsg.processMsg(socketChannel, msgModel);
		} else if (MsgTypeEnum.CLIENT_CONNECT.equals(msgTypeEnum)) {
			IProcessMsg processTheClientConnectMsg = new ProcessTheClientConnectMsg(msgTypeEnum);
			processTheClientConnectMsg.processMsg(socketChannel, msgModel);
		} else {
			log.warn("[{}] is a unknow mseeage type.", msgTypeEnum.toString());
			socketChannel.writeAndFlush(MsgModel.getMsgModelObject(msgModel.getMsgSeq(),
					MsgTypeEnum.MSG_REPLY, StatusCodeEnum.UNKNOW_MSG_TYPE.replyUnknowMsg()));
		}
	}

}
