package app;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 公共参数配置
 * @author yxlgg-if
 * @date 2021年3月24日 下午8:51:48
 */
public final class PubConfig {

	/**
	 * 
	 */
	public PubConfig() {
		// TODO 自动生成的构造函数存根
	}
	
	// 字符编码
	public static final Charset charset = StandardCharsets.UTF_8;
	// 服务端地址

	public static final String assassinServiceHost = "127.0.0.1";

	
	// 服务端端口
	public static final int assassinServerPort = 22222;
	
	// AES盐值
	public static final String aesBaseKey = "U2FsdGVkX1/5RWtUxy/o63igqCuAqNWHxKAPG9JSSPw=";
	
	// 签名盐值
    public static final String autographsKey = "guagua";
    
    // 映射对
    public static ExposeMap[] ExposeMapingArray = new ExposeMap[] {
            //
    		ExposeMap.modelObj(8081, "127.0.0.1", 8080),
            //
    		ExposeMap.modelObj(8888, "127.0.0.1", 8080)
    };
    
    public static class ExposeMap {
    	// 客户端地址
    	public String assassinClientHost;
    	// 客户端端口
    	public int assassinPort;
    	// 对应客户端端口要暴露到外网的映射端口
    	public int exposePort;
    	public static ExposeMap modelObj(int exposePort, String assassinClientHost, int assassinPort) {
    		ExposeMap model = new ExposeMap();
    		model.exposePort = exposePort;
    		model.assassinClientHost = assassinClientHost;
    		model.assassinPort = assassinPort;
    		
    		return model;
    	}
    }
    


}
