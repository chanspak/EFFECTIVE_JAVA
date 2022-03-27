# ITEM84 프로그램의 동작을 스레드 스케줄러에 기대지 말라

- 여러 스레드가 실행 중이면 운영체제의 스레드 스케줄러가 어떤 스레드를 얼마나 오래 실행하지 정한다.
- 정확성이 성능이 스레드 스케줄러에 따라 달라지는 프로그램이라면 다른 플랫폼에 이식하기 어렵다.

## 이식성 좋은 프로그램을 작성하는 가장 좋은 방법
- 실행가능한 스레드의 평균적인 수를 프로세서 수보다 지나치게 많아지지 않도록 하는것이다. 그래야 스레드 스케줄러가 고민할 거리가 줄어든다.
- 실행 준비가 된 스레드는 작업을 완료까지 계속 실행되게 만든다. 이런 프로그램은 스레드 스케줄링 정책이 상이한 시스템에서도 동작이 크게 달라지지 않는다.
- 실행 가능한 스레드 수를 적게 유지하는 주요 기법은 각 스레드가 유용한 작업을 완료한 후에 다음 일거리가 생길 때까지 대기하도록 하는 것이다. 즉, 지금 당장 처리해야 할 작업이 없다면 실행돼서는 안 된다.
- 스레드는 절대 바쁜 대기(busy waiting) 상태가 되면 안 된다. 공유 객체의 상태가 바뀔 때까지 쉬지 않고 검사해서는 안 된다는 뜻이다. 바쁜 대기 상태는 스레드 스케줄러의 변덕에 취약하고 프로세서에 큰 부담을 주어 다른 작업이 실행될 기회를 박탈한다.
```java
// 끔찍한 CountDownLatch 구현 - 바쁜 대기 버전 
public class SlowCountDownLatch {
    private int count;

    public SlowCountDownLatch(int count) {
        if (count < 0)
        throw new IllegalArgumentException(count + " < 0");
        this.count = count;
    }

    public void await() {
        while (true) { 
            synchronized(this) {    // 공유 객체의 상태를 계속 검사한다.
                if (count == 0)
                return;
            }
        }
    }

    public synchronized void countDown() {
        if (count != 0)
            count--;
    }
}
```
- Thread.yield를 써서 문제를 고쳐보려는 유혹을 떨쳐내자. 처음에는 JVM의 성능을 높여준 yield가 두 번째, 세 번째에서는 오히려 느려지게 할 수도 있으며, 테스트할 수단도 없다.