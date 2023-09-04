# 서블릿, JSP, MVC 패턴

## 서블릿과 JSP의 한계

- 서블릿

: 뷰 화면을 위한 HTML을 만드는 작업이 자바 코드에 섞여 **복잡함**

- JSP

: 뷰를 생성하는 HTML 작업 깔끔, 중간중간 동적으로 변경이 필요한 부분에만 자바 코드 적용
→ JAVA 코드, 데이터를 조회하는 리포지토리 등등 다양한 코드가 모두 JSP에 노출
→ **너무 많은 역할** 담당

- **MVC 패턴의 등장**

: 비즈니스 로직은 서블릿 처럼 다른곳에서 처리

: JSP는 목적에 맞게 HTML로 화면을 그리는 일에 집중

---

## MVC 패턴

- 너무 많은 역할
- 변경의 라이프 사이클
    
    : UI 수정하는 일과 비즈니스 로직 수정하는 일은 각각 다르게 발생할 가능성 매우 높고 대부분 서로에게 영향을 주지 않음
    
    → 유지보수 어려움
    
- 기능 특화
    
    : JSP는 화면을 렌더링 하는데 최적화, 이 부분만 담당하는 것이 효과적
    
- Model View Controller
    
    : 하나의 서블릿이나, JSP로 처리하던 것→ 컨트롤러와 뷰라는 영역으로 나눈 것
    
    ![Untitled](https://github.com/LAB-2023/LAB_study/assets/125250173/fcfd3ec1-2daa-4234-953d-7e1ab46981ed)
    
    - **컨트롤러**
        
        : HTTP 요청을 받아 파라미터를 검증, 비즈니스 로직을 실행
        
        : 뷰에 전달할 결과 데이터를 조회해서 모델에 담음
        
    - **모델**
        
        : 뷰에 출력할 데이터를 담아둠
        
        : 뷰가 필요한 데이터를 모두 모델에 담아서 전달해주는 덕분에 뷰는 비즈니스 로직이나 데이터 접근을 몰라도 되고, 화면을 렌더링 하는 일에 집중 가능
        
    - **뷰**
        
        : 모델에 담겨있는 데이터를 사용, 화면을 그리는 일에 집중
        

---

## MVC 패턴 - 적용

- Model은 HttpServletRequest 객체를 사용
    
    request는 내부에 데이터 저장소를 가지고 있음 → request.setAttribute() , request.getAttribute()를 사용하면 데이터 보관, 조회 가능
    

- dispatcher.forward()
    
    : 다른 서블릿이나 JSP로 이동할 수 있는 기능, 서버 내부에서 다시 호출 발생
    
- /WEB-INF
    
    : 이 경로안에 JSP가 있으면 외부에서 직접 JSP를 호출할 수 없음
    
     ( 항상 컨트롤러를 통해서 JSP를 호출 )
    
- redirect vs forward
    
    : 리다이렉트는 실제 클라이언트에 응답이 나갔다가, 클라이언트가 redirect 경로로 다시 요청 ( 클라이언트가 인지 가능, URL 경로도 변경 )
    
    : 반면에 포워드는 서버 내부에서 일어나는 호출, 클라이언트가 전혀 인지 불가
    

- JSP가 제공하는 taglib기능 사용
: members 리스트에서 member 를 순서대로 꺼내서 item 변수에 담고, 출력하는 과정을 반복
<c:forEach> 이 기능을 사용하려면 다음과 같이 선언
    
    <%@ taglib prefix="c" uri="[http://java.sun.com/jsp/jstl/core](http://java.sun.com/jsp/jstl/core)"%>
    

---

## MVC 패턴 - 한계

**MVC 컨트롤러의 단점**

- **포워드 중복**
: View로 이동하는 코드가 항상 중복 호출
    
    ```java
    RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
    dispatcher.forward(request, response);
    ```
    
- **ViewPath에 중복**
    
    ```java
    String viewPath = "/WEB-INF/views/new-form.jsp";
    ```
    
    - prefix: /WEB-INF/views/
    - suffix: .jsp
    
    : jsp가 아닌 다른 뷰로 변경한다면 전체 코드를 다 변경해야함
    
- **사용하지 않는 코드**
    
    ```java
    String viewPath = "/WEB-INF/views/new-form.jsp";
    ```
    
    : HttpServletRequest , HttpServletResponse 사용 코드는 테스트 케이스 작성 어려움
    
- **공통 처리가 어렵다.**
: 기능이 복잡해질수록 컨트롤러에서 공통으로 처리해야 하는 부분 증가

<aside>
💡 공통 처리가 어렵다는 문제
: 컨트롤러 호출 전에 먼저 공통 기능을 처리해야 함

→ **프론트 컨트롤러(Front Controller)** 패턴을 도입
**(입구를 하나로!)**

</aside>