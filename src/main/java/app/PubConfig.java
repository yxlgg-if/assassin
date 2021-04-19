package app;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import app.base.LoadIni;

/**
 * ������������
 * @author yxlgg-if
 * @date 2021��3��24�� ����8:51:48
 */
public final class PubConfig {

	static {
		LoadIni.redIni();
	}
	

	// �ַ�����
	public static final Charset charset = StandardCharsets.UTF_8;
	// ����˵�ַ

//	public static final String assassinServiceHost = "127.0.0.1";
//	
//	public static final String assassinServiceHost = "192.168.17.155";
	
	public static final String assassinServiceHost = LoadIni.serverHost;

	
	// ����˶˿�
	public static final int assassinServerPort = LoadIni.serverPort;
	
	// AES��ֵ
	public static final String aesBaseKey = LoadIni.aesBaseKey;
	
	// ǩ����ֵ
    public static final String autographsKey = LoadIni.autographsKey;
    
    // ӳ���
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
