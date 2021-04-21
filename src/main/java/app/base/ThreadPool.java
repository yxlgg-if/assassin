/**
 * 
 */
package app.base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池
 * @author yxlgg-if
 * @date 2021年3月31日 上午12:14:54
 */
public final class ThreadPool {

    /**
     * 
     */
    public ThreadPool() {
        // TODO 自动生成的构造函数存根
    }
    
    /**
     * 全局线程池
     */
    private static ExecutorService cachedThreadpool = Executors.newCachedThreadPool();
    
    /**
     * newCachedThreadPool是无限制的线程池，只适合处理短时间的任务，keepAliveTime为60S（空置60秒后被杀死）
     * 虽然线程可重用，但是长时间的任务切勿使用，LinkedBlockingQueue是一个用链表实现的有界阻塞队列，最大长度为Integer.MAX_VALUE，
     * 不设置就是无限大，阻塞队列过程容易出现OOM
     * @author yxlgg-if
     * @date 2021年3月31日 上午12:25:58
     * @param runnable
     */
    public static void excuteCachedThreadpool(Runnable runnable) {
        cachedThreadpool.execute(runnable);
    }
    
    /**
     * 关闭线程池
     * @author yxlgg-if
     * @date 2021年3月31日 上午12:17:28
     */
    public static void closeCachedThreadpool() {
        if (cachedThreadpool != null) {
            cachedThreadpool.shutdownNow();
        }
        cachedThreadpool = null;
    }

}
