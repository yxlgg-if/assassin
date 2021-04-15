/**
 * 
 */
package app.servercls.literacyevent;

import app.channel.SocketChannel;
import app.model.MsgModel;
import app.model.ReplyCodeModel;
import app.msgenum.MsgTypeEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yxlgg-if
 * @date 2021年4月9日 下午7:28:32
 */
@Slf4j
public class RcvHeartbeat implements IRcvHeartbeat<MsgModel, MsgModel>{

	/**
	 * 
	 */
	public RcvHeartbeat() {
		// TODO 自动生成的构造函数存根
	}

	@Override
	public boolean checkMsgType(MsgModel msgModel) {
		log.info("Heartbeat message:[{}]", msgModel);
		MsgTypeEnum msgTypeEnum = MsgTypeEnum.matchEnumName(msgModel.getMsgType());
		if (MsgTypeEnum.HEART_TEST.equals(msgTypeEnum)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean processMsg(MsgModel msgModel, SocketChannel<? extends MsgModel, ? super MsgModel> socketChannel)
			throws Exception {
		if (this.checkMsgType(msgModel)) {
			MsgModel sendModel = MsgModel.getMsgModelObject(msgModel.getMsgSeq(), MsgTypeEnum.HEART_REPLY, 
					ReplyCodeModel.replaySuccess());
			socketChannel.writeAndFlush(sendModel);
			return true;
		}
		return false;
	}


}
