/**
 * 
 */
package app.msgenum;

import org.apache.commons.lang3.StringUtils;

/**
 * 消息类型
 * @author yxlgg-if
 * @date 2021年3月26日 上午13:33:11
 */
public enum MsgTypeEnum {
    
    UNKNOW("未知的消息类型"),
    MSG_REPLY("消息回复"),
    HEART_TEST("发送心跳"),
    HEART_REPLY("心跳回复"),
    SERVER_WAIT_CLIENT("通知客户端建立短连接"),
    // 请求建立连接对第三方数据流进行交互
    CLIENT_CONNECT("客户端请求建立短链接连接"),
    // 客户端和服务端进行内部消息通知的端口
    CLIENT_CONTROL("客户端请求建立长连接连接"),
    ;
    
    private String enumDescribe;

    MsgTypeEnum(String enumDescribe) {
        // TODO 自动生成的构造函数存根
        this.enumDescribe = enumDescribe;
    }
    
    public String getEnumDescribe() {
        return enumDescribe;
    }
    
    /**
     * 对传入的字符串对象与枚举进行匹配，匹配到相同名字的枚举则返回该枚举，否则返回null
     * @author yxlgg-if
     * @date 2021年3月26日 下午7:07:25
     * @param enumStrName
     * @return
     */
    public static MsgTypeEnum matchEnumName(String enumStrName) {
        if (StringUtils.isBlank(enumStrName)) {
            return null;
        }
        for (MsgTypeEnum e:MsgTypeEnum.values()) {
            if (StringUtils.equals(enumStrName, e.name())) {
                return e;
            }
        }
        return null;
    }

}
