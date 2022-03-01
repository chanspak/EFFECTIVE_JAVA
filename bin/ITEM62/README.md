# ITEM62. 다른 타입이 적절하다면 문자열 사용을 피하라

- 문자열은 다른 값 타입을 대신하기에 적합하지 않다.
    - 파일, 네트워크, 키보드 입력으로부터 데이터를 받을 때 주로 문자열을 사용한다.
    - 기본 타입이든 참조 타입이든 적절한 값 타입이 있다면 그것을 사용하고, 없다면 새로 하나 작성하라.
- 문자열은 열거 타입을 대신하기에 적합하지 않다.
- 문자열은 혼합 타입을 대신하기에 적합하지 않다.
```java
String compoundKey = className + "#" + i.next();
// 두 요소를 구분해주는 문자 #이 두 요수중 하나에서 쓰였다면 혼란스러운 결과 초래
// 적절한 equlas, toString, compareTo 메서드를 제공 할수 없다.
// 전용 클래스를 새로 만드면 편이 낫다.
```
- 문자열은 권한을 표현하기에 적합하지 않다 ??
```java
public class ThreadLocal {
    private ThreadLocal() {} // 객체 생성 불가

    // 현 스레드의 값을 키로 구분해 저장한다.
    public static void set(String key, Object value);

    // (키가 가리키는) 현 스레드의 값을 반환한다.
    public static Object get(String key);

    // 키가 전역 이름공간에 공유된다는 점
    // 같은키를 쓰기로 결정한다면, 의도치 않게 같은 변수를 공유
    // 의도적으로 같은 키를 사용하면 다른 클라이언트의 값을 가져 옴
}
```
- 이 API는 문자열 대신 위조할 수 없는 키를 사용하면 해결된다. 이 키를 권한(capacity)이라고 한다.
```java
public final class ThreadLocal<T> {
    public ThreadLocal();
    public void set(T value);
    public T get();
}
```
