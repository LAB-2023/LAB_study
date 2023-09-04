# 스프링 MVC - 구조 이해

## 스프링 MVC 전체 구조

![Untitled](https://github.com/LAB-2023/LAB_study/assets/125250173/f38d74e9-f992-4c25-ae5d-72c2655186de)

**DispatcherServlet** 
: 스프링 MVC의 프론트 컨트롤러 = 디스패처 서블릿

**동작 순서**

1. **핸들러 조회:** 핸들러 매핑을 통해 요청 URL에 매핑된 핸들러 조회
2. **핸들러 어댑터 조회:** 핸들러를 실행할 수 있는 핸들러 어댑터를 조회
3. **핸들러 어댑터 실행:** 핸들러 어댑터 실행
4. **핸들러 실행:** 핸들러 어댑터가 실제 핸들러 실행.
5. **ModelAndView 반환:** 핸들러 어댑터는 핸들러가 반환하는 정보를 ModelAndView로 변환해 반환
6. **viewResolver 호출:** 뷰 리졸버를 찾고 실행
7. **View 반환:** 뷰 리졸버는 뷰의 논리 이름을 물리 이름으로 바꾸고, 렌더링 역할 담당하는 뷰 객체 반환
8. **뷰 렌더링:** 뷰를 통해 뷰 렌더링

---

## 핸들러 매핑과 핸들러 어댑터

**HandlerMapping(핸들러 매핑)**

- 핸들러 매핑에서 이 컨트롤러를 찾을 수 있어야 함
    
    예) 스프링 빈의 이름으로 핸들러를 찾을 수 있는 핸들러 매핑 필요
    

**HandlerAdapter(핸들러 어댑터)**

- 핸들러 매핑을 통해서 찾은 핸들러를 실행할 수 있는 핸들러 어댑터가 필요
    
    예) Controller 인터페이스를 실행할 수 있는 핸들러 어댑터 찾고 실행해야 함
    

**스프링 부트가 자동 등록하는 핸들러 매핑과 핸들러 어댑터**

- HandlerMapping
    
    ```java
    0 = RequestMappingHandlerMapping : 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용
    1 = BeanNameUrlHandlerMapping : 스프링 빈의 이름으로 핸들러를 찾는다.
    ```
    
- HandlerAdapter
    
    ```java
    0 = RequestMappingHandlerAdapter : 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용
    1 = HttpRequestHandlerAdapter : HttpRequestHandler 처리
    2 = SimpleControllerHandlerAdapter : Controller 인터페이스(애노테이션X, 과거에 사용) 처리
    ```
    

---

## 뷰 리졸버

**스프링 부트가 자동 등록하는 뷰 리졸버**

```java
0 = RequestMappingHandlerMapping : 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용
1 = BeanNameUrlHandlerMapping : 스프링 빈의 이름으로 핸들러를 찾는다.
```

1. **핸들러 어댑터 호출** 
    
    **:** new-form 이라는 논리 뷰 이름 획득
    
2. **ViewResolver 호출** 
    
    : new-form 이라는 뷰 이름으로 viewResolver를 순서대로 호출
    : BeanNameViewResolver 는 new-form 이라는 이름의 스프링 빈으로 등록된 뷰를 찾아야 하는데 없다.
    InternalResourceViewResolver 가 호출된다.
    
3. **InternalResourceViewResolver
:** 이 뷰 리졸버는 InternalResourceView 반환
4. **뷰 - InternalResourceView
:** InternalResourceView 는 JSP처럼 포워드 forward() 를 호출해서 처리할 수 있는 경우에 사용
5. **view.render()
:** view.render() 가 호출되고 InternalResourceView 는 forward() 를 사용해서 JSP를 실행

---

## 스프링 MVC - 시작하기

**@RequestMapping**

: 애노테이션을 활용한 매우 유연하고, 실용적인 컨트롤러

- RequestMappingHandlerMapping
- RequestMappingHandlerAdapter

**회원 등록**

```java
**@Controller**
public class SpringMemberFormControllerV1 {
    **@RequestMapping**("/springmvc/v1/members/new-form")
    public ModelAndView process() {
        return new **ModelAndView**("new-form");
    }
}
```

- @Controller :
스프링이 자동으로 스프링 빈으로 등록
스프링 MVC에서 애노테이션 기반 컨트롤러로 인식
- @RequestMapping :
    
    요청 정보를 매핑, 해당 URL이 호출되면 이 메서드가 호출
    
    애노테이션을 기반으로 동작, 메서드의 이름은 임의로
    
