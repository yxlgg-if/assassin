package app.servercls.literacyevent;

import app.PubConfig;
import app.PubConfig.ExposeMap;
import app.channel.SocketChannel;
import app.model.ExposePortModel;
import app.model.MsgModel;
import app.model.ReplyCodeModel;
import app.msgenum.MsgTypeEnum;
import app.msgenum.StatusCodeEnum;
import app.servercls.threads.RcvHeartbeatThread;
import app.servercls.threads.ServerInteractiveThread;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessTheClientControlMsg implements IProcessMsg{

	private MsgTypeEnum msgTypeEnum;
	
	public ProcessTheClientControlMsg(MsgTypeEnum msgTypeEnum) {
		// TODO 自动生成的构造函数存根
		this.msgTypeEnum = msgTypeEnum;
	}
	
	@Override
	public boolean processMsg(SocketChannel<? extends MsgModel, ? super MsgModel> socketChannel, MsgModel msgModel)
			throws Exception {
		if (MsgTypeEnum.CLIENT_CONTROL.equals(msgTypeEnum)) {
			ExposePortModel exposePortModel = msgModel.getMsgData().toJavaObject(ExposePortModel.class);
			
			// 检查暴露端口是否存在
			if (!checkExposePort(exposePortModel.getExposePort())) {
				socketChannel.writeAndFlush(MsgModel.getMsgModelObject(msgModel.getMsgSeq(), 
						MsgTypeEnum.MSG_REPLY, StatusCodeEnum.NO_THIS_EXPOSE_PORT.replyUnknowPort()));
				log.warn("Unknow port [{}].", exposePortModel.getExposePort());
				return false;
			}
			
			// 开启处理第三方消息的交互线程
			ServerInteractiveThread interactiveThread = SourceInteractiveThread.get(exposePortModel.getExposePort());
			
			if (interactiveThread == null) {
				return false;
			}
			
			interactiveThread.setClientSocket(socketChannel.getSocket());
			
			
			interactiveThread.start();
			
			// 开启心跳监测接收线程
			new RcvHeartbeatThread(socketChannel, exposePortModel.getExposePort()).startRcvHeartbeat();
			
			// 回复消息
			socketChannel.writeAndFlush(MsgModel.getMsgModelObject(msgModel.getMsgSeq(),
					MsgTypeEnum.MSG_REPLY, ReplyCodeModel.replaySuccess()));
			
			return true;
			
		} else {
			log.warn("[{}] is a unknow message type.", msgTypeEnum.toString());
			socketChannel.writeAndFlush(MsgModel.getMsgModelObject(msgModel.getMsgSeq(),
					MsgTypeEnum.MSG_REPLY, StatusCodeEnum.UNKNOW_MSG_TYPE.replyUnknowMsg()));
			return false;
		}
		
	}

	@Override
	public boolean checkExposePort(Integer exposePort) {
		// TODO 自动生成的方法存根
		for (ExposeMap exposeMap : PubConfig.ExposeMapingArray) {
			if (exposeMap.exposePort == exposePort) {
				return true;
			}
		}
		return false;
	}

}
