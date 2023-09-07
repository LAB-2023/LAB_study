<div align=center><h3> Section7. HTTP 헤더1 - 일반 헤더</h3></div>
<br/>

## HTTP 헤더의 변화

분류(과거-RFC2616)

- General 헤더 : 메시지 전체에 적용되는 정보 (Connection)
- Request 헤더 : 요청 정보 (User-Agent : 요청 보내는 디바이스의 정보)
- Response 헤더 : 응답 정보 (Server)
- Entity 헤더 : 엔티티 바디 정보 (Content-Type, Content-Length)
    - 메시지 바디는 엔티티 바디 전달하는데 사용
    - 엔티티 바디는 요청이나 응답에서 전달할 실제 데이터
    - 엔티티 헤더는 엔티티 본문의 데이터를 해석할 수 있는 정보 제공
        
        (데이터 유형, 길이, 압축 정보 등)
        
<br/>
현재(RFC7230~RFC7235)

- **엔티티(Entity) → 표현(Representation)**
- Representation = representation Metadata + Representation Data
- 메시지 바디를 통해 표현 데이터 전달
- 표현은 요청이나 응답에서 전달할 실제 데이터
- 표현 헤더는 표현 데이터를 해석할 수 있는 정보 제공
<br/>

## 표현

종류 (전송, 응답 모두 사용)

- Content-Type : 표현 데이터의 형식
    - 미디어 타입, 문자 인코딩
    - ex) text/html; charset=utf-8 , application/json , image/png
- Content-Encoding : 표현 데이터의 압축 방식
    - 표현 데이터 인코딩 → 압축하기 위해 사용
    - 데이터 전달하는 곳에서 압축 후 인코딩 헤더 추가
    - 데이터 읽는 곳에서 인코딩 헤더 정보를 통해 압축 해제
    - ex) gzip, deflate, identity - 압축 x
- Content-Language : 표현 데이터의 자연 언어
    - ex) ko, en, en-US
- Content-Length : 표현 데이터의 길이
    - 바이트 단위
    - Transfer-Encoding과 같이 사용 불가
<br/>

## 협상

설명

- Content Negotiation
- 클라이언트가 선호하는 표현을 요청하는 것
- 협상 헤더는 요청시에만 사용

<br/>
종류

- Accept : 클라이언트가 선호하는 미디어 타입 전달
- Accept-Charset : 클라이언트가 선호하는 문자 인코딩
- Accept-Encoding : 클라이언트가 선호하는 압축 인코딩
- Accpet-Language : 클라이언트가 선호하는 자연 언어

<br/>
협상과 우선순위

- Quality Values(q) 값 사용
    - 0~1, **클수록 높은 우선순위**
    - 값 생략시 1
    - 예시
        - Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7
        - q값으로 우선순위를 부여해 우선순위가 높은 언어를 지원하지 않을때 차선택을 찾음
- 구체적인 것 우선
    - 구체적인 것이 높은 우선순위를 가짐
    - 예시 : Accept: text/*, text/plain, text/plain;format=flowed, */*
        1. text/plain;format=flowed
        2. text/plain
        3. text/*
        4. */*
