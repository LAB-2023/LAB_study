<div align=center><h3> Section6. 스프링 데이터 JPA 분석 </h3></div>
<br/>


## 스프링 데이터 JPA 구현체 분석

SimpleJpaRepository

```java
@Repository
@Transactional(readOnly = true)
public class SimpleJpaRepository<T, ID> ...{

	 @Transactional
	 public <S extends T> S save(S entity) {
		 if (entityInformation.isNew(entity)) {
			 em.persist(entity);
			 return entity;
		 } 
		 else {
			 return em.merge(entity);
		 }
	 }

	 ...
}
```

- @Repository
    - 스프링 빈으로 등록
    - JPA 예외를 스프링이 추상화한 예외로 변경
- @Transactional
    - JPA의 모든 변경은 트랜잭션 안에서 동작
    - 스프링 데이터 JPA는 변경(등록, 수정, 삭제) 메서드를 트랜잭션 처리
    - 서비스 계층에서 트랜잭션을 시작 X → 리파지토리에서 트랜잭션 시작
    - 서비스 계층에서 트랜잭션을 시작 → 리파지토리는 해당 트랜잭션을 전파 받아서 사용
- @Transactional(readOnly = true)
    - 데이터 단순 조회시 사용
    - 약간의 성능 최적화
- save() 메서드
    - 새로운 엔티티 저장 → persist
    - 새로운 엔티티가 아니면 병합 → merge
        - merge 동작 방식 : DB에서 엔티티 조회 후 덮어쓰기, 가급적이면 변경 감지를 사용 할 것
<br/>


## 새로운 엔티티 구별하는 방법

새로운 엔티티 판단 기본 전략

- 식별자가 객체 : null로 판단 = 새 것 = persist
- 식별자가 자바 기본 타입 : 0으로 판단 = DB 확인 = merge
    - 아이디를 @GeneratedValue 대신 직접 할당하는 경우에도 여기에 해당
    - merge는 DB를 한번 호출해서 값을 확인하기 때문에 비효율적
        
        → Persistable 인터페이스를 구현해 판단 로직 변경
        
<br/>

Persistable 인터페이스

```java
public interface Persistable<ID> {
	 ID getId();
	 boolean isNew();
}
```

<br/>

Persistable 구현

```java
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String> {
	 @Id
	 private String id;

	 @CreatedDate
	 private LocalDateTime createdDate;

	 public Item(String id) {
		 this.id = id;
	 }

	 @Override
	 public String getId() {
		 return id;
	 }

	 @Override
	 public boolean isNew() {
		 return createdDate == null; 
	 }
}
```

- 생성일을 통해 새로운 객체인지 판단 (isNew)
    - 새로운 객체라면 생성일 == null
