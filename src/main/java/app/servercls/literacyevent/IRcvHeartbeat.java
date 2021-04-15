/**
 * 
 */
package app.servercls.literacyevent;


import app.channel.SocketChannel;

/**
 * ���տͻ���������⴦��ӿ�
 * @author yxlgg-if
 * @date 2021��4��9�� ����7:13:40
 */
public interface IRcvHeartbeat<R, W> {
	boolean checkMsgType(R msgModel);
	boolean processMsg(R msgModel, SocketChannel<? extends R, ? super W> socketChannel) throws Exception;
}
