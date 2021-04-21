/**
 * 
 */
package app.msgenum;

import org.apache.commons.lang3.StringUtils;

import app.model.ReplyCodeModel;

/**
 * 服务端返回的状态码
 * @author yxlgg-if
 * @date 2021年3月26日 下午7:19:26
 */
public enum StatusCodeEnum {
    
    SUCCESS("6666", "成功"),
    UNKNOW_MSG_TYPE("4001", "未知的消息类型"),
    NO_THIS_EXPOSE_PORT("4002", "未知的暴露公网端口"),
    UNKNOW_ERR("0000", "未知错误"),
    ;

    private String statusCode;
    private String statusName;
    
    StatusCodeEnum(String statusCode, String statusName) {
        // TODO 自动生成的构造函数存根
        this.statusCode = statusCode;
        this.statusName = statusName;
    }
    
    public String getStatusCode() {
        return statusCode;
    }
    
    public String getStatusName() {
        return statusName;
    }
    
    public ReplyCodeModel rpalyFailModel() {
        return ReplyCodeModel.replayFail(this);
    }
    
    public ReplyCodeModel replyUnknowPort() {
        return ReplyCodeModel.replyUnknowPort(this);
    }
    
    public ReplyCodeModel replyUnknowMsg() {
        return ReplyCodeModel.replyUnknowMsg(this);
    }
    /**
     * 通过状态码statusCode获取枚举值
     * @author yxlgg-if
     * @date 2021年3月26日 下午10:16:18
     * @param statusCode
     * @return
     */
    public static StatusCodeEnum getEnumByStatusCode(String statusCode) {
        if (StringUtils.isBlank(statusCode)) {
            return null;
        }
        
        for (StatusCodeEnum e:StatusCodeEnum.values()) {
            if (StringUtils.equals(statusCode, e.statusCode)) {
                return e;
            }
        }
        return null;
    }
    

}
