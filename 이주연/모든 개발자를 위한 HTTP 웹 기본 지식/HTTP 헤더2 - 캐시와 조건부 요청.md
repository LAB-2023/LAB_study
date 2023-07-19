<div align=center><h3> Section8. HTTP 헤더2 - 캐시와 조건부 요청</h3></div>
<br/>

## 캐시 기본 동작

캐시가 없을 때

- 데이터가 변경되지 않아도 계속 네트워크를 통해 데이터를 다운로드 받아야 함
- 인터넷 네트워크는 매우 느리고 비쌈
- 브라우저 로딩 속도 느려짐
- 느린 사용자 경험

<br/>
캐시 적용

- 캐시 덕분에 캐시 가능 시간동안 네트워크 사용하지 않아도 됨
- 비싼 네트워크 사용량 줄일 수 있음
- 브라우저 로딩 속도가 매우 빠름
- 빠른 사용자 경험
- 캐시 시간이 초과되면?
    - 서버를 통해 데이터를 다시 조회하고 캐시 갱신
    - 네트워크 다운로드 다시 발생
<br/>    

## 검증 헤더와 조건부 요청1


캐시 시간 초과

- 캐시 유효 시간이 초과해서 서버에 다시 요청하는 경우
    1. 서버에서 기존 데이터 변경
    2. 서버에서 기존 데이터 변경 x

<br/>
서버에서 기존 데이터를 변경하지 않는 경우

- 데이터 새로 전송하지 않고 캐시 재사용 가능
- 단 서버 데이터와 클라이언트 데이터가 같다는 사실을 확인할 방법 필요
    
    → 검증 헤더 추가 (수정된 날짜)
    
<br/>
검증 헤더

- 첫번째 요청 이후 Last-Modified 헤더 포함된 응답 받음
- 응답 결과를 캐시에 저장하고 캐시 시간 초과
- 캐시가 가지고 있는 데이터 최종 수정일 포함하여 두번째 요청 전송
- 서버가 가지고 있는 데이터의 최종 수정일과 일치하면 **HTTP 바디 없이 304 Not Modified  + 헤더**만 전송 (작은 전송 크기)
- 클라이언트는 서버가 보낸 응답 헤더 정보로 캐시의 메타 정보 갱신(유효시간 포함)
- 클라이언트는 캐시 데이터 재활용 가능

<br/>

## 검증 헤더와 조건부 요청2

검증 헤더

- 캐시 데이터와 서버 데이터가 같은지 검증하는 데이터
- Last-Modified , ETag

<br/>
조건부 요청 헤더

- 검증 헤더로 조건에 따른 분기
    - If-Modified-Since : Last-Modified 사용
    - If-None-Match : ETag 사용
- 조건 만족하면 200 OK (데이터 바뀜)
- 조건 만족 x 304 Not Modified (데이터 안바뀜)

<br/>
If-Modified-Since , Last-Modified 단점

- 1초 미만 단위로 캐시 조정 불가능
- 날짜 기반 로직
    - 데이터 수정해서 날짜는 달라졌지만 내용은 같은 경우
    - 서버에서 별도로 캐시 로직 관리하고 싶은 경우 (스페이스나 주석등의 변경으로 캐시 유지해도 상관없을때)

<br/>
해결 : ETag, If-None-Match

- ETag(Entity Tag)
- 캐시용 데이터에 임의의 고유한 버전 이름 부여
    - ETag: "v1.0", ETag: "a2jiodwjekjl3”
- 데이터 변경시 이름 변경(Hash 다시 생성)
    - ETag: "aaaaa" -> ETag: "bbbbb”
- ETag 보내서 같으면 유지(304 + 헤더, 바디x), 다르면 다시 받음(바디까지)
- 캐시 제어 로직을 **서버**에서 완전히 관리
<br/>

## 캐시와 조건부 요청 헤더


캐시 제어 헤더

- Cache-Control : 캐시 제어
- Pragma : 캐시 제어 (하위 호환)
- Expires : 캐시 유효 기간 (하위 호환)

<br/>
Cache-Control - 캐시 지시어

- Cache-Control: max-age (캐시 유효 시간, 초 단위)
- Cache-Control: no-cache (데이터 캐시 가능, but 항상 원 서버에 검증 후 사용)
- Cache-Control: no-store (민감한 정보 저장 x)

<br/>
Pragma

- Pragma: no-cache
- HTTP 1.0 하위 호환

<br/>
Expires

- expires: Mon, 01 Jan 1990 00:00:00 GMT
- 캐시 만료일을 정확한 날짜로 지정
- HTTP 1.0 부터 사용
- Cache-Control: max-age 권장
- Cache-Control: max-age와 함께 사용하면 Expires는 무시
<br/>

## 프록시 캐시

원 서버 직접 접근
<br/>
<img src="https://github.com/LAB-2023/LAB_study/assets/110540359/75235f32-0a82-4b54-830a-40b2cf2dd92f"/>

<br/>
프록시 캐시 도입
<br/>
<img src="https://github.com/LAB-2023/LAB_study/assets/110540359/4fc5b756-ada3-4e9b-99c0-652ffce1b2e6"/>

<br/>

- 클라이언트와 서버 사이에 대리로 통신을 수행하는(= 프록시) 서버

<br/>

프록시 서버와 관련된 Cache-Control

- Cache-Control : public (응답 public 캐시에 저장 가능)
- Cache-Control : private (응답 private 캐시에 저장해야함 - 기본값)
- Cache-Control : s-maxage (프록시 캐시에만 저장되는 max-age)
- Age: 60 (오리진 서버에서 응답 후 프록시 캐시 내에 머문 시간 (초) - HTTP 헤더

<br/>

## 캐시 무효화


방법

- Cache-Control: no-cache, no-store, must-revalidate
- Pragma: no-cache (하위 호환 위함)

<br/>
Cache-Control : must-revalidate

- 캐시 만료후 최초 조회시 원 서버에 검증해야함
- 원 서버 접근 실패시 **반드시 오류 발생** - 504 Gateway Timeout
    - no-cache의 경우 에러 대신 오래된 데이터 보여줄 수 있음
- must-revalidate는 캐시 유효 시간이라면 캐시 사용
