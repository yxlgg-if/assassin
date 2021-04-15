/**
 * 
 */
package app.channel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONObject;

import app.base.ConvertTools;
import app.model.EncryptMsgModel;
import app.model.MsgModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yxlgg-if
 * @date 2021��3��30�� ����12:57:03
 */
public class EncryptChannel extends SocketChannel<MsgModel, MsgModel>{
	
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private ReentrantLock writeLock = new ReentrantLock();
	private ReentrantLock readLock = new ReentrantLock();
	
	@Setter
	@Getter
	private String autographsKey;
	
	@Setter
	@Getter
	private String aesBaseKey;
	
	@Setter
	@Getter
	private Charset charset = StandardCharsets.UTF_8;
	
	// ��ʱ
	private Long timeoutMills = 5000L;

	/**
	 * 
	 */
	public EncryptChannel() {
		// TODO �Զ����ɵĹ��캯�����
	}

	@Override
	public MsgModel read() throws Exception {
		byte[] msgArray = readFromInputStream();
		String msgString = new String(msgArray, charset);
		// JSON�ַ���ת��ΪJSON����
		JSONObject msgJson = JSON.parseObject(msgString);
		// JSON����ת����Java����
		EncryptMsgModel encryptMsgModel = msgJson.toJavaObject(EncryptMsgModel.class);
		if (Math.abs(System.currentTimeMillis() - encryptMsgModel.getTimestamp()) > timeoutMills) {
			throw new IllegalStateException("��Ϣ��ʱ");
		}
		
		// ���ǩ��ģ��
		boolean checkAutograph = encryptMsgModel.checkAutographMsg(autographsKey);
		if (!checkAutograph) {
			throw new IllegalStateException("ǩ������");
		}
		encryptMsgModel.decryptMsg(aesBaseKey);
		
		return encryptMsgModel;
	}

	@Override
	public void write(MsgModel value) throws Exception {
		String msgString = null;
		EncryptMsgModel encryptMsgModel = new EncryptMsgModel(value);
		encryptMsgModel.setCharset(charset.name());
		encryptMsgModel.setEncryptValue(aesBaseKey, autographsKey);
        if (encryptMsgModel instanceof JSONAware) {
        	msgString = ((JSONAware) encryptMsgModel).toJSONString();
        } else {
        	msgString = JSON.toJSONString(encryptMsgModel);
        }
		// �ַ���ת��Ϊ��������
		byte[] msgByteArray = msgString.getBytes(charset);
		writeIntoOutputStream(msgByteArray);
	}

	@Override
	public void flush() throws Exception {
		writeLock.lock();
		try {
			this.getOutputStream().flush();
		} finally {
			writeLock.unlock();
		}
		
	}

	@Override
	public Socket getSocket() {
		// TODO �Զ����ɵķ������
		return socket;
	}

	@Override
	public void setsocket(Socket socket) throws IOException {
		this.socket = socket;
	}

	@Override
	public void closeSocket() throws IOException {
		this.socket.close();
		
	}
	
	/**
	 * ����������ȡ��Ϣ
	 * @author yxlgg-if
	 * @date 2021��3��30�� ����7:35:51
	 * @return
	 * @throws Exception
	 */
	public byte[] readFromInputStream() throws Exception{
		readLock.lock();
		try {
			InputStream inputStream = getInputStream();
			// Լ��ǰ��λΪ��Ϣ�ĳ��ȣ�byte������Ϊһ��������
			byte[] byteArray = new byte[4];
			// ��ȡ�ĸ��ֽڵı�������byte������Ϊһ����������ÿ�δ���ͻ�����һ����С��byte.length��������
			inputStream.read(byteArray);
			// ��ǰ�ĸ��ֽڵı�����ת��Ϊ��������ǰ0λ�ã�����ʾ���ȵ�4���ֽ��⣩���������ĳ��Ⱦ�����Ϣ������
			int msgLength = ConvertTools.bytesToInt(byteArray);
			// ��ȡ��Ϣ
			byte[] msgArray = new byte[msgLength];
			inputStream.read(msgArray, 0, msgLength);
			return msgArray;
		} finally {
			readLock.unlock();
		}
	}
	
	/**
	 * ����������д�뵽�����
	 * @author yxlgg-if
	 * @date 2021��3��30�� ����7:36:40
	 * @param value
	 * @throws Exception
	 */
	public void writeIntoOutputStream(byte[] value) throws Exception{
		writeLock.lock();
		try {
			OutputStream outputStream = getOutputStream();
			int msgLength = value.length;
			outputStream.write(ConvertTools.intToBytes(msgLength));
			outputStream.write(value);
		} finally {
			// TODO: handle finally clause
			writeLock.unlock();
		}
	}
	
	private InputStream getInputStream() throws IOException {
		if (this.inputStream == null ) {
			return this.socket.getInputStream();
		}
		return this.inputStream;
	}
	
	private OutputStream getOutputStream() throws IOException {
		if (this.outputStream == null) {
			return this.socket.getOutputStream();
		}
		return this.outputStream;
	}

	@Override
	public void writeAndFlush(MsgModel value) throws Exception {
		this.write(value);
		this.flush();
	}
}
