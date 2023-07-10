<div align=center><h3> Section2. URI와 웹 브라우저 요청 흐름</h3></div>
<br/>

## URI


의미

- URI : Uniform Resource Identifier
    - Uniform : 리소스 식별하는 통일된 방식
    - Resource : 자원, URI로 식별할 수 있는 모든 것
    - Identifier : 다른 항목과 구분하는데 필요한 정보
- URI = URL(Locator) + URN(Name)
  
    <img src="https://github.com/LAB-2023/LAB_study/assets/110540359/211ce712-4b4a-41da-973c-fa3bada91024"/>

    - URL : 리소스가 있는 위치를 지정 - 거의 URL만 사용
    - URN : 리소스에 이름을 부여

<br/>

## URL 분석


예시

- 실제 URL - https://www.google.com:443/search?q=hello&hl=ko
- 문법 - scheme://[userinfo@]host[:port][/path][?query][#fragment]
    - 프로토콜(https)
    - 호스트명(www.google.com)
    - 포트번호(443)
    - 패스(/search)
    - 쿼리 파라미터(?q=hello&hl=ko)

<br/>
URL scheme

- 주로 프로토콜 사용
- 프로토콜 : 어떤 방식으로 자원에 접근할 것인가 하는 약속 규칙
    
    (http, https, ftp 등)
    
<br/>
URL userinfo

- URL에 사용자 정보를 포함해서 인증
- 거의 사용 x

<br/>
URL host

- 호스트명
- 도메인명 or IP 주소 사용 가능

<br/>
URL port

- 접속 포트 번호
- 일반적으로 생략

<br/>
URL path

- 리로스 경로(path)
- 계층적 구조
- 예시
    - /home/file1.jpg
<br/>
URL query

- key=value 형태
- ?로 시작, &로 추가
    - ?keyA=valueA&keyB=valueB
- query parameter, query string등으로 불림
- 웹 서버에 제공

<br/>
URL fragment

- html 내부 북마크 등에 사용
- 서버에 전송 x

<br/>

## 웹 브라우저 요청 흐름

과정

- URL을 통해 웹 브라우저가 HTTP 요청 메시지 생성
  <br/>
    <img src="https://github.com/LAB-2023/LAB_study/assets/110540359/4b454dec-8d06-4f5a-b25c-49c88dbb9237"/>
    
- TCP/IP 패킷에 HTTP 메시지 포함해 HTTP 메시지 전송
  <br/>
    <img src="https://github.com/LAB-2023/LAB_study/assets/110540359/1b739e7b-21ae-4667-857d-92d4d4a779a6"/>
    
- 서버에서 응답 메시지 전송
  <br/>
    <img src="https://github.com/LAB-2023/LAB_study/assets/110540359/cdc63d9b-556d-49de-8e80-49837973457d"/>
    
- 웹 브라우저에서 응답 메시지로 받은 HTML 렌더링
