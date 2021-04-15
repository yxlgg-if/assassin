/**
 * 
 */
package app.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONObject;

import app.base.DefineFormat;
import app.msgenum.MsgTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 交互的消息模型
 * @author yxlgg-if
 * @date 2021年3月26日 上午13:26:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MsgModel implements JSONAware {

	// 消息序列
	private String msgSeq;
	
	// 消息类型
	private String msgType;
	
	// 消息内容
	private JSONObject msgData;
	
	/**
	 * 获取模型对象
	 * @author yxlgg-if
	 * @date 2021年3月30日 上午12:09:03
	 * @param msgTypeEnum
	 * @param msgData
	 * @return
	 */
	public static MsgModel getMsgModelObject(MsgTypeEnum msgTypeEnum, Object msgData) {
		return new MsgModel(DefineFormat.getMsgSeq(), msgTypeEnum.name(),
				msgData == null ? null : JSON.parseObject(JSON.toJSONString(msgData)));
	}
	
	/**
	 * 重载模型对象
	 * @author yxlgg-if
	 * @date 2021年3月30日 上午12:08:32
	 * @param msgSeq
	 * @param msgTypeEnum
	 * @param msgData
	 * @return
	 */
	public static MsgModel getMsgModelObject(String msgSeq, MsgTypeEnum msgTypeEnum, Object msgData) {
		return new MsgModel(msgSeq, msgTypeEnum.name(), 
				msgData == null ? null : JSON.parseObject(JSON.toJSONString(msgData)));
	}
	
//	public MsgModel() {
//		
//	}
	
    public MsgModel(MsgModel msgModel) {
        this.setValue(msgModel);
    }

    public void setValue(MsgModel msgModel) {
        this.setMsgSeq(msgModel.getMsgSeq());
        this.setMsgType(msgModel.getMsgType());
        this.setMsgData(msgModel.getMsgData());
    }
    
	@Override
	public String toJSONString() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msgSeq", msgSeq);
		jsonObject.put("msgType", msgType);
		jsonObject.put("msgData", msgData);
		return jsonObject.toJSONString();
	}

}
