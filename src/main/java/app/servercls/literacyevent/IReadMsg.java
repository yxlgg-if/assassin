/**
 * 
 */
package app.servercls.literacyevent;

import java.net.Socket;

import app.base.Optional;
import app.channel.SocketChannel;
import app.model.MsgModel;
import app.msgenum.MsgTypeEnum;

/**
 * 读取客户端消息接口
 * @author yxlgg-if
 * @param <R>
 * @param <W>
 * @date 2021年4月2日 下午7:02:55
 */
public interface IReadMsg<R, W> {
	void readClientMsg(Socket clientSocket) throws Exception;
	
	void processMsg(SocketChannel<? extends R, ? super W> socketChannel, MsgModel msgModel, MsgTypeEnum msgTypeEnum) throws Exception;
	
	void setMsgTypeEnumAndModel(Optional<? extends MsgModel> optional);
}
