/**
 * 
 */
package app;

import app.PubConfig.ExposeMap;
import app.clientcls.config.ClientConfig;
import app.clientcls.threads.ClientMainThread;

/**
 * @author yxlgg-if
 * @date 2021��3��24�� ����8:48:06
 */
public class AssassinClient {

	/**
	 * 
	 */
	public AssassinClient() {
		// TODO �Զ����ɵĹ��캯�����
	}

	/**
	 * @author yxlgg-if
	 * @date 2021��3��24�� ����8:48:06
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO �Զ����ɵķ������
		runAssassinClient();
	}
	
	public static void runAssassinClient() {
		for (ExposeMap exposeMap:PubConfig.ExposeMapingArray) {
			ClientConfig config = new ClientConfig();
			// ���÷���˵�IP�Ͷ˿�
			config.setAssassinServiceHost(PubConfig.assassinServiceHost);
			config.setAssassinServerPort(PubConfig.assassinServerPort);
			
			// ���ñ�¶�������Ķ˿�
			config.setExposePort(exposeMap.exposePort);
			
			// ���ñ��ص�IP�Ͷ˿�
			config.setAssassinClientHost(exposeMap.assassinClientHost);
			config.setAssassinPort(exposeMap.assassinPort);
			
			// ���ý�����Կ
			config.setAesBaseKey(PubConfig.aesBaseKey);
			config.setAutographsKey(PubConfig.autographsKey);
			
			// �洢���ַ�����
			config.setCharset(PubConfig.charset);
			new ClientMainThread(config).start();
		}
	}

}
