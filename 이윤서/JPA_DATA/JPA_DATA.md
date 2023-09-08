# 공통 인터페이스
예시 코드
```
public interface MemberRepository extends JpaRepository<Member,Long> {
}
```
extends JpaRepository<T, ID>
T: 엔티티 타입
ID: 식별자 타입(PK)
공통 CRUD 제공


## 쿼리 메소드 기능
- ### 메소드 이름으로 쿼리 생성
- ### 메소드 이름으로 JPA NamedQuery 호출
- ### @Query 어노테이션을 사용해서 레포지토리 인터페이스에 쿼리 직접 정의

