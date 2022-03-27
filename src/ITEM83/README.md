# ITEM83 지연 초기화는 신중히 사용하라

- 지연 초기화는 필드의 초기화 시점을 그 값이 처음 필요할 때까지 늦추는 기법이다.
- 지연 초기화는 주로 최적화 용도로 쓰이며, 클래스와 인스턴스 초기화 시 발생하는 위험한 순환 문제를 해결하는 효과도 있다.
- 다른 모든 최적화와 마찬가지로 지연 초기화에 대해 해줄 최선의 조언은 "필요할 떄까지 하지 말라"다.
- 인스턴스 생성 시의 초기화 비용은 줄지만 그 대신 지연 초기화하는 필드에 접근하는 비용은 커진다.
- 해당 클래스의 인스턴스 중 그 필드를 사용하는 인스턴스 비율이 낮은 반면, 그 필드를 초기화하는 비용이 크다면 지연 초기화가 제 역활을 해줄 것이다.
---
## 멀티스레드 환경에서는 지연 초기화.
- 멀티스레드 환경에서는 지연 초기화를 하기가 까다롭다.
- 지연 초기화하는 필드를 둘 이상의 스레드가 공유한다면, 반드시 동기화해야 한다. 그렇지 않으면 심각한 버그로 이어질 수 있다.
- 대부분의 상황에서 일반적인 초기화가 지연 초기화보다 낫다.
   
1. 일반적인 초기화 방법
    ``` java
    // 인스턴스 필드를 초기화하는 일반적인 초기화 방법
    private final FieldType field = computeFieldValue();    // final 한정자 사용
    ```
2. 인스턴스 필드의 지연 초기화 - synchronized 접근자 사용
    ``` java
    private FieldType field;

    private synchronized FieldType getField() {  // 지연 초기화가 초기화 순환성을 깨트릴 것 같다면 synchronized를 단 접근자를 사용하자.
        if (field == null)
            field = computeFieldValue();
        return field;
    }
    ```
3. 정적 필드 지연 초기화 홀더 클래스 관용구
    - 성능 때문에 정적 필드를 지연 초기화해야 한다면 지연 초기화 홀더 클래스 관용구를 사용하자. 클래스가 처음 쓰일 때 초기화된다는 특성을 이용한 관용구이다.
    ``` java
    /// 정적 필드 지연 초기화 홀더 클래스 관용구
    private static class FieldHolder {
        static final FieldType field = computeFieldValue();
    }

    // getField() 메서드가 호출되면 FieldHolder.field가 읽히면서 FieldHolder 클래스 초기화를 촉발한다. 동기화를 하지 않기 때문에 성능이 느려질 걱정이 전혀 없다는 장점이 있다.
    // VM은 클래스를 초기화할 때만 필드에 접근한다.
    private static FieldType getField() { return FieldHolder.field; }
    ```
4. 인스턴스 필드 지연 초기화 이중검사 관용구
    - 성능 때문에 인스턴스 필드를 지연 초기화해야 한다면 이중검사 관용구를 사용하자. 이 관용구는 초기화된 필드에 접근할 때의 동기화 비용을 없애준다.
    ``` java
    // 인스턴스 필드용 지연 초기화 이중검사 관용구
    private volatile FieldType field;   // 필드가 초기화된 후로는 동기화하지 않으므로 해당 필드는 volatile로 선언

    private FieldType getField() {
        FieldType result = field;   // 지역변수를 사용하여 인스턴스 변수를 딱 한 번만 읽도록 보장하는 역할을 하여, 성능을 높혔다.
        if (result != null)    // 첫 번째 검사 (락 사용 안 함)
            return result;

        synchronized(this) {
            if (field == null) // 두 번째 검사 (락 사용)
                field = computeFieldValue();
            return field;
        }
    }
    ``` 
5. 단일 검사 관용구
    - 이따금 반복해서 초기화 해도 상관없는 인스턴스 필드를 지연 초기화해야 할 때면 이중검사에서 두 번째 검사를 생략할 수 있다.
    ``` java
    // 단일검사 관용구 - 초기화가 중복해서 일어날 수 있다.
    private volatile FieldType field; // 모든 스레드가 필드의 값을 다시 계산해도 상관없다면, volatile 한정자를 없애도 된다.(짜릿한 단일 검사) - long, double은 제외

    private FieldType getField() {
        FieldType result = field;
        if (result == null)
            field = result = computeFieldValue();
        return result;
    }
    ```