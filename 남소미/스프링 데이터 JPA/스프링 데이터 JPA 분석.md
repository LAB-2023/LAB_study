# 스프링 데이터 JPA 분석

## 스프링 데이터 JPA 구현체 분석

**SimpleJpaRepository**

```java
@Repository
@Transactional(readOnly = true)
public class SimpleJpaRepository<T, ID> ...{
 @Transactional
 public <S extends T> S save(S entity) {
 if (entityInformation.isNew(entity)) {
 em.persist(entity);
 return entity;
 } else {
 return em.merge(entity);
 }
 }
 ...
}
```

- @Repository 적용
    - JPA 예외를 스프링이 추상화한 예외로 변환
- @Transactional 트랜잭션 적용
    - JPA의 모든 변경은 트랜잭션 안에서 동작
    - 변경(등록, 수정, 삭제) 메서드를 트랜잭션 처리
    - 서비스 계층에서 트랜잭션을 시작하지 않으면 리파지토리에서 트랜잭션 시작
    - 서비스 계층에서 트랜잭션을 시작하면 리파지토리는 해당 트랜잭션을 전파 받아서 사용
- @Transactional(readOnly = true)
    - 데이터 조회만 하는 트랜잭션에서 사용→ 플러시를 생략, 성능 향상
- **save() 메서드***
    - 새로운 엔티티면 저장( persist )
    - 새로운 엔티티가 아니면 병합( merge )

## 새로운 엔티티를 구별하는 방법

- **새로운 엔티티를 판단하는 기본 전략**
    - 식별자가 객체일 때 null 로 판단
    - 식별자가 자바 기본 타입일 때 0 으로 판단

Persistable

```java
public interface Persistable<ID> {
 ID getId();
 boolean isNew();
}
```

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
