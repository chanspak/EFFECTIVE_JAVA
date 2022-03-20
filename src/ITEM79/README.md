# ITEM79 과도한 동기화는 피하라

- 과도한 동기화는 성능을 떨어뜨리고, 교착상태에 빠뜨리고, 심지어 예측할 수 없는 동작을 낳기도 한다.
- 응답 불가와 안전 실패를 피하려면 동기화 메서드나, 동기화 블록 안에서는 제어를 절대로 클라이언트에 양도하면 안 된다.
- 동기화된 영역 안에서는 재정의할 수 있는 메서드 호출하면 안되며 클라이언트가 넘겨준 함수 객체를 호출해서도 안 된다.
- 기본 규칙은 동기화 영역에서는 가능한 한 일을 적게 하는 것이다.
- 가변 클래스를 작성하려겨든 두가지 선택지 중 하나를 선택
    - 클래스 내에서 동기화를 전혀 하지 말고, 클래스를 사용하는 측에서 동기화 하게 하자.
    - 동기화를 내부에서 수행해 스레드 안전한 클래스로 만들자. (외부에서 객체 전체에 락을 거는 것보다 동시성을 월등히 개선할 수 있을때만)

# 외계인 메소드를 사용한 예
``` java
public class ObservableSet<E> extends ForwardingSet<E> {
    public ObservableSet(Set<E> set) {
        super(set);
    }

    private final List<SetObserver<E>> observers = new ArrayList<>();

    public void addObserver(SetObserver<E> observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public boolean removeObserver(SetObserver<E> observer) {
        synchronized (observers) {
            return observers.remove(observer);
        }
    }

    // 외계인 메서드를 호출하는 예
    private void notifyElementAdded(E element) { 
        synchronized (observers) {
            for (SetObserver<E> observer : observers)
                observer.added(this, element);  // synchronized 블럭에서 외계인 메소드(클라이언트에 메소드) 실행
        }
    }

    // 외계인 메서드를 동기화 블록 바깥으로 옯겼다.
    private void notifyElementAdded(E element) {
        List<SetObserver<E>> snapshot = null;
        synchronized (observers) {
            snapshot = new ArrayList<>(observers); 
        }
        for (SetObserver<E> observer : snapshot)
            observer.added(this, element);     // 동기화 영역 밖에서 오는 외계인 메소드(바깥에서 오는 메서드)는 열린호출(open call)이라고 한다.
    }

    private void notifyElementAdded(E element) { 
        synchronized (observers) {
            for (SetObserver<E> observer : observers)
                observer.added(this, element);  // synchronized 블럭에서 외계인 메소드(클라이언트에 메소드) 실행
        }
    }

    @Override
    public boolean add(E element) {
        boolean added = super.add(element);
        if (added)
            notifyElementAdded(element);
        return added;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (E element : c)
            result |= add(element); 
        return result;
    }

    public static void main(String[] args) {
        ObservableSet<Integer> set = new ObservableSet<>(new HashSet<>());

        // add 된 원소를 출력한다. (정상 동작)
        set.addObserver((set1, element) -> System.out.println(element));

        // 값이 23이 되면 자기자신 구독 해지. ConcurrentModificationException 발생
        // set.add() -> notifyElementAdded() 호출.
        // notifyElementAdded 에서 observers 리스트 탐색하며 added()를 호출하고 자기자신을 removeObserver함수에 넘겨줌, 
        // callback을 거쳐 수정되는 값까진 동기화 시키지 못함.
        set.addObserver(new SetObserver<Integer>() {
            @Override
            public void added(ObservableSet<Integer> s, Integer e) {
                System.out.println(e);
                if (e == 23)            
                    s.removeObserver(this);     // ConcurrentModificationException에러 발생
            }
        });

        // 쓸데없이 백그라운드 스레드를 사용하는 관찰자
        // removeObserver() 호출시 main 스레드에서 락을 쥐고 있기 때문에 대기,
        // main Threa에서도 removeObserver()를 호출을 기다리고 있어 교착상태에 빠진다.
        set.addObserver(new SetObserver<Integer>() {
            @Override
            public void added(ObservableSet<Integer> s, Integer e) {
                System.out.println(e);
                if (e == 23) {
                    ExecutorService exec = Executors.newSingleThreadExecutor();
                    try {
                        exec.submit(() -> s.removeObserver(this)).get();   // 교착상태에 빠진다.
                    } catch (ExecutionException | InterruptedException ex) {
                        throw new AssertionError(ex);
                    } finally {
                        exec.shutdown();
                    }
                }
            }
        });

        for (int i = 0; i < 100; i++)
            set.add(i);
    }
}
```