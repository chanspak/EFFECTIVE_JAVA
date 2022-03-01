# ITEM60. 정확한 답이 필요하다면 float와 double은 피하라

- float와 double 타입은 특히 금융 관련 계산과는 맞지 않는다.
```java
System.out.println(1.03 - 0.42);        //0.6100000000000001 출력
System.out.println(1.00 - 9 * 0.10);    //0.09999999999999998 출력
```
- 금융 계산에는 BigDecimal, int 혹은 long을 사용해야 한다.
- BigDecimal에는 단점이 두 가지 있다. 기본 타입보다 쓰기가 훨씬 불편하고, 훨씬 느리다.
- BigDecimal의 대안으로 int 혹은 long 타입을 쓸 수 있다.

- float, double 부동 소수점: https://m.blog.naver.com/kmc7468/220990920730