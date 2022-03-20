# ITEM78 공유 중인 가변 데이터는 동기화해 사용하라

- synchronized 키워드는 해당 메서드나 블록을 한번에 한 스레드에 수행하도록 보장한다.
- 베타적 실행. 한 스레드가 변경하는 중이라서 상태가 일괁되지 않은 순간의 객체를 다른 스레드가 보지 못하게 막는다.
- 동기화를 제대로 사용하면 어떤 메서드도 이 객체의 상태가 일관되지 않은 순간을 볼 수 없을 것이다.
- 동기화된 메서드나 블록에 들어간 스레드가 같은 락의 보호하에 수행된 모든 이전 수정의 최종 결과를 보게 해준다.
- 언어 명세상 long과 duble 외의 변수를 읽고 쓰는 동작은 원자적(atomic)이다. 
    - 여러 스레드가 같은 변수를 동기화 없이 수정하는 중이라도, 항상 어떤 스레드가 정상적으로 저장한 값을 온전히 읽어옴을 보장한다는 뜻(진짜 ???)
    - 스레드가 필드를 읽을 때 항상 '수정이 완전히 반영된' 값을 얻는다고 보장하지만, 한 스레드가 지정한 값이 다른 스레드에게 '보이는가'는 보장하지 않는다.
- 동기화는 배타적 실행뿐 아니라 스레드 사이의 안정적인 통신에 꼭 필요하다.

## Thread 종료
- Thread.stop은 사용하지 말자
- backgroundThread에서 boolean 값을 폴링하면서 그값이 true가 되면 thread를 멈춘다.

### 잘못된 예시
``` java
public class StopThread {

    private static boolean stopRequested;

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(()->{
            int i = 0;
            // 원래코드
            while(!stopRequested){  // 1초뒤에 종료될것 같지만, 동기화가 빠지면 가상머신이 최적화된 코드로 실행
                i++;
            }
            // 최적화된 코드
            if (!stopRequested) {
                while (true)
                    i++;
            }            
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;   
    }
}
```
### Synchronized
```java
// 적정히 동기화해된 코드 (쓰기와 읽기 동기화된 메소드제공)
// 쓰기와 읽기 모두가 동기화 되지 않으면 동작을 보장하지 않는다.
// 정확히 1초 뒤에 종료
public class StopThread {

    private static boolean stopRequested;

    // 동기화된 쓰기 메소드 제공
    private static synchronized void requestStop(){     
        stopRequested = true;
    }

    // 동기화된 읽기 메소드 제공
    private static synchronized boolean stopRequested(){ 
        return stopRequested;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(()->{
            int i = 0;
            while(!stopRequested()){ 
                i++;
            }
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);
        requestStop();
    }
}
```
### Volatile
- 배타적 수행과는 상관없지만 항상 최근에 기록된 값을 읽게 보장
``` java
public class StopThread {

    private static volatile boolean stopRequested;  // boolean값 volatile로 지정

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread1 = new Thread(()->{
            int i = 0;
            while(!stopRequested){
                i++;
            }
        });
        backgroundThread1.start();

        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;
    }

    // 동기화가 필요한 코드
    // volatile은 배타적 수행은 보장하지 않기 때문에, 
    // ++ 연사자로 nextSeriaNumber 두번 접근하여 값을 저장하기 두번 접근사이에 다른 thread에서 해당 변수에 접근이 가능하다
    private static volatile int nextSeriaNumber = 0;

    public static int genarateSeriaNumber() {
        return nextSeriaNumber++; 
    }

    // 배타적 실행까지 지원하는 java.util.concurrent.atomic 패키지 사용
    private static final AtomicLong nextSeriaNumber = new AtomicLong();

    public static long genarateSeriaNumber() {
        return nextSeriaNumber.getAndIncrement(); 
    }
}
```