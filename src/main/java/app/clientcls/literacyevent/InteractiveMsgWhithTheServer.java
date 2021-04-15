package app.clientcls.literacyevent;

import java.io.IOException;
import java.net.Socket;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

import app.base.ThreadPool;
import app.channel.SocketChannel;
import app.clientcls.config.IClientConfig;
import app.clientcls.threads.ClientMainThread;
import app.interactive.InteractivePair;
import app.model.ExposePortModel;
import app.model.MsgModel;
import app.model.ReplyCodeModel;
import app.model.SocketPairKeyModel;
import app.msgenum.MsgTypeEnum;
import app.msgenum.StatusCodeEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InteractiveMsgWhithTheServer {

	private SocketChannel<? extends MsgModel, ? super MsgModel> socketChannel;
	private IClientConfig<MsgModel, MsgModel> config;
	private ClientMainThread clientMainThread;
	
	public InteractiveMsgWhithTheServer(IClientConfig<MsgModel, MsgModel> config,
			SocketChannel<? extends MsgModel, ? super MsgModel> socketChannel, 
			ClientMainThread clientMainThread) {
		// TODO 自动生成的构造函数存根
		this.config = config;
		this.socketChannel = socketChannel;
		this.clientMainThread =clientMainThread;
	}

	public boolean interactiveMsg() throws Exception {
		MsgModel msgModel = MsgModel.getMsgModelObject(MsgTypeEnum.CLIENT_CONTROL,
				new ExposePortModel(this.config.getExposePort()));
		this.socketChannel.writeAndFlush(msgModel);
		MsgModel rcvdMsg = this.socketChannel.read();
		log.info("server reply [CLIENT_CONTROL]:{}", rcvdMsg);
		
		ReplyCodeModel javaObject = rcvdMsg.getMsgData().toJavaObject(ReplyCodeModel.class);
		if (StringUtils.equals(StatusCodeEnum.SUCCESS.getStatusCode(), javaObject.getReplyCode())) {
			return true;
		}
		return false;
	}
	
	public void waitServerMsg() throws Exception {
		MsgModel msgModel = this.socketChannel.read();
		processMsgFromServer(msgModel);
	}
	
	public void processMsgFromServer(MsgModel msgModel) {
		log.info("Server [{}:{}] command received.", this.config.getAssassinClientHost(), this.config.getAssassinServerPort());
		
		String msgType = msgModel.getMsgType();
		MsgTypeEnum msgTypeEnum = MsgTypeEnum.matchEnumName(msgType);
		
		if (MsgTypeEnum.HEART_REPLY.equals(msgTypeEnum)) {
			return;
		} else if (MsgTypeEnum.SERVER_WAIT_CLIENT.equals(msgTypeEnum)) {
			JSONObject jsonObject = msgModel.getMsgData();
			SocketPairKeyModel socketPairKeyModel = jsonObject.toJavaObject(SocketPairKeyModel.class);
			ThreadPool.excuteCachedThreadpool(new Runnable() {
				
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					processConnect(socketPairKeyModel);
				}
			});
		} else {
			log.error("Unprocessable message.");
		}
		
	}
	
	public boolean processConnect(SocketPairKeyModel socketPairKeyModel) {
		// 建立指向本地服务的sock
		Socket loaclSocket = null;
		try {
			loaclSocket = new Socket(this.config.getAssassinClientHost(), this.config.getAssassinPort());
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			log.error("Failed to created socket [{}:{}].", this.config.getAssassinClientHost(),
					this.config.getAssassinPort());
			return false;
		} 
		
		SocketChannel<? extends MsgModel, ? super MsgModel> pairSocketChannel = null;
		try {
			// 建立指向服务端的socket
			pairSocketChannel = this.config.newSocketChannel();
			
			MsgModel msgModel = MsgModel.getMsgModelObject(MsgTypeEnum.CLIENT_CONNECT, 
					new SocketPairKeyModel(socketPairKeyModel.getSocketPairKey()));
			pairSocketChannel.writeAndFlush(msgModel);
			
			// 读取回复
			MsgModel rcvdMsg = pairSocketChannel.read();
			log.info("server reply [CLIENT_CONNECT]:{}", rcvdMsg);
			
			ReplyCodeModel javaObject = rcvdMsg.getMsgData().toJavaObject(ReplyCodeModel.class);
			if (!StringUtils.equals(StatusCodeEnum.SUCCESS.getStatusCode(), javaObject.getReplyCode())) {
				throw new RuntimeException("Binding failed.");
			}
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			log.error("failed to created socket channel {}:{}<-->{}:{}", this.config.getAssassinServiceHost()
					, this.config.getAssassinServerPort(), this.config.getAssassinClientHost()
					, this.config.getAssassinPort());
			
			try {
				loaclSocket.close();
			} catch (IOException e2) {

			}
			
			if (pairSocketChannel != null) {
				try {
					pairSocketChannel.closeSocket();
				} catch (IOException e1) {
					
				}
			}
			return false;
		}
		
		// 进行交互传输
		InteractivePair interactivePair = new InteractivePair();
		interactivePair.setSendSocket(pairSocketChannel.getSocket());
		interactivePair.setRcvdSocket(loaclSocket);
		interactivePair.setAssassinThread(this.clientMainThread);
		interactivePair.setSocketPairKey(socketPairKeyModel.getSocketPairKey());
		boolean createPairTag = interactivePair.createPair();
		
		if (!createPairTag) {
			interactivePair.close();
			return false;
		}
		
		this.clientMainThread.putSocketPairMap(socketPairKeyModel.getSocketPairKey(), interactivePair);
		
		return true;	
	}
}
