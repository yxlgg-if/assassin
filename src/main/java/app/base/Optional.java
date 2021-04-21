/**
 * 
 */
package app.base;


/**
 * 对象重设
 * @author yxlgg-if
 * @date 2021年4月2日 上午12:59:28
 */
public class Optional<T> {
    
    private T value;
    
    public static <T> Optional<T> of(T value) {
        return new Optional<T>(value);
    }

    public Optional(T value) {
        // TODO 自动生成的构造函数存根
        this.value = value;
    }
    
    public T getValue() {
        return value;
    }
    
    public void setVaule(T value) {
        this.value = value;
    } 
    

}
