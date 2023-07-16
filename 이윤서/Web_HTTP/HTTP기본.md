# HTTP
: HyperText Transfer Protocol

>
## 특징
- 클라이언트 서버 구조
- 무상태 프로토콜(스테이트리스), 비연결성
- HTTP 메시지
- 단순함, 확장 가능

---

### 클라이언트 서버 구조
- Request / Response 구조
- 클라이언트는 서버에 요청을 보내고, 응답을 대기
- 서버가 요청에 대한 결과를 만들어서 응답

![](https://velog.velcdn.com/images/yseo14/post/288ad5d8-3781-4ff5-9740-b05781d9f201/image.png)

--- 
### 무상태 프로토콜(stateless)
- 서버가 클라이언트의 상태를 보존 x
- 장점: 서버 확장성이 높음
- 단점: 클라이언트가 추가 데이터 전송

---
### 비연결성
- HTTP는 기본적으로 연결을 유지하지 않음
- 일반적으로 초 단위 이하의 빠른 속도로 응답
- 서버 자원을 매우 효율적으로 사용할 수 있음

---

### HTTP 메시지

>
### 시작 라인
![](https://velog.velcdn.com/images/yseo14/post/ca6cdabf-8747-4d6e-b97c-b28dfae47d2c/image.png)request-line = method SP(공백) request-target SP HTTP-version CRLF(엔터)
- 요청 메시지 - HTTP 메서드
![](https://velog.velcdn.com/images/yseo14/post/2c985e61-c8ef-4c89-bc55-a4735315a223/image.png)종류: GET, POST, PUT, DELETE ...
서버가 수행해야할 동작을 지정
- 요청 메시지 - 요청 대상
![](https://velog.velcdn.com/images/yseo14/post/66f85b4c-ce07-4a9c-9320-864b38a354d1/image.png)절대 경로 = "/"로 시작하는 경로
- 요청 메시지 - HTTP 버전
![](https://velog.velcdn.com/images/yseo14/post/44d3a179-b44a-48bb-887d-2f16d75b47fe/image.png)</br>
- 응답 메시지
![](https://velog.velcdn.com/images/yseo14/post/b12f9dc3-bdb5-4790-b0d4-569358a52435/image.png)status-line =  HTTP-version SP status-code SP reason-phrase CRLF
	-HTTP 버전
    -HTTP 상태 코드: 요청 성공, 실패를 나타냄.(200:성공, 400:클라이언트 요청 오류, 500: 서버 내부 오류)
    - 이유 문구: 사람이 이해할 수 있는 짧은 상태 코드 설명 글
### HTTP 헤더
![](https://velog.velcdn.com/images/yseo14/post/ec867ee2-56c0-4d80-aaf6-54fcd6b41817/image.png)
 - HTTP 전송에 필요한 모든 부가 정보
### HTTP 메시지 바디
- 실제 전송할 데이터
- HTML문서, 이미지, 영상 등 byte로 표현할 수 있는 모든 데이터 전송 가능