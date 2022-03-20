# ITEM81 wait 와 notify 보다는 동시성 유틸리티를 애용하라

- wait와 notifiy는 올바르게 사용하기가 아주 까다로우니 고수준 동시성 유틸리티(java.util.concurrent)를 사용하자.

## java.util.concurrent
- 실행자 프래임워크(Executor Framework)
- 동시성 컬랙션(Concurrent Collections)
- 동기화 장치(Synchronizers)

## 동시성 컬랙션(Concurrent Collections)
- List, Queue, Map 같은 표준 컬렉션 인터페이스에 동시성을 가미해 구현한 고성능 컬렉션이다.
- 동시성 컬렉션에서 동시성을 무력화하는건 불가능하며, 외부에서 락을 추가로 사용하면 오히려 속도가 느려진다.

### ConcurrentMap
- 동시성이 뛰어나며 속도도 무척 빠르다.
- Collections.synchronizedMap 보다는 ConcurrentHashMap 을 사용하자
``` java
public class Intern {
    private static final ConcurrentMap<String, String> map
        = new ConcurrentHashMap<>();

    public static String intern(String s) {
        String previousValue = map.putIfAbsent(s, s);
        return previousValue == null
                ? s : previousValue;
    }

    public static String intern(String s) {
        String result = map.get(s);     // ConcurrentMap 은 get 과 같은 검색 기능에 최적화되어 get을 먼저 호출하면 더 빠르다.
        if (result == null) {
            result = map.putIfAbsent(s, s);
            if (result == null) result = s;
        }
        return result;
    }    
}
```
### BlockingQueue
- task 메소드는 큐의 첫 원소를 꺼낸다. 만약 큐가 비었다면 새로운 원소가 추가될 때까지 기다린다.
- 작업 큐(생산자-소비자 큐)로 쓰기에 적합하다.
- ThreadPoolExecutor를 포함한 대부분의 실행자 서비스 구현체에서 사용한다.

## 동기화 장치(Synchronizers)
- 스레드가 다른 스레드를 기다릴 수 있게 하여, 서로 작업을 조율할 수 있게 해준다
- CountDownLatch, Semaphore 가장 자주 쓰이고, CyclicBarrier, Exchangers는 그 보다 덜 쓰인다. Phaser는 가장 강력한 동기화 장치다.
- 시간 간격을 잴때는 항상 System.currentTimeMills 가 아니라, System.nanoTime 을 사용하자.

## Wait/Notify 사용할 경우 (어쩔수 없이 레거시 코드를 다뤄야할때)
- wait메서드를 사용할때는 반드시 대기 반복문(wait loop) 관용구를 사용하라. 반복문 밖에서는 절대로 호출하지 말라
``` java
// wait 메서드를 사용하는 표준방식
synchronized (obj) {
  while (<조건이 충족되지 않음>) {
    obj.wait(); // 락을 놓고, 깨어나면 다시 잡는다.
  }
  ...// 조건이 충족되었을 경우의 코드를 실행한다.
}
```
- notify와 notifyAll 중에 notifyAll을 사용하는게 합기적이고 안전한다.
    - 깨어난 스레드들은 기다리던 조건이 충족되었는지 확인하여, 충적되지 않았다면 다시 대기 할 것
    - 외부로 공개된 객체에 대해 실수로 혹은 악의적으로 notify 호출하는 상황에 대비


