>
# 상태 코드란?
## : 클라이언트가 보낸 요청의 처리 상태를 응답에서 알려주는 기능
- 1xx (Informational): 요청이 수신되어 처리중
- 2xx (Successful): 요청 정상 처리
- 3xx (Redirection): 요청을 완료하려면 추가 행동이 필요
- 4xx (Client Error): 클라이언트 오류, 잘못된 문법등으로 서버가 요청을 수행할 수 없음 5xx (Server Error): 서버 오류, 서버가 정상 요청을 처리하지 못함

## 2xx (Successful)
: 클라이언트의 요청을 성공적으로 처리
 - 200 OK
 - 201 Created
 - 202 Accepted
 - 204 No Content
 
## 3xx (Redirection)
: 요청을 완료하기 위해 유저 에이전트의 추가 조치 필요

- 300 Multiple Choices 
- 301 Moved Permanently 
- 302 Found
- 303 See Other
- 304 Not Modified
- 307 Temporary Redirect 
- 308 Permanent Redirect

![](https://velog.velcdn.com/images/yseo14/post/bef16feb-9a70-4b1e-ad1a-f6b594afeaca/image.png)

### 리다이렉션 종류
- 영구 리다이렉션 - 특정 리소스의 URI가 영구적으로 이동
	
    - 원래의 URL를 사용 x, 검색 엔진 등에서도 변경 인지
    - 
    301 Moved Permanently: 리다이렉트 시 요청 메서드가 GET으로 변하고 본문이 제거될 수 있음  
    308 Permanent Redirect: 리다이렉트 시 요청 메서드와 본문 유지
- 일시 리다이렉션 
	
    -  리소스의 URI가 일시적으로 변경
    - 검색 엔진 등에서 URL을 변경하면 안됨
    - 302 Found: 리다이렉트시 요청 메서드가 GET으로 변하고, 본문이 제거될 수 있음(MAY)
    - 307 Temporary Redirect: 리다이렉트시 요청 메서드와 본문 유지(요청 메서드를 변경하면 안된다. MUST NOT)
    - 303 See Other: 리다이렉트시 요청 메서드가 GET으로 변경  
- 기타 리다이렉션
	
    - 300 Multiple Choices: 안씀
    - 304 Not Modified:
    	캐시를 목적으로 사용
        
 ## 4xx (Client Error)
 - 클라이언트의 요청에 잘못된 문법 등으로 서버가 요청을 수행할 수 없음
 - 오류의 원인이 클라이언트에 있음
 - 클라이언트가 이미 잘못된 요청, 데이터를 보내고 있기 때문에 똑같은 재시도가 실패함
 
 400 Bad Request: 클라이언트가 잘못된 요청을 해서 서버가 요청을 처리할 수 없음
 401 Unauthorized: 클라이언트가 해당 리소스에 대한 인증이 필요함
 403 Forbidden: 서버가 요청을 이해했지만 승인을 거부함
 404 Not Found: 요청 리소스를 찾을 수 없음 
 
 ## 5xx (Server Error)
: 서버 문제로 오류 발생, 서버에 문제가 있기 때문에 재시도하면 성공할 수도 있음

- 500 Internal Server Error: 서버 내부 문제로 오류 발생, 애매하면 500 오류
- 503 Service Unavailable: 서버가 일시적인 과부화 또는 예정된 작업으로 잠시 요청을 처리할 수 없음
 