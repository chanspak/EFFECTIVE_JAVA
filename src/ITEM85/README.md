# ITEM85 자바 직렬화의 대안을 찾으라

- 1997년, 자바에서 처음으로 직력화가 도입되었다.
- 직렬화의 근본적인 문제는 공격 범위가 너무 넣고 지속적으로 더 넓어져 방어하기 어렵다는 점이다.
    - ObjectInputStream의 readObject 메서드를 호출하면서 객체 그래프가 역직렬화되기 때문이다.
    - readObject 메서드는 바이트 스트림을 역직렬화하는 과정에서 이 메서드는 그 타입들 안의 모든 코드를 수행 할 수 있다.

## 가젯(gadget)
- 역직렬화 과정에서 잠재적으로 위험한 동작을 수행하는 메서드
- 여러 가젯을 함께 사용하여 가젯 체인을 구성할 수도 있는데, 가끔 하드웨어의 네이티브 코드를 마음대로 수행할 수도 있는 아주 강력한 가젯 체인이 발견되기도 한다.

## 역직렬화 폭탄(deserialization bomb)
- 역직렬화 시간이 오래 걸리는 짧은 스트림을 역직렬화하는 것만으로도 서비스 거부 공격에 쉽게 노출될 수 있다. 이런 스트림을 역직렬화 폭탄 이라고 한다.

    ``` java
    static byte[] bomb() {
        Set<Object> root = new HashSet<>();
        Set<Object> s1 = root;
        Set<Object> s2 = new HashSet<>();
    
        // root<s1<....>,s2<.....>> 구조로 hashset을 만든다.
        for (int i=0; i<100; i++) {
            Set<Object> t1 = new HashSet<>();
            Set<Object> t2 = new HashSet<>();
            t1.add("foo");  // t1과 t2를 다르게 만든다.
            s1.add(t1);s1.add(t2);  
            s2.add(t1);s2.add(t2);
            s1 = t1;
            s2 = t2;
        }
        return serialize(root); // 간결하게 하기 위해 이 메서드의 코드는 생략함.
        // 스트림의 전체 크기는 5,744바이트 이지만, HashSet 인스턴스를 역직렬화하려면 그 원소들의 해시코드를 계산해야 한다. 2의 100승 번 넘게 hashCode 메소드를 호출해야 한다.
    }
    ```
- 직렬화 위험을 회피하는 가장 좋은 방법은 아무것도 역직렬화하지 않는것이다.
- 여러분이 작성하는 새로운 시스템에서 자바 직렬화를 써야 할 이유는 전혀 없다. 객체와 바이트 시퀀스를 변환해주는 다른 매커니즘이 많이 있다.
- 크로스-플랫폼 구조화된 데이터 표현의 선두주자는 JSON과 프로토콜 버퍼다.
    - JSON: 브라우저와 서버의 통신용으로 설계, 텍스트 기반이라 사람이 읽을 수 있음. 
    - 프로토콜 버퍼: 서버 사이에 데이터를 교환하고 저장하기 위해 설계, 이진 표현이라 효율이 훨씬 높음.
- 자바 직렬화를 완전히 배제할 수 없을 때의 차선책은 **신뢰할 수 없는 데이터는 절대 역직렬화 하지 않는 것**
- 직렬화를 피할 수 없고 역질렬화한 데이터가 안전하지 완전히 확신할 수 없다면 객체 역직렬화 필ObjectInputFilter)터릴(java.io.을 사용하자.
    - 블랙리스트 방식보다는 화이트리스트 방식을 추천한다.(블랙리스트: 기본 수용, 화이트리스트: 기본 거부)
