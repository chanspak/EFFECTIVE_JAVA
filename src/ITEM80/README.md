# ITEM80 스레드보다는 실행자, 태스크, 스트림을 애용하라

## 실행자 프레임워크(Executor Framework)
- java.util.concurrent 패키지에 기반
- 유연한 테스크 실행 기능

```java
    ExecutorService exec = Executors.newSingleThreadExecutor(); // 작업 큐 생성
    exec.execute(runnable);     // 테스크(task:작업)를 넘기는 방법
    exec.shutdown() // 실행자 종료
```
## 주요기능들
- 특정 태스크가 완료되기를 기다리는 get 메서드
- 태스크 모음 중 아무것 하나(invokeAny 메서드) 혹은 모든 태스크 (invokeAll 메서드) 가 완료되기를 기다린다.
- 실행자 서비스가 종료되기를 기다린다.(awaitTermination 메서드)
- 완료된 태스크들의 결과를 차례로 받는다.(ExecutorCompletionService 이용)
- 태스크를 특정 시간에 혹은 주기적으로 실행하게 한다(ScheduledThreadPoolExecutor 이용)

## 정적팩토리(실행자 서비스)
- 큐를 둘 이상의 스레드가 처리하게 하고 싶다면 간다한 다른 정적 팩토리를 이용하여 다른 종류의 실행자 서비스(스레드 풀)을 생성하면 된다.
    - java.util.concurrent.Executors 클래스의 정적 팩터리를 이용해 대부분의 실행자를 생성.
    - ThreadPoolExcutor클래스를 직접 사용하여 평범하지 않은 실행자 직접 생성. 스레드 풀 동작을 결정하는 거의 모든 속성 설정 가능
    - Executors.newCachedThreadPool 실행자 서비는 작은 프로그램이나 가벼운 서버라면 사용, 무거운 프로덕션 서버에는 좋지 않음.
        - 요청 받은 태스크들이 큐에 쌓이지 않고 즉시 스래드에 위임돼 실행되여 서버가 아주 무겁다면 CPU 이용률이 100%로 치닫을 수 있음.
    - 무거운 프로덕션 서버라면 Executors.newFixedThreadPool 실행자 서비스로 스래드 수를 고정하여 사용하거나, ThreadPoolExcutor를 직접 사용.

## 태스크
- Runnable과 그 사촌인 Callable로 나눈다. (Callable은 값을 반환하고 임의의 예외를 던질 수 있다.)
- 테스크를 수행하는 일반적인 매커니즘이 바로 실행자 서비스

## 포크-조인 태스크
- 포크-조인 풀이라는 특별한 실행자 서비스가 실행해준다.
- 포크-조인 풀을 구성하는 스레드들이 태스크들을 처리하며, 일을 먼저 끝내 스레드는 다른 스레드의 남의 태스크를 가져와 대신 처리할 수 있다.
- 모든 스레드가 바쁘게 움직여 CPU를 최대한 활용하면서 높은 처리량과 낮은 지연시간을 달성한다.

- [참고블로그](https://songiam.tistory.com/78) 