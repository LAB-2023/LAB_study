# HTTP 기본

### HTTP ( HyperText Transfer Protocol )

- **HTTP 메시지에 모든 것을 전송**
    - 거의 모든 형태의 데이터 전송 가능
        - HTML, TEXT, IMAGE, 음성, 영상, 파일, JSON, XML (API)
    - 서버간에 데이터를 주고 받을 때도 대부분 HTTP 사용
- HTTP/1.1 : 가장 많이 사용, 우리에게 가장 중요한 버전
- 기반 프로토콜
    - TCP : HTTP/1.1, HTTP/2
    - UDP : HTTP/3

---

### 특징

### 1) 클라이언트 서버 구조

- Request Response 구조
- 클라이언트는 서버에 요청을 보내고, 응답을 대기
- 서버가 요청에 대한 결과를 만들어서 응답

### 2) Stateful, Stateless

- Stateless ( 무상태 프로토콜 : 상태 유지 x )
    - 서버가 클라이언트의 상태를 보존X
    - 장점: 서버 확장성 높음(스케일 아웃)
    - 단점: 클라이언트가 추가 데이터 전송
- Stateful vs Stateless
    - **Stateful** : 중간에 다른 점원으로 바뀌면 X, 정보를 다른 점원에게 미리 알려줘야함.
        - **최소한만 사용**
    - **Stateless** : 중간에 다른 점원으로 바뀌어도 O
        - 응답 서버를 쉽게 바꿀 수 O -> **무한한 서버 증설 가능**

### 3) 비 연결성(connectionless)

HTTP : **연결을 유지하지 않는 모델**

- 일반적으로 초 단위의 이하의 빠른 속도로 응답
- 서버 자원을 매우 효율적으로 사용할 수 있음

문제

- TCP/IP 연결을 새로 맺어야 함 - 3 way handshake 시간 추가
    - 웹 브라우저로 사이트를 요청하면 HTML 뿐만 아니라 자바스크립트, css, 추가 이미지 등등 수 많은 자원이 함께 다운로드

→ **HTTP 지속 연결(Persistent Connections)**로 문제 해결

![Untitled](https://github.com/LAB-2023/LAB_study/assets/125250173/4507c521-4c76-4700-a293-aa5979e4c3d9)

### 4) HTTP 메시지

![Untitled 1](https://github.com/LAB-2023/LAB_study/assets/125250173/14f38df2-ca30-4083-b675-ba80c77f61bc)

- **요청 메시지**
    - start-line = request-line / status-line
    - request-line = method SP(공백) request-target SP HTTP-version CRLF(엔터)
    
    - **HTTP 메서드**
        - 종류: GET, POST, PUT, DELETE...
        - 서버가 수행해야 할 동작 지정
    - **요청 대상**
        - absolute-path[?query] (절대경로[?쿼리])
            
            ( 절대경로= "/" 로 시작하는 경로 )
            
    - **HTTP 버전**

![Untitled 2](https://github.com/LAB-2023/LAB_study/assets/125250173/7c754e30-175b-44a3-94a2-1165a6ad7471)

- **응답 메시지**
    - start-line = request-line / status-line
    - status-line = HTTP-version SP status-code SP reason-phrase CRLF
    
    - **HTTP 버전**
    - **HTTP 상태 코드**
        - 요청 성공, 실패를 나타냄
        - 200: 성공, 400: 클라이언트 요청 오류, 500: 서버 내부 오류
    - **이유 문구**
        - 사람이 이해할 수 있는 짧은 상태 코드 설명 글

![Untitled 3](https://github.com/LAB-2023/LAB_study/assets/125250173/dbd5112d-9efe-4666-af18-af05e6ac2db7)

- **HTTP 헤더**
    - HTTP 전송에 필요한 모든 부가정보
    - 표준 헤더가 너무 많음
    - 필요시 임의의 헤더 추가 가능

![Untitled 4](https://github.com/LAB-2023/LAB_study/assets/125250173/0cedf9d4-f3fb-4725-970b-e7a3fb8776ba)

- **HTTP 메시지 바디**
    - 실제 전송할 데이터
    - byte로 표현할 수 있는 모든 데이터 전송 가능
    

---

<aside>
💡 **단순함 확장 가능**
• HTTP 메시지도 매우 단순
• 크게 성공하는 표준 기술은 단순하지만 확장 가능한 기술

</aside>
