# ITEM73 추상화 수준에 맞는 예외를 던지라


- 메서드가 저수준 예외를 처리하지 않고 바깥으로 전파해버릴 때 당황스러울 것이다. 이는 내부 구현 방식을 드러내어 윗 레벨 API를 오염시킨다.
- 다음 릴리스에서 구현 방식을 바꾸면 다른 예외가 튀어나와 기존 클라이언트 프로그램을 깨지게 할 수도 있다.
- 상위 계층에서 는 저수준 예외를 잡아 자신의 추상화 수준에 맞는 예욍로 바꿔 던져야 한다. (예외번역)

```java
//  AbstractSequentialList에서 수행하는 예외번역
public E get(int index) {
	ListIterator<E> i = listIterator(index);
	try {
		return i.next();
	} catch (NoSuchElementException e) {     // 저수준 추상화를 이용한다.
        // 추상화 수준에 맞게 번역한다.	
		throw new IndexOutOfBoundsException("인덱스: " + index);
	}
}
```

- 예외를 번역할 때, 저수준 예외가 디버깅에 되움이 된다면 예외 연쇄를 사용하는게 좋다.

```java
// 예외 연쇄
try {
	... // 저수준 추상화를 이용한다.
} catch (LowerLevelException cause) {
	// 저수준 예외를 고수준 예외에 실어 보낸다.
	throw new HigherLevelException(cause);
}

// 예외 연쇄용 생성자
class HigherLevelException extends Exception {
	HigherLevelException(Throwable cause) {
		super(cause);   // 부모 Exception 클래스에 생성자에 원인을 건네준다.
	}
}
```

- 예외 전파를 남발하는 것보다 예외 번역이 우수한 방법이지만, 그렇다고 해서 남용해서는 안된다.
    - 가능하다면 저수준 메서드가 반드시 성공하도록 하여 아래 계층에서는 예외가 발생하지 않도록 하는 것이 최선
- 차선책
    - 위 계층에서 그 예외를 조용히 처리하여 문제를 API 호출자에게 까지 전파하지 않고, 발생한 예외는 java.util.logging 같은 적절한 로깅 기능을 활용하여 기록