package app.base;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 自定义格式化
 * @author yxlgg-if
 * @date 2021年3月26日 下午7:01:34
 */
public class DefineFormat {
	
    /**
     * 服务端生成的处理第三方请求的处理方法的key
     * @author yxlgg-if
     * @date 2021年3月26日 下午7:09:49
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
     * 根据sockPartnerKey获取exposePort
     * @author yxlgg-if
     * @date 2021年3月26日 下午7:22:10
     * @param socketPartnerKey
     * @return
     */
	public static Integer getExposePortBySocketPairKey(String socketPairKey) {
        String[] split = socketPairKey.split("-");
        return Integer.valueOf(split[1]);
	}
	
	/**
	 * 获取消息序列
	 * @author yxlgg-if
	 * @date 2021年3月26日 下午7:24:27
	 * @return
	 */
	public static String getMsgSeq() {
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String randomNum = RandomStringUtils.randomNumeric(4);
        return "MS-" + dateTime + "-" + randomNum;
	}
}
