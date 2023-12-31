# HTTP(HyperText Transfer Protocol)

### 특징 

+ 클라이언트 서버 구조
  + 서로 독립적으로 진행 가능  
  + Request Response 구조
  + 클라이언트는 서버에 요청을 보내고, 응답을 대기
  + 서버가 요청에 대한 결과를 만들어서 응답

+ HTTP 메시지

+ 단순함, 확장 가능

+ 무상태 프로토콜(스테이스리스, Stateless), 비연결성
  + 서버가 클라이언트의 상태를 보존X
  + 장점: 서버 확장성 높음(스케일 아웃)
  + 단점: 클라이언트가 추가 데이터 전송

  + 상태 유지(Stateful) : 중간에 다른 서버로 바뀌면 안된다.

    (중간에 다른 서버로 바뀔 때 상태 정보를 다른 서버에게 미리 알려줘야 한다.
    혹은 쿠키 사용 )
    
  + 무상태(Stateless): 중간에 다른 서버로 바뀌어도 된다.
    + 갑자기 클라이언트 요청이 증가해도 서버를 대거 투입할 수 있다.
    + 무상태는 응답 서버를 쉽게 바꿀 수 있다. -> 무한한 서버 증설 가능

---
### 비 연결성(connectionless)

+ HTTP는 기본이 연결을 유지하지 않는 모델
+ 일반적으로 초 단위의 이하의 빠른 속도로 응답
+ 1시간 동안 수천명이 서비스를 사용해도 실제 서버에서 동시에 처리하는 요청은 수십개 이
하로 매우 작음
  + 예) 웹 브라우저에서 계속 연속해서 검색 버튼을 누르지는 않는다.
+ 서버 자원을 매우 효율적으로 사용할 수 있음

### 단점
+ TCP/IP 연결을 새로 맺어야 함 - 3 way handshake 시간 추가
+ 웹 브라우저로 사이트를 요청하면 HTML 뿐만 아니라 자바스크립트, css, 추가 이미지 등
등 수 많은 자원이 함께 다운로드

