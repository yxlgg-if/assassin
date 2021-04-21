/**
 * 
 */
package app.servercls.literacyevent;

import app.PubConfig;
import app.PubConfig.ExposeMap;
import app.base.DefineFormat;
import app.channel.SocketChannel;
import app.model.MsgModel;
import app.model.ReplyCodeModel;
import app.model.SocketPairKeyModel;
import app.msgenum.MsgTypeEnum;
import app.msgenum.StatusCodeEnum;
import app.servercls.threads.ServerInteractiveThread;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yxlgg-if
 * @date 2021��4��2�� ����6:49:03
 */
@Slf4j
public class ProcessTheClientConnectMsg implements IProcessMsg {

    private MsgTypeEnum msgTypeEnum;
    
    public ProcessTheClientConnectMsg(MsgTypeEnum msgTypeEnum) {
        // TODO �Զ����ɵĹ��캯�����
        this.msgTypeEnum = msgTypeEnum;
    }

    @Override
    public boolean processMsg(SocketChannel<? extends MsgModel, ? super MsgModel> socketChannel, MsgModel msgModel)
            throws Exception {
        if (MsgTypeEnum.CLIENT_CONNECT.equals(msgTypeEnum)) {
            SocketPairKeyModel socketPairKeyModel = msgModel.getMsgData().toJavaObject(SocketPairKeyModel.class);
            Integer exposePort = DefineFormat.getExposePortBySocketPairKey(socketPairKeyModel.getSocketPairKey());
            ServerInteractiveThread serverInteractiveThread = SourceInteractiveThread.get(exposePort);
            
            if (serverInteractiveThread == null) {
                socketChannel.writeAndFlush(MsgModel.getMsgModelObject(msgModel.getMsgSeq(), MsgTypeEnum.MSG_REPLY,
                        StatusCodeEnum.NO_THIS_EXPOSE_PORT.replyUnknowPort()));
                return false;
            }
        
            // �ظ������������óɹ�
            socketChannel.writeAndFlush(MsgModel.getMsgModelObject(msgModel.getMsgSeq(), MsgTypeEnum.MSG_REPLY,
                    ReplyCodeModel.replaySuccess()));
            
            // ���н�������
            serverInteractiveThread.interactivePairDo(socketPairKeyModel.getSocketPairKey(), socketChannel.getSocket());
            
            return true;
        } else {
            log.warn("[{}] is a unknow message type.", msgTypeEnum.toString());
            socketChannel.writeAndFlush(MsgModel.getMsgModelObject(msgModel.getMsgSeq(),
                    MsgTypeEnum.MSG_REPLY, StatusCodeEnum.UNKNOW_MSG_TYPE.replyUnknowMsg()));
            return false;
        }
    }

    @Override
    public boolean checkExposePort(Integer exposePort) {
        // TODO �Զ����ɵķ������
        for (ExposeMap exposeMap : PubConfig.ExposeMapingArray) {
            if (exposeMap.exposePort == exposePort) {
                return true;
            }
        }
        return false;
    }

}
