/**
 * 
 */
package app.servercls.literacyevent;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import app.model.MsgModel;
import app.servercls.config.IServiceConfig;
import app.servercls.threads.ServerInteractiveThread;
import lombok.extern.slf4j.Slf4j;

/**
 * ��ʼ�������߳�
 * @author yxlgg-if
 * @date 2021��4��8�� ����6:44:49
 */
@Slf4j
public class SourceInteractiveThread {
    
    // Ӧ�õ�ȫ�ֵ�hashMap
    public static Map<Integer, ServerInteractiveThread> serverInteractiveMap = new ConcurrentHashMap<>();
    
    // ��ȡhashMap��ֵ
    public static ServerInteractiveThread get(Integer exposePort) {
        return serverInteractiveMap.get(exposePort);
    }
    
    // ��������뵽hashMap
    public static void add(ServerInteractiveThread serverInteractiveThread) {
        serverInteractiveMap.put(serverInteractiveThread.getExposePort(), serverInteractiveThread);
    }
    
    public static void remove(Integer exposePort) {
        ServerInteractiveThread serverInteractiveThread = serverInteractiveMap.get(exposePort);
        if (serverInteractiveThread == null) {
            return;
        }
        serverInteractiveMap.remove(exposePort);
    }
    
    // ��ʼ����̬����serverInteractiveMap
    public static void sourceServerInteractiveThread(IServiceConfig<MsgModel, MsgModel> config) {
        ServerInteractiveThread serverInteractiveThread = null;
        try {
            serverInteractiveThread = new ServerInteractiveThread(config);
        } catch (IOException e) {
            // TODO �Զ����ɵ� catch ��
            log.error("ServerInteractiveThread[{}] source failed.", config.getExposePort());
            e.printStackTrace();
            return;
        }
        SourceInteractiveThread.add(serverInteractiveThread);
    }

}
