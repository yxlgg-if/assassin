/**
 * 
 */
package app.base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * �̳߳�
 * @author yxlgg-if
 * @date 2021��3��31�� ����12:14:54
 */
public final class ThreadPool {

	/**
	 * 
	 */
	public ThreadPool() {
		// TODO �Զ����ɵĹ��캯�����
	}
	
	/**
	 * ȫ���̳߳�
	 */
	private static ExecutorService cachedThreadpool = Executors.newCachedThreadPool();
	
	/**
	 * newCachedThreadPool�������Ƶ��̳߳أ�ֻ�ʺϴ����ʱ�������keepAliveTimeΪ60S������60���ɱ����
	 * ��Ȼ�߳̿����ã����ǳ�ʱ�����������ʹ�ã�LinkedBlockingQueue��һ��������ʵ�ֵ��н��������У���󳤶�ΪInteger.MAX_VALUE��
	 * �����þ������޴��������й������׳���OOM
	 * @author yxlgg-if
	 * @date 2021��3��31�� ����12:25:58
	 * @param runnable
	 */
	public static void excuteCachedThreadpool(Runnable runnable) {
		cachedThreadpool.execute(runnable);
	}
	
	/**
	 * �ر��̳߳�
	 * @author yxlgg-if
	 * @date 2021��3��31�� ����12:17:28
	 */
	public static void closeCachedThreadpool() {
		if (cachedThreadpool != null) {
			cachedThreadpool.shutdownNow();
		}
		cachedThreadpool = null;
	}

}
