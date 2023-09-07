# HTTP 기본

#### HTTP(Hyper Text Transfer Protocol)

###### 최근에는 모든 전송을 HTTP로 한다고 해도 과언이 아니다.

-  HTML, TEXT
- IMAGE, 음성, 영상, 파일
- JSON, XML (API)
- 거의 모든 형태의 데이터 전송 가능
- 서버간에 데이터를 주고 받을 때도 대부분 HTTP 사용
-  지금은 HTTP 시대!

###### HTTP 특징
- 클라이언트 서버 구조
- 무상태 프로토콜(스테이스리스), 비연결성
- HTTP 메시지
- 단순함, 확장 가능

###### 클라이언트 서버 구조
- Request, Response 구조
- 클라이언트는 서버에 요청을 보내고, 응답을 대기
- 서버가 요청에 대한 결과를 만들어서 응답.


###### 무상태 프로토콜(Stateless)
- 서버가 클라이언트의 상태를 보존X
- 장점: 서버 확장성 높음(스케일 아웃)
- 단점: 클라이언트가 추가 데이터 전송

###### Stateful, Stateless 차이(정리)
- 상태 유지: 중간에 다른 점원으로 바뀌면 안된다.
- 무상태: 중간에 다른 점원으로 바뀌어도 된다.
- 무상태는 응답 서버를 쉽게 바꿀 수 있다. -> 무한한 서버 증설가능

###### Stateless(실무 한계)
- 모든 것을 무상태로 설계 할 수 있는 경우도 있고 없는 경우도 있다.
- 무상태
 예) 로그인이 필요 없는 단순한 서비스 소개 화면
- 상태 유지
 예) 로그인
- 로그인한 사용자의 경우 로그인 했다는 상태를 서버에 유지
- 일반적으로 브라우저 쿠키와 서버 세션등을 사용해서 상태 유지
- 상태 유지는 최소한만 사용

#### 비 연결성(connectionless)

###### 비 연결성
- HTTP는 기본이 연결을 유지하지 않는 모델
- 일반적으로 초 단위의 이하의 빠른 속도로 응답
- 1시간 동안 수천명이 서비스를 사용해도 실제 서버에서 동시에 처리하는 요청은 수십개 이
하로 매우 작음
 예) 웹 브라우저에서 계속 연속해서 검색 버튼을 누르지는 않는다.
-  서버 자원을 매우 효율적으로 사용할 수 있음


#### HTTP 메시지

###### 모든것이 HTTP - 한번 더!
- HTTP 요청 메시지
 예) GET /search?q=hello&hl=ko HTTP/1.1 Host: www.google.com
- HTTP 응답 메시지
 예) HTTP/1.1 200 OK
Content-Type: text/html;charset=UTF-8
Content-Length: 3423
<html>
 <body>...</body>
</html>


###### HTTP 메시지 구조
- 1.start-line 시작 라인
- 2.header 헤더
- 3.empty line 공백 라인 (CRLF)
- 4.message body
- *요청 메시지도 body 본문을 가질 수 있음*

###### 시작 라인(요청 메시지)
- start-line = request-line / status-line
- request-line = method SP(공백) request-target SP HTTP-version CRLF(엔터)
- HTTP 메서드 (GET: 조회)
- 요청 대상 (/search?q=hello&hl=ko)
- HTTP Version


###### 시작 라인(요청 메시지 - HTTP 메서드)
- 종류: GET, POST, PUT, DELETE...
- 서버가 수행해야 할 동작 지정
- GET: 리소스 조회
- POST: 요청 내역 처리

###### 시작 라인(응답 메시지)
- start-line = request-line / status-line
- status-line = HTTP-version SP status-code SP reason-phrase CRLF
- HTTP 버전
- HTTP 상태 코드: 요청 성공, 실패를 나타냄
- 200: 성공
- 400: 클라이언트 요청 오류
- 500: 서버 내부 오류
- 이유 문구: 사람이 이해할 수 있는 짧은 상태 코드 설명 글

