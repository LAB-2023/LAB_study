<div align=center><h3> Section3. HTTP 기본</h3></div>
<br/>

## HTTP

설명

- HTTP : HyperText Transfer Protocol
- HTTP 메시지에 모든 것을 전송
    - HTML, TEXT
    - IMAGE, 음성, 영상, 파일
    - JSON, XML(API)
    - 거의 모든 형태의 데이터 전송 가능하며 서버간에 데이터 주고 받을 때도 사용
- 기반 프로토콜
    - TCP : HTTP/1.1, HTTP/2
    - UDP : HTTP/3
    

**특징**

- 클라이언트 서버 구조
- 무상태 프로토콜(stateless), 비연결성
- HTTP 메시지
- 단순함, 확장 가능
<br/>

## 클라이언트 서버 구조

<img src="https://github.com/LAB-2023/LAB_study/assets/110540359/7b208d0c-b470-4a0a-9fa8-b5a4d2012559"/>

- Request Response 구조
- 클라이언트는 서버에 요청을 보내고, 응답을 대기
- 서버가 요청에 대한 결과를 만들어서 응답
- 클라이언트와 서버가 서로 **독립적**으로 작동
<br/>

## Stateful, Stateless

stateful(상태 유지)

<img src="https://github.com/LAB-2023/LAB_study/assets/110540359/9f801484-e936-4732-a860-00931fa481fc"/>


- 항상 같은 서버가 유지 되어야 함
- 서버가 상태 정보를 보관하여 해당 서버에 장애가 생기면 사용 불

<br/>
stateless(무상태)

<br/>
<img src="https://github.com/LAB-2023/LAB_study/assets/110540359/2ab6442d-1f2d-4c4b-9e06-fd4574fe20e1"/>

- 아무 서버나 호출 가능
- 서버가 상태 정보를 보관하지 않아 해당 서버에 장애가 생기면 다른 서버 사용 가능
- 수평 확장(scale out)에 유리
    - 요청이 많아졌을때 서버 대거 투입 가능

<br/>
실제 사용

- 모든 것을 무상태로 설계할 수 없는 경우도 있음
- 상태 유지가 필요한 경우 : 로그인
    - 로그인한 사용자의 경우 로그인 했다는 상태를 서버에 유지해야함
    - 일반적으로 브라우저 쿠키와 서버 세션 등을 사용해 상태 유지
- 상태 유지는 최소한만 사용
- **대용량 트래픽**에 대비하기 위해 **무상태를 유지**하는 것이 중요
<br/>

## 비 연결성(connectionless)


연결을 유지하는 모델

<img src="https://github.com/LAB-2023/LAB_study/assets/110540359/ceffb3c2-6cc9-49a4-b3d7-33be8ef87a7a"/>

- 요청 보내지 않는 클라이언트도 모두 연결을 유지하며 서버 자원 소모

<br/>
연결을 유지하지 않는 모델

<br/>
<img src="https://github.com/LAB-2023/LAB_study/assets/110540359/4686141b-a2f0-41df-8049-6bbe0af6cc11"/>

- 응답 후 TCP/IP 연결 종료

<br/>
비 연결성

- HTTP는 연결을 유지하지 않는 모델 사용
- 일반적으로 초 단위 이하의 빠른 속도로 응답
- 1시간동안 수천명이 서비스를 사용해도 실제 서버에서 동시에 처리하는 요청은 매우 작음
- 서버 자원의 효율적 사용 가능

<br/>
비 연결성의 한계

- TCP/IP 연결을 새로 맺어야함 - 3 way handshake 시간 추가
- 웹 브라우저로 사이트 요청시 HTML, 자바스크립트, CSS, 이미지 등 수 많은 자원 다운로드 필요

<br/>
한계 극복

- HTTP 지속 연결(Persistent Connections)로 문제 해결
    - 지속 연결 : HTTP에서 TCP/IP 연결을 일정기간 열어두고 여러 요청을 하는 것
- HTTP/2, HTTP/3에서 최적화

<br/>

## HTTP 메시지

HTTP 메시지 구조

- start-line 시작 라인
- header 헤더
- empty line 공백라인 (CRLF)
- message body

<img src="https://github.com/LAB-2023/LAB_study/assets/110540359/3435f9ca-f571-451f-8d6e-8071449c439d"/>

<img src="https://github.com/LAB-2023/LAB_study/assets/110540359/70704994-caac-44c2-af9f-c854ef79b334"/>


<br/>
시작 라인 - 빨강 박스

- start-line = request-line or status-line (요청 혹은 응답)
- **request-line** = method SP request-target SP HTTP-version CRLF
    - method : HTTP 메서드 (GET, POST 등)로 서버가 수행해야할 동작 지
    - request-target : 요청대상 (보통 URL path와 query 포함)
    - HTTP-version : HTTP 버전
    
    (SP = 스페이스, CRLF = 엔터)
    
- **status-line** = HTTP-version SP status-code SP reason-phrase CRLF
    - HTTP-version : HTTP 버전
    - status-code : HTTP 상태 코드로 요청 성공 또는 실패를 나타냄
        
        (200:성공, 400:클라이언트 요청 오류, 500:서버 내부 오류)
        
    - reason-phrase : 이유 문구 - 사람이 이해할 수 있는 짧은 상태 코드 설명 글

<br/>
HTTP 헤더 - 노랑 박스

- header-filed = filed-name: OWS field-value OWS (OWS:띄어쓰기 허용)
    - field-name은 대소문자 구분 없음
    - HTTP 전송에 필요한 모든 부가정보
        
        (메시지 바디 내용, 메시지 바디 크기, 압축, 인증, 요청 클라이언트 정보 등)
        
    - 표준 헤더 종류 많으며 임의의 헤더 추가도 가
    
<br/>
HTTP 메시지 바디 - 파랑 박스

- 실제 전송할 데이터
- HTML 문서, 이미지, 영상, JSON 등등 byte로 표현할 수 있는 모든 데이터 전송 가능
