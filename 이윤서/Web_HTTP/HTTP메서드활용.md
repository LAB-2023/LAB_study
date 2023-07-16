>
# 클라이언트에서 서버로 데이터 전송
- ## 쿼리 파라미터 사용	
    - GET
    - 주로 정렬 필터  
- ## 메시지 바디를 통한 데이터 전송
    - POST, PUT, PATCH
    - 회원가입, 상품 주문, 리소스 등록, 리소스 변경

## 4가지의 상황
### 1. 정적 데이터 조회
#### 쿼리 파라미터 미사용
-	이미지, 정적 텍스트 문서
- 조회는 GET 사용
- 정적 데이터는 일반적으로 쿼리 파라미터 없이 리소스 경로로 단순하게 조회 가능
![](https://velog.velcdn.com/images/yseo14/post/94c3ecd1-d798-4059-859f-b2ff89e53540/image.png)

### 2. 동적 데이터 조회
#### 쿼리 파라미터 사용
- 주로 검색, 게시판 목록에서 정렬 필터
- 조회 조건을 줄여주는 필터, 조회 결과를 정렬하는 정렬 조건에 주로 사용
- 조회는 GET 사용
- GET은 쿼리 파라미터 사용해서 데이터 전달
![](https://velog.velcdn.com/images/yseo14/post/124555c8-2667-4958-a883-71e5117e1055/image.png)

### 3. HTML Form 데이터 전송
#### POST 전송 - 저장
![](https://velog.velcdn.com/images/yseo14/post/a1f6993e-6904-4a52-90f4-be32c9377e64/image.png): POST면 메시지 바디에 Form의 내용이 들어감. 
![](https://velog.velcdn.com/images/yseo14/post/09aebe54-f65c-49f7-a670-779bd85ab8fc/image.png): GET이면 url 경로에 쿼리문처럼 넣음. GET메서드는 리소스 변경이 발생하는 요청에는 사용하지 말 것. 아래 그림(조회)처럼 사용.
#### GET 전송 - 조회
![](https://velog.velcdn.com/images/yseo14/post/14277d13-fe90-48ae-8ad7-6cfc8807d249/image.png)
#### multipart/form-data
![](https://velog.velcdn.com/images/yseo14/post/7e01e67d-aa64-4761-9a94-551a18da190c/image.png): 이름, 나이, 파일 등 여러 개의 파트로 나누어 전송
. 주로 바이너리 데이터를 전송 시 사용

- HTML From submit시 POST 전송
- Content-Type: application/x-www-form-urlencoded 사용
	
    - form의 내용을 메시지 바디를 통해서 전송(key=value, 쿼리 파라미터 형식)
    - 전송 데이터를 url encoding 처리
    예) 김 -> abc%EA%B9%80
- Content-Type: multipart/form-data
	
    - 파일 업로드 같은 바이너리 데이터 전송시 사용
    
### 4. HTML API 전송
![](https://velog.velcdn.com/images/yseo14/post/8bd94ead-d846-4a9d-a7b7-779d13ba59a8/image.png)

- 서버 to 서버
- 앱 클라이언트
- 웹 클라이언트
- POST, PT, PATCH: 메시지 바디를 통해 데이터 전송
- GET: 조회,쿼리 파라미터로 데이터 전달
- Content-Type: application/json을 주로 사용 (사실상 표준)

# HTTP API 설계 예시

## 회원 관리 시스템
: POST 기반 등록
- 클라이언트는 등록될 리소스의 URI를 모름
- 서버가 새로 등록된 리소스 URI를 생성해줌
- 컬렉션(Collection)
	
    - 서버가 관리하는 리소스 디렉토리
    - 서버가 리소스의 URI를 생성하고 관리
    - 예) /members
## 파일 관리 시스템
: PUT 기반 등록
- 클라이언트가 리소스 URI를 알고 있어야함
- 클라이언트가 직접 리소스의 URI를 지정한다.
- 스토어(Store)
	
    - 라이언트가 관리하는 리소스 저장소
    - 클라이언트가 리소스의 URI를 알고 관리
    - 예) /files
    
## HTML FORM 사용
- HTML FORM은 GET, POST만 지원
- 컨트롤 URI
	
    - GET, POST만 지원하므로 제약이 있음
    - 이 제약을 해결하기 위해 동사로 된 리소스 경로 사용
    - POST의 /new, /edit, /delete가 컨트롤 URI 