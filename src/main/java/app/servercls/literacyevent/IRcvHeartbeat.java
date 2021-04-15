/**
 * 
 */
package app.servercls.literacyevent;


import app.channel.SocketChannel;

/**
 * 接收客户端心跳检测处理接口
 * @author yxlgg-if
 * @date 2021年4月9日 下午7:13:40
 */
public interface IRcvHeartbeat<R, W> {
	boolean checkMsgType(R msgModel);
	boolean processMsg(R msgModel, SocketChannel<? extends R, ? super W> socketChannel) throws Exception;
}
