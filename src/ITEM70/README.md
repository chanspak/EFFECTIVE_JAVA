# ITEM70. 복구할 수 있는 상황에는 검사 예외를, 프로그래밍 오류에는 런타임 예외를 사용하라

- 자바는 문제 상황을 알리는 타입(throwable)으로 검사 예외, 비검사 예외(런타임 예외, 에러) 이렇게 세가지를 제공
- 호출하는 쪽에서 복구하리라 여겨지는 상황이라면 검사 예외를 사용하라.
    - 예외를 catch로 잡아 처리하거나 더 바깥으로 전파하도록 강제
    - API 설계자는 API 사용자에게 검사 예외를 던져주어 그 상황에서 회복해내라고 요구한 것
- 프로그래밍 오류를 나타낼 때는 런타임 예외를 사용하자.
    - API 설계자의 판단
    - 복구 가능하다고 믿는다면 검사 예외를, 그렇지 않다면 런타임 예외를 사용하자
- 여러분이 구현하는 비검사 throwable은 모두 RuntimeException의 하위 클래스여야 한다. Error는 상속하지 말아야 할 뿐 아니라, trow 문으로 직접 던지는 일도 없어야 한다.(AssertionError는 예외다)
- throwable 이로울게 없으닌 절대 사용하지 말자!!(직접 상속 받지 말아라)
- 검사 예외라면 복구에 필요한 정보를 알려주는 메서드도 제공하자.