- ModelAndView :
    
    모델과 뷰 정보를 담아서 반환
    

→  RequestMappingHandlerMapping 은 스프링 빈 중에서 @RequestMapping 또는 @Controller 가 클래스 레벨에 붙어 있는 경우에 매핑 정보로 인식

**회원 저장**

```java
@Controller
public class SpringMemberSaveControllerV1 {
    private MemberRepository memberRepository = MemberRepository.getInstance();
    @RequestMapping("/springmvc/v1/members/save")
    public ModelAndView process(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);
        System.out.println("member = " + member);
        memberRepository.save(member);

        ModelAndView mv = new ModelAndView("save-result");
        **mv.addObject("member", member);**
        return mv;
    }
}
```

- mv.addObject("member", member)
    
    : 스프링이 제공하는 ModelAndView 를 통해 Model 데이터를 추가할 때는 addObject() 사용
    

**회원 목록**

```java
@Controller
public class SpringMemberListControllerV1 {
    private MemberRepository memberRepository = MemberRepository.getInstance();
    @RequestMapping("/springmvc/v1/members")
    public ModelAndView process() {
        List<Member> members = memberRepository.findAll();
        ModelAndView mv = new ModelAndView("members");
        mv.addObject("members", members);
        return mv;
    }
}
```

---

## 스프링 MVC - 컨트롤러 통합

@RequestMapping 클래스 단위가 아니라 메서드 단위에 적용

→ 컨트롤러 클래스를 유연하게 하나로 통합

```java
**@Controller
@RequestMapping("/springmvc/v2/members")**
public class SpringMemberControllerV2 {
    private MemberRepository memberRepository = MemberRepository.getInstance();
    @RequestMapping("/new-form")
    public ModelAndView newForm() {
        return new ModelAndView("new-form");
    }
    @RequestMapping("/save")
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        ModelAndView mav = new ModelAndView("save-result");
        mav.addObject("member", member);
        return mav;
    }
    @RequestMapping
    public ModelAndView members() {
        List<Member> members = memberRepository.findAll();

        ModelAndView mav = new ModelAndView("members");
        mav.addObject("members", members);
        return mav;
    }
}
```

/springmvc/v2/members 부분 중복 제거 ?

→ 클래스 레벨에 다음과 같이 @RequestMapping 을 두면 메서드 레벨과 조합

```java
@Controller
@RequestMapping("/springmvc/v2/members")
public class SpringMemberControllerV2 {}
```

---

## 스프링 MVC - 실용적인 방식

```java
/**
 * v3
 * Model 도입
 * ViewName 직접 반환
 * @RequestParam 사용
 * @RequestMapping -> @GetMapping, @PostMapping
 */
@Controller
@RequestMapping("/springmvc/v3/members")
public class SpringMemberControllerV3 {
    private MemberRepository memberRepository = MemberRepository.getInstance();
    @GetMapping("/new-form")
    public String newForm() {
        return "new-form";
    }
    @PostMapping("/save")
    public String save(
            @RequestParam("username") String username,
            @RequestParam("age") int age,
            Model model) {

        Member member = new Member(username, age);
        memberRepository.save(member);

        model.addAttribute("member", member);
        return "save-result";
    }
    @GetMapping
    public String members(Model model) {
        List<Member> members = memberRepository.findAll();
        model.addAttribute("members", members);
        return "members";
    }
}
```

- **Model 파라미터**
: save() , members() 를 보면 Model을 파라미터로 받음
- **ViewName 직접 반환**
: 뷰의 논리 이름을 반환
- **@RequestParam 사용**
: 스프링은 HTTP 요청 파라미터를 @RequestParam 으로 받을 수 있음
: @RequestParam("username") == request.getParameter("username")
- **@RequestMapping → @GetMapping, @PostMapping
:** @RequestMapping 은 URL만 매칭하는 것이 아니라, HTTP Method도 함께 구분
EX) URL이 /new-form 이고, HTTP Method가 GET인 경우를 모두 만족하는 매핑
    
    ```java
    @RequestMapping(value = "/new-form", method = RequestMethod.GET)
    ```
    
    —> @GetMapping , @PostMapping 으로 더 편하게 사용할 수 있음