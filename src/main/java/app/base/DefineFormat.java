package app.base;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * �Զ����ʽ��
 * @author yxlgg-if
 * @date 2021��3��26�� ����7:01:34
 */
public class DefineFormat {
    
    /**
     * ��������ɵĴ������������Ĵ�������key
     * @author yxlgg-if
     * @date 2021��3��26�� ����7:09:49
     * @param exposePort
     * @return
     */
    public static String getSocketPairKey(Integer exposePort) {
        DecimalFormat fiveLenFormat = new DecimalFormat("00000");
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String randomNum = RandomStringUtils.randomNumeric(4);
        return "SK-" + fiveLenFormat.format(exposePort) + "-" + dateTime + "-" + randomNum;
    }
    
    /**
     * ����sockPartnerKey��ȡexposePort
     * @author yxlgg-if
     * @date 2021��3��26�� ����7:22:10
     * @param socketPartnerKey
     * @return
     */
    public static Integer getExposePortBySocketPairKey(String socketPairKey) {
        String[] split = socketPairKey.split("-");
        return Integer.valueOf(split[1]);
    }
    
    /**
     * ��ȡ��Ϣ����
     * @author yxlgg-if
     * @date 2021��3��26�� ����7:24:27
     * @return
     */
    public static String getMsgSeq() {
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String randomNum = RandomStringUtils.randomNumeric(4);
        return "MS-" + dateTime + "-" + randomNum;
    }
}
