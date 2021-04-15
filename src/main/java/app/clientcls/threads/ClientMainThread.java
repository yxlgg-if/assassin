/**
 * 
 */
package app.clientcls.threads;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import app.api.IAssassinThread;
import app.channel.SocketChannel;
import app.clientcls.config.ClientConfig;
import app.clientcls.config.IClientConfig;
import app.clientcls.literacyevent.InteractiveMsgWhithTheServer;
import app.interactive.InteractivePair;
import app.model.MsgModel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yxlgg-if
 * @date 2021��4��7�� ����6:26:55
 */
@Slf4j
public class ClientMainThread implements Runnable, IAssassinThread {
	
	private IClientConfig<MsgModel, MsgModel> config;
	private SocketChannel<? extends MsgModel, ? super MsgModel> socketChannel;
	private boolean isAlive = false;
	private Thread clientThread = null;
	private InteractiveMsgWhithTheServer interactiveMsgWhithTheServer;
	private Map<String, InteractivePair> socketPairMap = new ConcurrentHashMap<>();
	private SendHeartbeatThread sendHeartBeatThread = null;
	private boolean sendClientControlTag = false;

	/**
	 * 
	 */
	public ClientMainThread(ClientConfig config) {
		// TODO �Զ����ɵĹ��캯�����
		this.config = config;
	}
	
	public void start() {
		this.isAlive = true;
		if (clientThread == null || !clientThread.isAlive()) {
			clientThread = new Thread(this);
			clientThread.setName("Assassin-client-" + String.valueOf(this.config.getAssassinServerPort()) + 
					"-" + String.valueOf(this.config.getExposePort() + "-" + String.valueOf(this.config.getAssassinPort())));
			clientThread.start();
		}
	}
	
	public boolean createSocketChannel() {
		// ����ָ�����˵�socket����ͨ��
		socketChannel = this.config.newSocketChannel();
		if (socketChannel == null) {
			log.error("Fail to create sock [{}:{}]", this.config.getAssassinServiceHost(), this.config.getAssassinServerPort());
			return false;
		}
		return true;
	}

	public boolean interactiveMsg() throws Exception {
		boolean replyTag = false;
		boolean channelTag;
		channelTag = this.createSocketChannel();
		if (channelTag) {
			interactiveMsgWhithTheServer = new InteractiveMsgWhithTheServer(config, socketChannel, this);
			replyTag = interactiveMsgWhithTheServer.interactiveMsg();
		} else {
			isAlive = false;
			
		}
		return replyTag;
	}
	
	// ����ֵ��Ϊ��������
	public boolean sendClientControl() {
		// ����������˵�ͨ��
		try {
			sendClientControlTag = interactiveMsg();
		} catch (Exception e1) {
			log.error("[CLIENT_CONTROL] messgae interaction exception.");
			sendClientControlTag = false;
			this.stopClientThread();
			return sendClientControlTag;
			
		}	
		return sendClientControlTag;
	}
	
	@Override
	public void run() {
		// ����������˵�ͨ��
		if (!sendClientControlTag) {
			this.sendClientControl();
		}
		
		// ��������������������ӵ�ʱ���ʧ�������ɷ���������������
		if (sendHeartBeatThread == null || !this.sendHeartBeatThread.isAlive()) {
			if (this.sendHeartBeatThread != null) {
				sendHeartBeatThread.start();
			} else {
				sendHeartBeatThread = new SendHeartbeatThread(config, socketChannel, this);
				sendHeartBeatThread.start();
			}
		}
		
		if (!sendClientControlTag) {
			isAlive = false;
		} 
		
		while (isAlive) {
			try {
				interactiveMsgWhithTheServer.waitServerMsg();
			} catch (Exception e) {
				log.warn("ClientThread[{}] waitServerMsg is exception,will excute stopClientThread.", this.config.getExposePort());
				this.stopClientThread();
				return;
			}
		}
	}
	
	public void stopClientThread() {
		isAlive = false;
		if (clientThread != null) {
			clientThread.interrupt();
			clientThread = null;
		}
	}
	
	public void putSocketPairMap(String socketPairKey, InteractivePair interactivePair) {
		this.socketPairMap.put(socketPairKey, interactivePair);
	}

	@Override
	public boolean stopInteractive(String socketPairKey) {
		log.debug("stop socketPair[{}].", socketPairKey);
		InteractivePair interactivePair = socketPairMap.get(socketPairKey);
		if (interactivePair == null) {
			return false;
		}
		interactivePair.close();
		socketPairMap.remove(socketPairKey);
		return true;
	}

}
