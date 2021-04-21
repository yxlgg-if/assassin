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
 * 初始化交互线程
 * @author yxlgg-if
 * @date 2021年4月8日 下午6:44:49
 */
@Slf4j
public class SourceInteractiveThread {
    
    // 应用到全局的hashMap
    public static Map<Integer, ServerInteractiveThread> serverInteractiveMap = new ConcurrentHashMap<>();
    
    // 获取hashMap的值
    public static ServerInteractiveThread get(Integer exposePort) {
        return serverInteractiveMap.get(exposePort);
    }
    
    // 将对象加入到hashMap
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
    
    // 初始化静态变量serverInteractiveMap
    public static void sourceServerInteractiveThread(IServiceConfig<MsgModel, MsgModel> config) {
        ServerInteractiveThread serverInteractiveThread = null;
        try {
            serverInteractiveThread = new ServerInteractiveThread(config);
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            log.error("ServerInteractiveThread[{}] source failed.", config.getExposePort());
            e.printStackTrace();
            return;
        }
        SourceInteractiveThread.add(serverInteractiveThread);
    }

}
