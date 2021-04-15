/**
 * 
 */
package app.servercls.threads;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import app.api.IAssassinThread;
import app.base.DefineFormat;
import app.channel.SocketChannel;
import app.interactive.InteractivePair;
import app.model.MsgModel;
import app.model.SocketPairKeyModel;
import app.msgenum.MsgTypeEnum;
import app.servercls.config.IServiceConfig;
import app.servercls.literacyevent.SourceInteractiveThread;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * �Ե��������������ݽ��н���
 * @author yxlgg-if
 * @param <R>
 * @param <W>
 * @date 2021��4��6�� ����7:00:03
 */
@Slf4j
public class ServerInteractiveThread implements Runnable, IAssassinThread {
	
	// ͨ����¶�������˿ڽ�����socketServer
	private ServerSocket exposeServerSocket;
	// �ͻ��˳�����socket
	@Setter
	private Socket clientSocket;
	// �ͻ��˶�����socket���ӿͻ��˷���connect��
//	private Socket clientConnSocket;
	private IServiceConfig<MsgModel, MsgModel> config;
	private boolean isAlive = false;
	@Getter
	private Integer exposePort;
	private SocketChannel<? extends MsgModel, ? super MsgModel> socketChannel; 
	private Map<String, InteractivePair> socketPairMap = new ConcurrentHashMap<>();
	private Thread interactiveThread = null;
	
	/**
	 * @throws IOException 
	 * 
	 */
	public ServerInteractiveThread(IServiceConfig<MsgModel, MsgModel> config) throws IOException {
		// TODO �Զ����ɵĹ��캯�����
		this.config = config;
		this.exposeServerSocket = this.config.newServerSocket();
		this.exposePort = this.config.getExposePort();
		log.info("Created socketServer [{}].", this.config.getExposePort());
	}
	
	public boolean sendWaitMsgToClient(String SocketPairKey) {
		log.info("Received a new third-party[{}] request.", this.exposePort);
		try {
			socketChannel = this.config.newSocketChannel(clientSocket);
		} catch (IOException e1) {
			// TODO �Զ����ɵ� catch ��
			log.error("Failed to create socketChannel.");
//			e1.printStackTrace();
			return false;
		}
		
		MsgModel msgModel = MsgModel.getMsgModelObject(MsgTypeEnum.SERVER_WAIT_CLIENT, new SocketPairKeyModel(SocketPairKey));
		try {
			socketChannel.writeAndFlush(msgModel);
		} catch (Exception e) {
//			e.printStackTrace();
			log.error("Send SERVER_WAIT_CLIENT[{}] message exception.", this.exposePort);
			return false;
		}
		return true;
	}
	
	public InteractivePair newInteractivePair(String socketPairKey, Socket otherSocket) {
		InteractivePair interactivePair = new InteractivePair();
//		interactivePair.setSendSocket(clientConnSocket);
		interactivePair.setRcvdSocket(otherSocket);
		interactivePair.setSocketPairKey(socketPairKey);
//		interactivePair.createPair();
		return interactivePair;
	}
	
	public void interactivePairDo(String socketPairKey, Socket clientConnSocket) {
		InteractivePair interactivePair = socketPairMap.get(socketPairKey);
		if (interactivePair == null) {
			log.warn("socketPairKey[{}] not found.", socketPairKey);
			return;
		}
		interactivePair.setSendSocket(clientConnSocket);
		interactivePair.createPair();
		
	}
	
	public void start() {
		isAlive = true;
		if (interactiveThread == null || !interactiveThread.isAlive()) {
			log.info("The service is starting to listen for third-party requests on [{}].", this.exposePort);
			interactiveThread = new Thread(this);
			interactiveThread.setName("Assassin-server-listen-" + String.valueOf(this.exposePort));
			interactiveThread.start();
		} else {
			log.info("The service is already started to listen for third-party requests on [{}].", this.exposePort);
			return;
		}
		log.info("The service is started to listen for third-party requests on [{}].", this.exposePort);
	}

	/**
	 * ���յ���������
	 */
	@Override
	public void run() {
		// TODO �Զ����ɵķ������
		while (isAlive) {
			try {
				Socket otherSocket = exposeServerSocket.accept();
				String socketPairKey = DefineFormat.getSocketPairKey(exposePort);
				InteractivePair interactivePair = newInteractivePair(socketPairKey, otherSocket);
				interactivePair.setAssassinThread(this);
				interactivePair.setSocketPairKey(socketPairKey);
				socketPairMap.put(socketPairKey, interactivePair);
				boolean senWaitTag = sendWaitMsgToClient(socketPairKey);
				if (!senWaitTag) {
					socketPairMap.remove(socketPairKey);
					interactivePair.close();
					// ������Ϣ��ͬ��û�д��ڵ�������
					stopServerInteractiveThread();
					continue;
				}
			} catch (IOException e) {
				log.error("Unknow exception.", this.config.getExposePort());
				stopServerInteractiveThread();
			}
		}
		
	}

	public void stopServerInteractiveThread() {
		log.info("stop ServerInteractiveThread[{}].", this.exposePort);
		this.isAlive = false;
		if (clientSocket != null) {
			try {
				clientSocket.close();
			} catch (IOException e) {
				
			}
			clientSocket = null;	
		}
		
		// �����쳣���׹رտ����ڹ����Ķ˿�
//		if (exposeServerSocket != null) {
//			try {
//				exposeServerSocket.close();
//			} catch (IOException e) {
//
//			}
//			
//			exposeServerSocket = null;
//			
//		}
		
		if (interactiveThread != null) {
			interactiveThread.interrupt();
			interactiveThread = null;
		}
	}
	

	public void cancel() {
		stopServerInteractiveThread();
		
		// �����쳣���׹رտ����ڹ����Ķ˿�
		if (exposeServerSocket != null) {
			try {
				exposeServerSocket.close();
			} catch (IOException e) {

			}
			exposeServerSocket = null;
			SourceInteractiveThread.serverInteractiveMap.remove(exposePort);
		}
		
	}
	
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
