/**
 * 
 */
package app.api;

/**
 * �����߳̽ӿ�
 * @author yxlgg-if
 * @date 2021��4��12�� ����9:55:04
 */
public interface IAssassinThread {
	
	default boolean stopInteractive(String socketPairKey) {
		return true;
	}
	
	default void stopUpperLevelPro() {}
}
