/**
 * 
 */
package app.msgenum;

import org.apache.commons.lang3.StringUtils;

import app.model.ReplyCodeModel;

/**
 * ����˷��ص�״̬��
 * @author yxlgg-if
 * @date 2021��3��26�� ����7:19:26
 */
public enum StatusCodeEnum {
    
    SUCCESS("6666", "�ɹ�"),
    UNKNOW_MSG_TYPE("4001", "δ֪����Ϣ����"),
    NO_THIS_EXPOSE_PORT("4002", "δ֪�ı�¶�����˿�"),
    UNKNOW_ERR("0000", "δ֪����"),
    ;

    private String statusCode;
    private String statusName;
    
    StatusCodeEnum(String statusCode, String statusName) {
        // TODO �Զ����ɵĹ��캯�����
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
     * ͨ��״̬��statusCode��ȡö��ֵ
     * @author yxlgg-if
     * @date 2021��3��26�� ����10:16:18
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
