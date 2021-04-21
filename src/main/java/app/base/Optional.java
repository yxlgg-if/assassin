/**
 * 
 */
package app.base;


/**
 * ��������
 * @author yxlgg-if
 * @date 2021��4��2�� ����12:59:28
 */
public class Optional<T> {
    
    private T value;
    
    public static <T> Optional<T> of(T value) {
        return new Optional<T>(value);
    }

    public Optional(T value) {
        // TODO �Զ����ɵĹ��캯�����
        this.value = value;
    }
    
    public T getValue() {
        return value;
    }
    
    public void setVaule(T value) {
        this.value = value;
    } 
    

}
