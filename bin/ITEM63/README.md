# ITEM63. 문자열 연결은 느리지 주의하라.

- 문자열 연결 연산자로 문자열 n개를 잇는 시간은 2제곱에 비례한다.
- 성능을 포기하고 싶지 않다면 String 대신 StringBuilder를 사용하자. (StringBuffer 는??)
- **많은 문자열을 연결할 때는 문자열 연결 연산자(+)를 피하자**. 대신 StringBuilder의 appen 메서드를 사용하라.