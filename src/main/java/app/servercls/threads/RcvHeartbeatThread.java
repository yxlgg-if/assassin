/**
 * 
 */
package app.servercls.threads;

import app.channel.SocketChannel;
import app.model.MsgModel;
import app.msgenum.MsgTypeEnum;
import app.msgenum.StatusCodeEnum;
import app.servercls.literacyevent.RcvHeartbeat;
import lombok.extern.slf4j.Slf4j;

/**
 * 接收客户端发送的心跳检测
 * @author yxlgg-if
 * @date 2021年4月9日 下午6:39:15
 */
@Slf4j
public class RcvHeartbeatThread implements Runnable {

	private boolean isAlive = false;
	private Thread rcvHeartbeatThread = null;
	private SocketChannel<? extends MsgModel, ? super MsgModel> socketChannel;
	private Integer exposePort;
	
	public RcvHeartbeatThread(SocketChannel<? extends MsgModel, ? super MsgModel> socketChannel, Integer exposePort) {
		this.socketChannel = socketChannel;
		this.exposePort = exposePort;
	}
	
	public void startRcvHeartbeat() {
		isAlive = true;
		log.info("rcvHeartbeatThread [{}] starting...", this.exposePort);
		if (rcvHeartbeatThread == null || !rcvHeartbeatThread.isAlive()) {
			rcvHeartbeatThread = new Thread(this);
			rcvHeartbeatThread.setName("Assassain-server-rcvHeartbeat-" + String.valueOf(this.exposePort));
			rcvHeartbeatThread.start();
			log.info("rcvHeartbeatThread [{}] started.", this.exposePort);
		}
	}

	@Override
	public void run() {
		
		while(isAlive) {
			try {
				MsgModel msgModel = this.socketChannel.read();
				
				log.info("Heartbeat message received from the client.");
				boolean processTag = new RcvHeartbeat().processMsg(msgModel, this.socketChannel);
				
				if (!processTag) {
					this.socketChannel.writeAndFlush(MsgModel.getMsgModelObject(msgModel.getMsgSeq(),
							MsgTypeEnum.HEART_REPLY, StatusCodeEnum.UNKNOW_MSG_TYPE.replyUnknowMsg()));
				}
			} catch (Exception e) {
				log.error("Read heartbeat message exception.");
				stopStartRcvHeartbeat();
			}
		}

	}
	
	public void stopStartRcvHeartbeat() {
		isAlive = false;
		if (rcvHeartbeatThread != null) {
			rcvHeartbeatThread.interrupt();
			rcvHeartbeatThread = null;
		}
	}
}
