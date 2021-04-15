/**
 * 
 */
package app.model;


import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONObject;

import app.msgenum.StatusCodeEnum;
import lombok.Data;

/**
 * 服务端回复状态信息模型
 * @author yxlgg-if
 * @date 2021年3月26日 下午8:31:05
 */
@Data
public class ReplyCodeModel implements JSONAware {
	
	private String replyCode;
	private String replyMsg;
	private Object data;

	public ReplyCodeModel(String replyCode, String replayMsg, Object data) {
		// TODO 自动生成的构造函数存根
		this.replyCode = replyCode;
		this.replyMsg = replayMsg;
		this.data = data;
	}
	
	public static ReplyCodeModel replaySuccess() {
		return new ReplyCodeModel(StatusCodeEnum.SUCCESS.getStatusCode(),
				StatusCodeEnum.SUCCESS.getStatusName(), null);
	}

	public static ReplyCodeModel replayFail(Object data) {
		return new ReplyCodeModel(StatusCodeEnum.UNKNOW_ERR.getStatusCode(),
				StatusCodeEnum.UNKNOW_ERR.getStatusName(), data);
	}
	
	public static ReplyCodeModel replyUnknowPort(Object data) {
		return new ReplyCodeModel(StatusCodeEnum.NO_THIS_EXPOSE_PORT.getStatusCode(),
				StatusCodeEnum.NO_THIS_EXPOSE_PORT.getStatusName(), data);
	}
	
	public static ReplyCodeModel replyUnknowMsg(Object data) {
		return new ReplyCodeModel(StatusCodeEnum.UNKNOW_MSG_TYPE.getStatusCode(),
				StatusCodeEnum.UNKNOW_MSG_TYPE.getStatusName(), data);
	}
//    public ReplyCodeModel set(String fieldStr, Object object) {
//        Field field = null;
//        try {
//            field = this.getClass().getDeclaredField(fieldStr);
//        } catch (NoSuchFieldException | SecurityException e) {
//            log.warn("ResultModel get field faild!", e);
//            return this;
//        }
//
//        field.setAccessible(true);
//        try {
//            field.set(this, object);
//        } catch (IllegalArgumentException | IllegalAccessException e) {
//            log.warn("ResultModel set field faild!", e);
//            return this;
//        }
//
//        return this;
//    }
    
	@Override
	public String toString() {
		return this.toJSONString();
	}
	
	@Override
	public String toJSONString() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("replyCode", replyCode);
		jsonObject.put("replyMsg", replyMsg);
		jsonObject.put("data", data);
		
		return jsonObject.toJSONString();
	}
}
