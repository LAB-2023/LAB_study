# HTTP 메서드
#### API URI 설계
- 회원 목록 조회 /read-member-list
- 회원 조회 /read-member-by-id
- 회원 등록 /create-member
- 회원 수정 /update-member
- 회원 삭제 /delete-member

#### API URI 설계 리소스 식별, URI 계층 구조 활용
- 회원 목록 조회 /members
- 회원 조회 /members/{id}
- 회원 등록 /members/{id}
- 회원 수정 /members/{id}
- 회원 삭제 /members/{id}
- 참고: 계층 구조상 상위를 컬렉션으로 보고 복수단어 사용 권장(member -> members)

#### 리소스와 행위을 분리
###### 가장 중요한 것은 리소스를 식별하는 것
- URI는 리소스만 식별! 
- 리소스와 해당 리소스를 대상으로 하는 행위을 분리
- 리소스: 회원
- 행위: 조회, 등록, 삭제, 변경
- 리소스는 명사, 행위는 동사 (미네랄을 캐라)
- 행위(메서드)는 어떻게 구분?

#### HTTP 메서드 종류
###### 주요 메서드(이거 중요해! 이해를 해 이해를!!)
- GET: 리소스 조회
- POST: 요청 데이터 처리, 주로 등록에 사용
- PUT: 리소스를 대체, 해당 리소스가 없으면 생성
- PATCH: 리소스 부분 변경
- DELETE: 리소스 삭제

#### GET
###### 메시지 형식
###### GET /search?q=hello&hl=ko HTTP/1.1
###### Host: www.google.com

- 리소스 조회
- 서버에 전달하고 싶은 데이터는 query(쿼리 파라미터, 쿼리 스트링)를 통해서 전달
- 메시지 바디를 사용해서 데이터를 전달할 수 있지만, 지원하지 않는 곳이 많아서 권장하지
않음

#### POST (진짜 중요!)
###### 메시지 형식
###### POST /members HTTP/1.1
###### Content-Type: application/json
###### {
###### "username": "hello",
###### "age": 20
###### }
- 요청 데이터 처리
- 메시지 바디를 통해 서버로 요청 데이터 전달
- 서버는 요청 데이터를 처리
- 메시지 바디를 통해 들어온 데이터를 처리하는 모든 기능을 수행한다.
- 주로 전달된 데이터로 신규 리소스 등록, 프로세스 처리에 사용

#### POST(정리)
1. 새 리소스 생성(등록) 
- 서버가 아직 식별하지 않은 새 리소스 생성
2. 요청 데이터 처리
- 단순히 데이터를 생성하거나, 변경하는 것을 넘어서 프로세스를 처리해야 하는 경우
- 예) 주문에서 결제완료 -> 배달시작 -> 배달완료 처럼 단순히 값 변경을 넘어 프로세스의 상태가 변경되는 경우
- POST의 결과로 새로운 리소스가 생성되지 않을 수도 있음
- 예) POST /orders/{orderId}/start-delivery (컨트롤 URI)
3. 다른 메서드로 처리하기 애매한 경우
- 예) JSON으로 조회 데이터를 넘겨야 하는데, GET 메서드를 사용하기 어려운 경우
- 애매하면 POST


#### PUT
1. 리소스를 대체
- 리소스가 있으면 대체
- 리소스가 없으면 생성
- 쉽게 이야기해서 덮어버림
2. 중요! 클라이언트가 리소스를 식별
- 클라이언트가 리소스 위치를 알고 URI 지정
- POST와 차이점(인자 값이 하나가 안와도 완전하게 덮어버린다.)

#### PATCH
- 리소스 부분 변경(예를 들면 age값만 보냈을 경우 age값만 바뀌고 나머지 인자는 그대로 유지!!)

#### DELETE
- 리소스 제거