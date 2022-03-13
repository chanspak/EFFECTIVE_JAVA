# ITEM72 표준 예외를 사용하라

|예외|주요쓰임|
|-----|-----|
|IllegalArgumentException|허용하지 않는 값이 인수로 건네졌을떄|
|IllegalStateException|대상 객체의 상태가 호출된 메서드를 수행하기에 적합하지 않을 때. 예시로 제대로 초기화되지 않은 객체를 사용하려 할때|
|NullPointerException|null을 허용하지 않은 메서드에 null을 건냈을 때|
|IndexOutOfBoundsException|인덱스 범위를 넘어섰을 떄|
|ConcurrentModificationException|단일 스레드에서 사용하려고 설계한 객체를 여러 스레드가 동시에 수정하려 할 때|
|UnsupportedOperationException|호출한 메서드를 지원하지 않을때|

- **Exception, RuntileException, Throwable, Error**는 직접 재사용하지 말자.
    - 이 클래스들은 추상 클래스라고 생각해라.