# ITEM65 리플렉션보다는 인터페이스를 사용하라

- 리플렉션 기능(java.lang.reflect)을 이용하면 프로그램에서 임의의 클래스에 접근 할 수 있다.
- Constructor, Method, Field 인스턴스를 이용해 각각에 연결된 실제 생성자, 메서드, 필드를 조작할 수도 있다.
- 리플렉션 단점
    - **컴파일타임 타입 검사가 주는 이점을 하나도 누릴수 없다.**
    - **리플렉션을 이용하면 코드가 지저분하고 장황해진다.**
    - **성능이 떨어진다.**
- 리플렉션은 인스턴스 생성에만 쓰고, 이렇게 만든 인스턴스는 인터페이스나 상위 클래스로 참조해 사용하자.