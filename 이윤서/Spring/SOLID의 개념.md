![](https://velog.velcdn.com/images/yseo14/post/d164f184-231a-4718-b2fb-77998ce5ec3d/image.png)


> # 좋은 객체 지향 설계의 5가지 원칙(SOLID)

## SRP: 단일 책임 원칙 (single responsibility principle)
한 클래스는 하나의 책임만 가져야함. 
하나의 책임이라는 것은 모호함. 
그 기준이 '변경'임. 즉 변경이 있을 때 파급 효과가 적으면 단일 책임 원칙을 잘 따른 것. ex) UI 변경, 객체의 생성과 사용을 분리.
<hr>

## OCP: 개방-폐쇄 원칙 (Open/closed principle)
소프트웨어 요소는 확장에는 열려 있으나 변경에는 닫혀 있어야함.
다형성을 활용할 것.
인터페이스를 구현한 새로운 클래스를 하나 만들어서 새로운 기능을 구현.
> 문제점: 
	1. 구현 객체를 변경하려면 클라이언트 코드를 변경해야한다.
	2. 다형성을 사용했지만 OCP 원칙을 지킬 수 없다. 
    -> 객체를 생성하고, 연관 관계를 맺어주는 별도의 조립, 설정자가 필요.  >> Spring
    
<hr>

## LSP: 리스코프 치환 원칙 (Liskov substitution principle)
<hr>

## ISP: 인터페이스 분리 원칙 (Interface segregation principle)
특정 클라이언트를 위한 인터페이스 여러 개가 범용 인터페이스 하나보다 낫다. 
인터페이스가 명확해지고, 대체 가능성이 높아짐.
ex) 자동차 인터페이스 -> 운전 인터페이스, 정비사 인터페이스

<hr>

## DIP: 의존관계 역전 원칙 (Dependency inversion principle)
구현 클래스에 의존하지 말고, 인터페이스에 의존하라는 뜻