- 구체적인 것을 기준으로 미디어 타입 맞춤
    - 미디어타입
      <br/>
        <img src="https://github.com/LAB-2023/LAB_study/assets/110540359/bf715c15-c94d-461a-add0-0f25edaba7c1"/>

        
    - 예시 : Accept: text/*;q=0.3, text/html;q=0.7, text/html;level=1,
    text/html;level=2;q=0.4, */*;q=0.5
        - text/*같은 경우 *에 모든게 들어올 수 있으니 더 구체적인 text/plain을 기준으로 q값을 정한다는 뜻인듯!
<br/>

## 전송 방식


종류

- 단순 전송
    - Content-Length
    - 메시지 바디에 대한 Content-Length 지정
    - 단순하게 한 번 요청하고, 한번에 받음
- 압축 전송
    - Content-Encoding
    - 서버에서 압축해서 용량을 줄인채로 전송
    - Content-Encoding 헤더를 통해 어떤 방식으로 압축했는지 알려줌
- 분할 전송
    - Transfer-Encoding
    - chuncked를 통해 덩어리로 분할해서 보냄
    - 마지막엔 0으로 표시, /r/n으로 끝 나타냄
    - **Content-Length 사용 불가** : 쪼개서 보내면 길이를 미리 알 수 없기 때문
   <br/> 
    <img src="https://github.com/LAB-2023/LAB_study/assets/110540359/d916ca15-fb6e-416e-9899-c06b793e059b"/>
- 범위 전송
    - Range, Content-Range
    - 전송 받아 올 내용의 범위를 지정할 수 있음
    <br/>
    <img src="https://github.com/LAB-2023/LAB_study/assets/110540359/f2e6a01b-1c6d-4067-8582-59f4d1a5009a"/>
<br/>

## 일반 정보

종류

- From : 유저 에이전트의 이메일 정보
    - 잘 사용안함
    - 요청에서 사용
- Referer : 이전 웹 페이지 주소
    - 현재 요청된 페이지의 이전 웹 페이지 주소
    - 유입 경로 분석 가능
    - 요청에서 사용
- User-Agent : 유저 에이전트 애플리케이션 정보
    - 요청에서 사용
- Server : 요청을 처리하는 오리진 서버의 소프트웨어 정보
    - 서버는 프록시 서버와 같이 중간의 다른 여러 서버를 거쳐올 수 있는데, 이러한 서버들이 아닌 내가 데이터를 요청한 가장 말단의 서버
    - 응답에서 사용
- Data : 메시지가 생성된 날짜
    - 응답에서 사용
<br/>

## 특별한 정보


종류

- Host : 요청한 호스트 정보(도메인)
    - 필수
    - 하나의 서버가 여러 도메인을 처리해야 할 때,
    - 하나의 IP 주소에 여러 도메인이 적용되어 있을 때 구분하기 위해 사용
    - 요청에서 사용
   <img src="https://github.com/LAB-2023/LAB_study/assets/110540359/8d09b4b0-c15c-4dd8-a776-287dd9472ff5"/>

    
- Location : 페이지 리다이렉션에 사용
    - 상태코드 3xx 응답의 결과에 Location 헤더가 있으면, 위치로 자동이동
    - 201(Created)의 Location 값은 요청에 의해 생성된 리소스 URI
- Allow : 허용 가능한 HTTP 메서드
    - 405(Method Not Allowed)에서 응답에 포함해야함
    - Allow : GET, HEAD, PUT
- Retry-After : 유저 에이전트가 다음 요청을 하기까지 기다려야 하는 시간
    - 503 (Service Unavailable) : 서비스가 언제까지 불능인지 알려줄 수 있음
    - GMT 기준 날짜 표기 or 초 단위 시간 표기
<br/>

## 인증


종류

- Authorization : 클라이언트 인증 정보를 서버에 전달
- WWW-Authenticate : 리소스 접근시 필요한 인증 방법 정의
    - 401 Unauthorized 응답과 함께 사용
<br/>

## 쿠키


종류

- Set-Cookie : 서버에서 클라이언트로 쿠키 전달(응답)
- Cookie : 클라이언트가 서버에서 받은 쿠키를 저장하고, HTTP 요청시 서버로 전달

<br/>
쿠키 미사용시 문제점

- HTTP는 무상태 프로토콜
- 클라이언트가 요청과 응답을 주고 받으면 연결이 끊어지기 때문에 클라이언트가 다시 요청하면 서버는 이전 요청을 기억하지 못함
    
    (로그인하고 새로운 요청을 보내도 로그인 상태 기억 X)
    
- 이를 해결하기 위해 모든 요청에 정보를 포함해야함 → 쿠키 사용

<br/>
설명

- 사용자 로그인 세션 관리, 광고 정보 트래킹에 사용
- 쿠키 정보는 항상 서버에 전송됨
    - 네트워크 트래픽 추가 유발
    - 따라서 최소한의 정보만 사용 (세션 id, 인증 토큰 등)
    - 서버에 전송하지않고, 웹 브라우저 내부에 데이터를 저장하는 웹 스토리지도 있음
- 보안에 민감한 데이터는 저장 x

<br/>
생명주기

- Set-Cookie: **expires**=Sat, 26-Dec-2020 04:39:21 GMT
    - 만료일이 되면 쿠키 삭제
- Set-Cookie: **max-age**=3600 (3600초)
    - 해당 초가 지나거나 0 또는 음수 지정시 쿠키 삭제
- 세션 쿠키 : 만료 날짜를 생략하면 브라우저 종료시 까지만 유지
- 영속 쿠키 : 만료 날짜를 입력하면 해당 날짜까지 유지

<br/>
쿠키 - 도메인

- domain = example.org
- 명시 : 명시한 문서 기준 도메인 + 서브 도메인 포함
    - [example.org](http://example.org), [dev.example.org](http://dev.example.org) 쿠키 접근
- 생략 : 현재 문서 기준 도메인만 적용
    - example.org에서만 쿠키 접근 가능

<br/>
쿠키 - 경로

- path=/home
- 이 경로를 포함한 하위 경로 페이지만 쿠키 접근
- 일반적으로 path=/루트로 지정

<br/>
쿠키 - 보안

- Secure
    - 쿠키는 http, https 구분하지 않고 전송
    - Secure 적용시 https인 경우에만 전송
- HttpOnly
    - XSS(Cross Site Scripting) 공격 방지 - 게시판이나 웹 메일등에 자바 스크립트 코드를 삽입해 개발자가 고려하지 못한 기능이 작동하게 하는
    - 자바스크립트에서 접근 불가
    - HTTP 전송에만 사용
- SameSite
    - XSRF(Cross Site Request Forgrey) 공격 방지 - 쿠키만으로 인증하는 서비스의 취약점을 이용해, 사용자가 모르게 해당 서비스에 특정 명령을 요청하는 공격 (쿠키를 가지고 다른 페이지에서 요청을 보내는 것)
    - 요청 도메인과 쿠키에 설정된 도메인이 같은 경우에만 쿠키 전송
