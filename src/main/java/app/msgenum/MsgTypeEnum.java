/**
 * 
 */
package app.msgenum;

import org.apache.commons.lang3.StringUtils;

/**
 * ��Ϣ����
 * @author yxlgg-if
 * @date 2021��3��26�� ����13:33:11
 */
public enum MsgTypeEnum {
    
    UNKNOW("δ֪����Ϣ����"),
    MSG_REPLY("��Ϣ�ظ�"),
    HEART_TEST("��������"),
    HEART_REPLY("�����ظ�"),
    SERVER_WAIT_CLIENT("֪ͨ�ͻ��˽���������"),
    // ���������ӶԵ��������������н���
    CLIENT_CONNECT("�ͻ�������������������"),
    // �ͻ��˺ͷ���˽����ڲ���Ϣ֪ͨ�Ķ˿�
    CLIENT_CONTROL("�ͻ�������������������"),
    ;
    
    private String enumDescribe;

    MsgTypeEnum(String enumDescribe) {
        // TODO �Զ����ɵĹ��캯�����
        this.enumDescribe = enumDescribe;
    }
    
    public String getEnumDescribe() {
        return enumDescribe;
    }
    
    /**
     * �Դ�����ַ���������ö�ٽ���ƥ�䣬ƥ�䵽��ͬ���ֵ�ö���򷵻ظ�ö�٣����򷵻�null
     * @author yxlgg-if
     * @date 2021��3��26�� ����7:07:25
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
