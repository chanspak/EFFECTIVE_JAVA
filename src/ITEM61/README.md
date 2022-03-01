# ITEM61. 박싱된 기본 타입보다는 기본 타입을 사용하라

- 기본 타입은 값만 가지고 있으나, 박싱된 기본 타입은 값에 더해 식별자(identity)이란 속성을 갖는다.
- 기본 타입의 값은 언제나 유효하나, 박싱된 기본 타입은 유효하지 않은 값, 즉 null을 가질 수 있다.
- 기본 타입이 박싱된 기본 타입보다 시간과 메모리 사용면에서 더 효율적이다.

***
```java
Comparator<Integer> naturalOrder = 
    (i, j ) -> (i < j) ? -1 : (i == j) ? 0 : 1;     // 객체 메모리값 비교
```
- 박싱된 기본 타입에 == 연산자를 사용하면 오류가 일어난다.

***
```java
public Class Unbelievable { 
    static Integer i;   // 객체 초기화 안해줌 i = null

    public static void main(Stringp[] args) {
        if (i == 42)    // i = null NullPointerException 유발
            System.out.println("믿을 수 없군!");
    }
}
```
- 기본 타입과 박싱된 기본 타입을 혼용한 연산에서는 박싱된 기본 타입의 박싱이 자동으로 풀린다. 그리고 null참조를 언박싱하면 NullPointerException이 발생한다.
```java
public static void main(String[] args) {
    Long sum = 0L;
    for (long i = 0; i <= Interger.MAX_VALUE; i++) {
        sum += i;   // Interger.MAX_VALUE개 만큼 인스턴스 생성됨 
    }
    System.out.println(sum);
}
```
- 박싱과 언박싱이 반복해서 일어나 체감될 정도로 성능이 느려진다.