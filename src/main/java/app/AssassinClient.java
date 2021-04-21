/**
 * 
 */
package app;

import app.PubConfig.ExposeMap;
import app.clientcls.config.ClientConfig;
import app.clientcls.threads.ClientMainThread;

/**
 * @author yxlgg-if
 * @date 2021年3月24日 下午8:48:06
 */
public class AssassinClient {

    /**
     * 
     */
    public AssassinClient() {
        // TODO 自动生成的构造函数存根
    }

    /**
     * @author yxlgg-if
     * @date 2021年3月24日 下午8:48:06
     * @param args
     */
    public static void main(String[] args) {
        // TODO 自动生成的方法存根
        runAssassinClient();
    }
    
    public static void runAssassinClient() {
        for (ExposeMap exposeMap:PubConfig.ExposeMapingArray) {
            ClientConfig config = new ClientConfig();
            // 设置服务端的IP和端口
            config.setAssassinServiceHost(PubConfig.assassinServiceHost);
            config.setAssassinServerPort(PubConfig.assassinServerPort);
            
            // 设置暴露到公网的端口
            config.setExposePort(exposeMap.exposePort);
            
            // 设置本地的IP和端口
            config.setAssassinClientHost(exposeMap.assassinClientHost);
            config.setAssassinPort(exposeMap.assassinPort);
            
            // 设置交互密钥
            config.setAesBaseKey(PubConfig.aesBaseKey);
            config.setAutographsKey(PubConfig.autographsKey);
            
            // 存储的字符编码
            config.setCharset(PubConfig.charset);
            new ClientMainThread(config).start();
        }
    }

}
