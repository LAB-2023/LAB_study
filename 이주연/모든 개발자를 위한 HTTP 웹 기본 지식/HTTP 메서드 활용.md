<div align=center><h3> Section5. HTTP 메서드 활용</h3></div>
<br/>

## 클라이언트에서 서버로 데이터 전송

전달 방식

- 쿼리 파라미터를 통한 데이터 전송
    - GET
    - 정렬 필터(검색어)
- 메시지 바디를 통한 데이터 전송
    - POST, PUT, PATCH
    - 회원 가입, 상품 주문, 리소스 등록, 리소스 변경

<br/>
4가지 상황

- 정적 데이터 조회
- 동적 데이터 조회
- HTML Form을 통한 데이터 전송
- HTTP API를 통한 데이터 전송

<br/>
정적 데이터 조회

- 이미지, 정적 텍스트 문서
- GET 사용
- 일반적으로 쿼리 파라미터 없이 리소스 경로로 단순하게 조회 가능

<img src="https://github.com/LAB-2023/LAB_study/assets/110540359/28fc8805-dc6f-4ab9-ac62-b69d8d4e12b2"/>
<br/>

<br/>
동적 데이터 조회

- 검색, 게시판 목록에서 정렬 필터(검색어)
- 조회 조건 줄여주는 필터, 조회 결과를 정렬하는 정렬 조건에 주로 사용
- GET 사용
- 쿼리 파라미터를 사용해 데이터 전달
<br/>
<img src="https://github.com/LAB-2023/LAB_study/assets/110540359/a897bb8f-4fae-445f-8863-7739bcd4c10c"/>
<br/>

<br/>
HTML Form을 통한 데이터 전송

- GET, POST만 지원 - 주로 POST 사용
- Content-Type: application/x-www-form-urlencoded
    - Form의 내용을 메시지 바디를 통해 전송 (쿼리 파라미터 형식, key=value) → GET 사용시에는 메시지 바디 아닌 URL 쿼리 사용
    - 전송 데이터를 url encoding 처리
    
    <img src="https://github.com/LAB-2023/LAB_study/assets/110540359/94d9069f-dc6c-44fd-a49a-266d7b8f19e1"/>

- Content-Type: multipart/form-data
    - 파일 업로드 같은 바이너리 데이터 전송시 사용
    - 다른 종류의 여러 파일과 폼의 내용 함께 전송 가능
    - boundary로 내용 구분
    
    <img src="https://github.com/LAB-2023/LAB_study/assets/110540359/8fd33bb2-a64d-4df8-9092-d8e2bb89cf2e"/>

<br/>
HTTP API 데이터 전송

- 서버 to 서버, 앱 클라이언트, 웹 클라이언트에서 사용
- POST, PUT, PATCH : 메시지 바디 통해 데이터 전송
- GET : 조회, 쿼리 파라미터로 데이터 전송
- Content-Type: application/json
    - TEXT, XML, JSON 모두 가능하나 json을 거의 표준으로 사

<img src="https://github.com/LAB-2023/LAB_study/assets/110540359/5b057fd3-55df-4809-ba67-d434d7830e1c"/>

## HTTP API 설계


컬렉션

- **POST** 기반 등록
- **서버**가 관리하는 리소스 디렉토리
- 클라이언트는 등록될 리소스의 URI를 모르며, 서버가 새로 등록된 리소스 URI를 생성
- 예시 : 회원 관리

<br/>
스토어

- **PUT** 기반 등록
- **클라이언트**가 관리하는 리소스 저장소
- 클라이언트가 리소스 URI를 알고 있어야 함 (직접 지정)
- 예시 : 파일 관리

<br/>
HTML FORM 사용

- GET과 POST만 지원 → 제약이 있음
- **컨트롤 URI** (컨트롤러)
    - 제약 해결하기 위해 동사로 된 리소스 경로 사용
        
        (원래는 리소스 = 명사, 행위 = 동사가 이상적)
        
    - HTTP 메서드로 해결 어려울 때 사용
- 예시 : 회원관리
    
    <img src="https://github.com/LAB-2023/LAB_study/assets/110540359/a672de48-0c64-4f79-ac14-24585f736114"/>

    - /new, /edit, /delete가 컨트롤 URI
        
        (컬렉션에서는 POST, PATCH, DELETE로 해결 가능)
