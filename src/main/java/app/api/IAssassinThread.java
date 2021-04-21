/**
 * 
 */
package app.api;

/**
 * 归属线程接口
 * @author yxlgg-if
 * @date 2021年4月12日 下午9:55:04
 */
public interface IAssassinThread {
    
    default boolean stopInteractive(String socketPairKey) {
        return true;
    }
    
    default void stopUpperLevelPro() {}
}
