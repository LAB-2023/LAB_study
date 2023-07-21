 ![](https://velog.velcdn.com/images/yseo14/post/c9187669-eb8f-41c8-9cd8-50a20623c458/image.png)

## 동작 순서
1. **핸들러 조회**: 핸들러 매핑을 통해 요청 url에 매핑된 핸들러를 조회한다.
2. **핸들러 어댑터 조회**: 핸들러를 실행할 수 있는 핸들러 어댑터를 조회한다. 
3. **핸들러 어댑터 실행**: 핸들러 어댑터를 실행히함.
4. **핸들러 실행**: 핸들러 어댑터가 실제 핸들러를 실행한다. 
5. **ModelAndView 반환**: 핸들러 어댑터는 핸들러가 반환하는 정보를 ModelView로 변환해서 반환한다.
6. **viewResolver 호출**: 뷰 리졸버를 찾고 실행한다. 
7. **View 반환**: 뷰 립졸버는 뷰의 논리 이름을 물리 이름으로 바꾸고, 렌더링 역할을 담당하는 뷰 객체를 반환한다. 
8. **뷰 렌더링**


# 핸들러 매핑과 핸들러 어댑터

- ## HandlerMapping(핸들러 매핑)
	
    - 핸들러 매핑에서 이 컨트롤러를 찾을 수 있어야함
    	예) 스프링 빈의 이름으로 핸들러를 찾을 수 있는 핸들러 매핑이 필요

- ## HandlerAdapter(핸들러 어댑터)
	
    - 핸들러 매핑을 통해서 찾은 핸들러를 실행할 수 있는 핸들러 어댑터가 필요하다. 
    	예)```Controller``` 인터페이스를 실행할 수 있는 핸들러 어댑터를 찾고 실행해야한다.
        
        
## 스프링 부트가 자동 등록하는 핸들러 매핑과 핸들러 어댑터

### HandlerMapping
```
0 = RequestMappingHandlerMapping 사용 
	: 애노테이션 기반의 컨트롤러인 @RequestMapping에서
    
1 = BeanNameUrlHandlerMapping HandlerAdapter
	: 스프링 빈의 이름으로 핸들러를 찾는다.
```

### HandlerAdapter  

```
0 = RequestMappingHandlerAdapter
	: 애노테이션 기반의 컨트롤러인 @RequestMapping에서
    
1 = HttpRequestHandlerAdapter
	: HttpRequestHandler 처리
    
2 = SimpleControllerHandlerAdapter
	: Controller 인터페이스(애노테이션X, 과거에 사용)
```

# 뷰 리졸버
```
@Component("/springmvc/old-controller")
public class OldController implements Controller {
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("OldController.handleRequest");
        return new ModelAndView("new-form");
    }
}
```

 - ### InternalResourceViewResolver
 : 스프링 부트는 ```InternalResourceViewResolver```라는 뷰 리졸버를 자동으로 등록하는데, 이때 ```application.properties```에 등록한 ```spring.mvc.view.prefix``` , ```spring.mvc.view.suffix```설정 정보를 사용해서 등록함.
```
 spring.mvc.view.prefix=/WEB-INF/views/
 spring.mvc.view.suffix=.jsp
 ```
 
## 스프링 부트가 자동으로 등록하는 뷰 리졸버
```
 1 = BeanNameViewResolver 
 	: 빈 이름으로 뷰를 찾아서 반환한다. (예: 엑셀 파일 생성 기능에 사용)
 2 = InternalResourceViewResolver
 	: JSP를 처리할 수 있는 뷰를 반환한다.
```
 1. 핸들러 어댑터 호출
 	: 핸들러 어댑터를 통해 ```new-form```이라는 논리 뷰 이름을 획득한다. 
 2. ViewResolver 호출
 	- new-form 이라는 뷰 이름으로 viewResolver를 순서대로 호출
    - BeanNameViewResolver는 new-form이라는 스프링 빈으로 등록된 뷰를 찾아야하는데 없다. 
    - InternalResourceViewResolver가 호출된다. 
 3. InternalResourceViewResolver
 	: 이 뷰 리졸버는 InternalResourceView를 반환함
