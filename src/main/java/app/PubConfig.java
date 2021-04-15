package app;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * ������������
 * @author yxlgg-if
 * @date 2021��3��24�� ����8:51:48
 */
public final class PubConfig {

	/**
	 * 
	 */
	public PubConfig() {
		// TODO �Զ����ɵĹ��캯�����
	}
	
	// �ַ�����
	public static final Charset charset = StandardCharsets.UTF_8;
	// ����˵�ַ
//	public static final String assassinServiceHost = "192.168.17.155";
	public static final String assassinServiceHost = "127.0.0.1";
	
	// ����˶˿�
	public static final int assassinServerPort = 22222;
	
	// AES��ֵ
	public static final String aesBaseKey = "U2FsdGVkX1/5RWtUxy/o63igqCuAqNWHxKAPG9JSSPw=";
	
	// ǩ����ֵ
    public static final String autographsKey = "guagua";
    
    // ӳ���
    public static ExposeMap[] ExposeMapingArray = new ExposeMap[] {
            //
    		ExposeMap.modelObj(8081, "127.0.0.1", 8080),
            //
    		ExposeMap.modelObj(8888, "127.0.0.1", 8080)
    };
    
    public static class ExposeMap {
    	// �ͻ��˵�ַ
    	public String assassinClientHost;
    	// �ͻ��˶˿�
    	public int assassinPort;
    	// ��Ӧ�ͻ��˶˿�Ҫ��¶��������ӳ��˿�
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
