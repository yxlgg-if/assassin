/**
 * 
 */
package app;

import app.PubConfig.ExposeMap;
import app.servercls.config.ServiceConfig;
import app.servercls.literacyevent.SourceInteractiveThread;
import app.servercls.threads.ServerMainThread;

/**
 * @author yxlgg-if
 * @date 2021年3月24日 下午8:48:41
 */
public class AssassinServer {

	/**
	 * 
	 */
	public AssassinServer() {
		// TODO 自动生成的构造函数存根
	}

	/**
	 * @author yxlgg-if
	 * @date 2021年3月24日 下午8:48:41
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		runAssassinServer();
	}
	
	public static void runAssassinServer() {
		ServiceConfig config = new ServiceConfig();
		config.setAssassinServerPort(PubConfig.assassinServerPort);
		config.setAesBaseKey(PubConfig.aesBaseKey);
		config.setAutographsKey(PubConfig.autographsKey);
		config.setCharset(PubConfig.charset);
		new ServerMainThread(config).start();
		
		for (ExposeMap exposeMap:PubConfig.ExposeMapingArray) {
			config.setExposePort(exposeMap.exposePort);
			SourceInteractiveThread.sourceServerInteractiveThread(config);
		}
		
//		for (Object k:SourceInteractiveThread.serverInteractiveMap.keySet()) {
//			ServerInteractiveThread interactiveThread = SourceInteractiveThread.serverInteractiveMap.get(k);
//			System.out.println(interactiveThread.getExposePort());
//		}
	}

}
