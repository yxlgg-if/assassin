package app;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import app.base.LoadIni;

/**
 * 公共参数配置
 * @author yxlgg-if
 * @date 2021年3月24日 下午8:51:48
 */
public final class PubConfig {

	static {
		LoadIni.redIni();
	}
	

	// 字符编码
	public static final Charset charset = StandardCharsets.UTF_8;
	// 服务端地址

//	public static final String assassinServiceHost = "127.0.0.1";
//	
//	public static final String assassinServiceHost = "192.168.17.155";
	
	public static final String assassinServiceHost = LoadIni.serverHost;

	
	// 服务端端口
	public static final int assassinServerPort = LoadIni.serverPort;
	
	// AES盐值
	public static final String aesBaseKey = LoadIni.aesBaseKey;
	
	// 签名盐值
    public static final String autographsKey = LoadIni.autographsKey;
    
    // 映射对
    public static List<ExposeMap> ExposeMapingArray = PubConfig.sourceList();
    
    public static List<ExposeMap> sourceList() {
    	List<ExposeMap> list = new ArrayList<>();
		for (String key:LoadIni.map.keySet()) {
			list.add(ExposeMap.modelObj(Integer.parseInt(key),
					LoadIni.map.get(key).split(":")[0].replaceAll("(?:\"|')", ""),
					Integer.parseInt(LoadIni.map.get(key).split(":")[1].replaceAll("(?:\"|')", ""))));
		}
		
		return list;
    }
    
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
