# JPA 구동 방식
![](https://velog.velcdn.com/images/yseo14/post/de3860af-4a9b-464f-b40e-c5de46867700/image.png)


- JPA에서는 트랜잭션이라는 단위가 매우 중요. 모든 데이터를 변경하는 작업은 트랜잭션 안에서 해야함. 

```public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Member member = new Member();
        member.setId(1L);
        member.setName("HelloA");

        em.persist(member);

        tx.commit();

        em.close();

        emf.close();

    }
}
```
![](https://velog.velcdn.com/images/yseo14/post/138fbeac-50fd-423d-bfe4-b4f7a0392ca9/image.png)

![](https://velog.velcdn.com/images/yseo14/post/95cd122a-f648-43a6-afe1-9dafbacb0cd8/image.png)
persistence.xml의 옵션 덕분에 저런 식으로 쿼리가 어떻게 실행됐는지 확인 가능

> # 주의
- 엔티티 매니저 팩토리는 하나만 생성해서 애플리케이션 전체에서 공유
- 엔티티 매니저는 쓰레드 간에 공유 X
- JPA의 모든 데이터 변셩은 트랜잭션 안에서 실행
