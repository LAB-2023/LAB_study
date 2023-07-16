# HTTP 헤더 - 일반 헤더

### HTTP 헤더

- header-field = field-name ":" OWS field-value OWS

![Untitled](https://github.com/LAB-2023/LAB_study/assets/125250173/13e44b2d-ae5c-497e-bd4d-c824521a796b)

- 용도
    - HTTP 전송에 필요한 모든 부가정보
    - 표준 헤더가 너무 많음
    - 필요시 임의의 헤더 추가 가능
    
- RFC723x ( 최신 버전 )
    - 표현 = 표현 메타데이터 + 표현 데이터
- HTTP BODY
    - 메시지 본문(message body)을 통해 표현 데이터 전달
    - 메시지 본문 = 페이로드(payload)
    - 표현은 요청이나 응답에서 전달할 실제 데이터
    - 표현 헤더는 표현 데이터를 해석할 수 있는 정보 제공
        - 데이터 유형(html, json), 데이터 길이, 압축 정보 등등
        

---

### 표현

- 표현 헤더는 전송, 응답 둘다 사용
- ex )
    
    ( 회원 리소스를 HTML이라는 표현으로 전송할지 json이라는 표현으로 전송할지 )
    

- Content-Type
    - 표현 데이터의 형식 설명
    - 미디어 타입, 문자 인코딩
- Content-Encoding
    - 표현 데이터 압축하기 위해 사용 ( gzip )
    - 데이터를 전달하는 쪽 : 압축 후 인코딩 헤더 추가
    - 데이터를 읽는 쪽 : 인코딩 헤더의 정보로 압축 해제
- Content-Language
    - 표현 데이터의 자연 언어를 표현 ( ko, en, en-US )
- Content-Length
    - 표현 데이터의 길이 ( 바이트 단위 )
    - Transfer-Encoding(전송 코딩)을 사용하면 Content-Length를 사용 X
    

---

### 콘텐츠 협상

- 클라이언트가 선호하는 표현 요청
- 협상 헤더는 요청시에만 사용
    - Accept : 클라이언트가 선호하는 미디어 타입 전달
    - Accept-Charset : 클라이언트가 선호하는 문자 인코딩
    - Accept-Encoding : 클라이언트가 선호하는 압축 인코딩
    - Accept-Language : 클라이언트가 선호하는 자연 언어
- ex )
    
    ( 클라이언트가 한국어 브라우저 이용할 때 한국어로 보내달라하면 서버에서 한국어로 보내줌, 한국어 지원하지 않는 경우 기존 **우선순위**에 맞춰서 독일어로 보내줌 ) 
    

- **협상과 우선순위**
    - **Quality Values(q)** 값 사용
        - 0~1, 클수록 높은 우선순위 ( 생략하면 1 )
            
            ex ) Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7
            
        - 구체적인 것이 우선 ( 긴거 )
            
            ex ) Accept: text/*, text/plain, text/plain;format=flowed, */*
            
        - 구체적인 것을 기준으로 미디어 타입을 맞춤
        ex ) Accept: text/*;q=0.3, text/html;q=0.7, text/html;level=1, text/html;level=2;q=0.4, */*;q=0.5

---

### 전송 방식

- 단순 전송 : 요청하고 한번에 전체를 받음
- 압축 전송 : 전체를 압축하여 받음
    
    → Content-Encoding 추가해서 받은 쪽에서 풀 수 있게
    
- 분할 전송 : 용량이 큰 걸 분할해서 빠르게 보냄
    
    → 이 경우 Content-Length 사용불가. chunk마다의 길이를 예측할 수 없음
    
- 범위 전송 : 지정한 범위만 전송 ( 재전송 받아야할 때 필요한 부분만 받을 수 있음 )

---

### 일반 정보

- From —> 잘 사용X
    - 유저 에이전트의 이메일 정보
    - 요청에서 사용
- Referer
    - 현재 요청된 페이지의 이전 웹 페이지 주소
    - A -> B,  B를 요청할 때 Referer: A 를 포함해서 요청
    - 유입 경로 분석 가능
    - 요청에서 사용
- User-Agent
    - 클라이언트의 애플리케이션 정보
    - 통계 정보
    - 어떤 종류의 브라우저에서 장애가 발생하는지 파악
    - 요청에서 사용
- Server
    - 요청을 처리하는 오리진 서버의 소프트웨어 정보
        
        ( 오리진 서버 : 중간에 거치는 서버가 아닌 실제 나의 요청에 응답을 해주는 서버 )
        
    - 응답에서 사용
- Date
    - 메시지가 생성된 날짜
    - 응답에서 사용

---

### 특별한 정보

- **Host** ( 필수 !!! )
    - 요청한 호스트 정보(도메인)
    - 하나의 서버가 여러 도메인을 처리해야 할 때
    - 하나의 IP 주소에 여러 도메인이 적용되어 있을 때
    - 요청에서 사용
- Location
    - 페이지 리다이렉션 ( 앞에서 설명 )
- Allow
    - 허용 가능한 HTTP 메서드
    - 405 (Method Not Allowed) 에서 응답에 포함해야함
- Retry-After
    - 유저 에이전트가 다음 요청을 하기까지 기다려야 하는 시간
    - 503 (Service Unavailable): 서비스가 언제까지 불능인지 알려줄 수 있음

---

### 인증

- Authorization
    - 클라이언트 인증 정보를 서버에 전달
- WWW-Authenticate
    - 리소스 접근시 필요한 인증 방법 정의
    - 401 Unauthorized 응답과 함께 사용

---

### 쿠키

- Set-Cookie
    - 서버에서 클라이언트로 쿠키 전달(응답)
- Cookie
    - 클라이언트가 서버에서 받은 쿠키를 저장하고, HTTP 요청시 서버로 전달

- 사용자 로그인 세션 관리 / 광고 정보 트래킹 등에 사용
- 쿠키는 항상 서버에 전송
    - 네트워크 트래픽 추가 유발
    - 최소한의 정보만 사용
- 보안에 민감한 데이터는 저장 X

**생명주기**

- expires : 만료일이 되면 쿠키 삭제
- max-age : 0이나 음수를 지정하면 쿠키 삭제
- 세션 쿠키 : 만료 날짜를 생략하면 브라우저 종료시 까지만 유지
- 영속 쿠키 : 만료 날짜를 입력하면 해당 날짜까지 유지

**도메인**

- 명시 : 명시한 문서 기준 도메인 + 서브 도메인 포함
    - domain = example.org
    - dev.example.org에서도 쿠키 접근
- 생략 : 현재 문서 기준 도메인만 적용
    - example.org에서 쿠키 생성, domian 지정 생략하면 example.org에서만 쿠키 접근

**경로**

- path=/ 루트로 지정
    - 이 경로를 포함한 하위 경로 페이지만 쿠키 접근
    - path=/home
    - /home, /home/level1… 가능
    - /hello 불가능

**보안**

- Secure
    - 쿠키는 http,https 구분하지 않고 전송
    - Secure을 적용하면 https인 경우만 전송
- HttpOnly
    - XSS 공격 방지
    - 자바스크립트에서 접근 불가
    - HTTP 전송에만 사용
- SameSite
    - XSRF 공격 방지
    - 요청 도메인과 쿠키에 설정된 도메인이 같은 경우만 전송
