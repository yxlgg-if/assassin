/**
 * 
 */
package app.servercls.literacyevent;

import app.channel.SocketChannel;
import app.model.MsgModel;

/**
 * 消息处理接口
 * @author yxlgg-if
 * @date 2021年4月6日 上午13:54:00
 */
public interface IProcessMsg {
	boolean processMsg(SocketChannel<? extends MsgModel, ? super MsgModel> socketChannel, MsgModel msgModel) throws Exception;
	
	boolean checkExposePort(Integer exposePort);
}