4. 뷰 - InternalResourceView
 InternalResourceView 는 JSP처럼 포워드 forward() 를 호출해서 처리할 수 있는 경우에 사용한다.
5. view.render()
view.render() 가 호출되고 InternalResourceView 는 forward() 를 사용해서 JSP를 실행한다.

# 스프링 MVC

## 회원 등록 폼
```
@Controller
public class SpringMemberFormControllerV1 {

    @RequestMapping("/springmvc/v1/members/new-form")
    public ModelAndView process() {
        return new ModelAndView("new-form");
    }
}
```

- ```@Controller```: 스프링이 자동으로 스프링 빈으로 등록한다. (내부에 ```@Component``` 애노테이션이 있어서 컴포넌트 스캔의 대상이 됨)
- ```@RequestMapping```: 요청 정보를 매핑한다. 해당 URL이 호출되면 이 메서드가 호출된다. 애노테이션을 기반으로 동작하기 때문에, 메서드의 이름은 임의로 지으면 된다.
- ```ModelAndView```: 모델과 뷰 정보를 담아서 반환하면 된다. 

## 회원 저장 
```
@Controller
public class SpringMemberListControllerV1 {
    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/springmvc/v1/members")
    public ModelAndView process() {
        List<Member> members = memberRepository.findAll();

        ModelAndView mv = new ModelAndView("members");
        mv.addObject("members",members);
        return mv;
    }
}
```
- ```mv.addObject("member", member)```: 스프링이 제공하는 ModelAndView를 통해 Model 데이터를 추가할 때는 ```
addObject()```를 사용하면 됨. 이 데이터는 이후 뷰를 렌더링 할 때 사용된다. 

# 스프링 MVC - 컨트롤러 통합

```
@Controller
@RequestMapping("/springmvc/v2/members")
public class SpringMemberControllerV2 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/new-form")
    public ModelAndView newForm() {
        return new ModelAndView("new-form");
    }

    @RequestMapping("save")
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        ModelAndView mv = new ModelAndView("save-result");
        mv.addObject("member", member);
        return mv;
    }

    @RequestMapping
    public ModelAndView members() {
        List<Member> members = memberRepository.findAll();

        ModelAndView mv = new ModelAndView("members");
        mv.addObject("members", members);
        return mv;
    }


}
```
```/springmvc/v2/member``` 이 부분의 중복은 ```
@RequestMapping```을 통해서 제거할 수 있음. 

```
@Controller
@RequestMapping("/springmvc/v2/members")
public class SpringMemberControllerV2
```

## 실용적인 방식

```
@Controller
@RequestMapping("/springmvc/v3/members")
public class SpringMemberControllerV3 {
    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @GetMapping("/new-form")
    public String newForm() {
        return "new-form";
    }

    @PostMapping("save")
    public String save(@RequestParam("username") String username,
                             @RequestParam("age") int age,
                             Model model) {

        Member member = new Member(username, age);
        memberRepository.save(member);

        model.addAttribute("member", member);
        return "save-result";
    }

    @PostMapping
    public String members(Model model) {
        List<Member> members = memberRepository.findAll();

        model.addAttribute("members", members);
        return "members";
    }

}
```
- Model 파라미터
: ```save()```, ```members()```를 보면 Model을 파라미터로 받는 것을 확인할 수 있음. 스프링 MVC도 이러한 기능을 제공함
- ViewName 직접 반환
: 뷰의 논리 이름을 반환할 수 있다. 
- @RequestParam 사용
: 스프링은 HTTP 요청 파라미터를 @RequestParam 으로 받을 수 있다. ```@RequestParam("username")```은 ```request.getParameter("username")``` 와 거의 같은 코드라 생각하면 된다.
- @RequestMapping -> @GetMapping, @PostMapping
: ``` @RequestMapping(value = "/new-form", method = RequestMethod.GET)``` URL 매칭 뿐만 아니라 HTTP Method도 구분할 수 있음. 또한 이것을 ```@GetMapping``` , ```@PostMapping```로 더 편리하게 사용 가능.