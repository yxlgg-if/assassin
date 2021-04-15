/**
 * 
 */
package app.servercls.literacyevent;

import app.channel.SocketChannel;
import app.model.MsgModel;

/**
 * ��Ϣ����ӿ�
 * @author yxlgg-if
 * @date 2021��4��6�� ����13:54:00
 */
public interface IProcessMsg {
	boolean processMsg(SocketChannel<? extends MsgModel, ? super MsgModel> socketChannel, MsgModel msgModel) throws Exception;
	
	boolean checkExposePort(Integer exposePort);
}
