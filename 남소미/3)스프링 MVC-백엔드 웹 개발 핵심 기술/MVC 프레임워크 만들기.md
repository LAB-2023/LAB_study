# MVC 프레임워크 만들기

![Untitled](https://github.com/LAB-2023/LAB_study/assets/125250173/bf2f5849-48c2-49c9-beb6-a2d4bd320674)

**FrontController 패턴 특징**

- 프론트 컨트롤러 서블릿 하나로 클라이언트의 요청을 받음
- 프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출

→ **입구를 하나로! → 공통 처리 가능**

- 나머지 컨트롤러는 서블릿을 사용하지 않아도 됨
- 스프링 웹 MVC의 **DispatcherServlet이 FrontController 패턴으로 구현**

---

## V1 : 프론트 컨트롤러를 도입

![Untitled 1](https://github.com/LAB-2023/LAB_study/assets/125250173/f3188bb5-d239-448d-b8bc-586cb77ffc45)

```java
public interface ControllerV1 {
 void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
}
```

**프론트 컨트롤러**

```java
@WebServlet(name="frontControllerServletV1", urlPatterns = "/front-controller/v1/*")
public class FrontControllerServletV1 extends HttpServlet {

    private Map<String, ControllerV1> controllerMap = new HashMap<>();

    public FrontControllerServletV1() {
        controllerMap.put("/front-controller/v1/members/new-form", new MemberFormControllerV1());
        controllerMap.put("/front-controller/v1/members/save", new MemberSaveControllerV1());
        controllerMap.put("/front-controller/v1/members", new MemberListControllerV1());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV1.service");

        String requestURI = request.getRequestURI();

        ControllerV1 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        controller.process(request,response);
    }

}
```

- **urlPatterns**
    
    urlPatterns = "/front-controller/v1/*" 
    
    : /front-controller/v1 를 포함한 하위 모든 요청 이 서블릿에서 받음
    
- **controllerMap**
key: 매핑 URL / value: 호출될 컨트롤러
- **service()**
requestURI 를 조회, 호출할 컨트롤러를 controllerMap 에서 찾음 → controller.process(request, response); 호출해서 해당 컨트롤러 실행

---

## V2 : View 분류

모든 컨트롤러에서 뷰로 이동하는 부분에 중복 발생 해결

![Untitled 2](https://github.com/LAB-2023/LAB_study/assets/125250173/403438fb-f80d-49b1-b49b-83112c8135fd)

**MyView ( 뷰 객체 )**

```java
@WebServlet(name="frontControllerServletV1", urlPatterns = "/front-controller/v1/*")
public class FrontControllerServletV1 extends HttpServlet {

    private Map<String, ControllerV1> controllerMap = new HashMap<>();

    public FrontControllerServletV1() {
        controllerMap.put("/front-controller/v1/members/new-form", new MemberFormControllerV1());
        controllerMap.put("/front-controller/v1/members/save", new MemberSaveControllerV1());
        controllerMap.put("/front-controller/v1/members", new MemberListControllerV1());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV1.service");

        String requestURI = request.getRequestURI();

        ControllerV1 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        controller.process(request,response);
    }

}
```

**컨트롤러 인터페이스**

```java
public interface ControllerV2 {
 MyView process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException;
}
```

→  각 컨트롤러는 dispatcher.forward() 를 직접 생성해서 호출하지 않고 MyView 객체를 생성하고 거기에 뷰 이름만 넣고 반환

EX )

```java
public class MemberSaveControllerV2 implements ControllerV2 {
    private MemberRepository memberRepository = MemberRepository.getInstance();
    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        request.setAttribute("member", member);

        **return new MyView("/WEB-INF/views/save-result.jsp");**
    }
}
```

**프론트 컨트롤러**

```java
@WebServlet(name = "frontControllerServletV2", urlPatterns = "/front-controller/v2/*")
public class FrontControllerServletV2 extends HttpServlet {
    private Map<String, ControllerV2> controllerMap = new HashMap<>();
    public FrontControllerServletV2() {
        controllerMap.put("/front-controller/v2/members/new-form", new MemberFormControllerV2());
        controllerMap.put("/front-controller/v2/members/save", new MemberSaveControllerV2());
        controllerMap.put("/front-controller/v2/members", new MemberListControllerV2());
    }
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        ControllerV2 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        MyView view = controller.process(request, response);
        **view.render(request, response);**
    }
}
```

프론트 컨트롤러는 컨트롤러의 호출 결과로 MyView 를 반환받음

→ view.render() 를 호출 → forward 로직 수행 → JSP 실행

---

## V3 : Model 추가

- **서블릿 종속성 제거**
컨트롤러 입장에서 HttpServletRequest, HttpServletResponse 필요 X
요청 파라미터 정보 → 자바의 Map으로 넘기도록
: 컨트롤러가 서블릿 기술을 전혀 사용하지 않음.
- **뷰 이름 중복 제거**
컨트롤러에서 지정하는 뷰 이름에 중복있음
뷰의 논리 이름을 반환, 실제 물리 위치의 이름은 프론트 컨트롤러에서 처리하도록 단순화
/WEB-INF/views/new-form.jsp → **new-form**
/WEB-INF/views/save-result.jsp → **save-result**
/WEB-INF/views/members.jsp → **members**

![Untitled 3](https://github.com/LAB-2023/LAB_study/assets/125250173/aec33e61-6fa8-41e6-acc9-3273862691c3)

**ModelView**

```java
public class ModelView {
 private String viewName;
 private Map<String, Object> model = new HashMap<>();
 public ModelView(String viewName) {
 this.viewName = viewName;
 }
 public String getViewName() {
 return viewName;
 }
 public void setViewName(String viewName) {
 this.viewName = viewName;
 }
 public Map<String, Object> getModel() {
 return model;
 }
 public void setModel(Map<String, Object> model) {
 this.model = model;
 }
}
```

뷰의 이름과 뷰를 렌더링할 때 필요한 model 객체 가지고 있음

→ map으로 구성, 컨트롤러에서 key, value로 넣어주면 됨

**컨트롤러**

```java
@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {
    private Map<String, ControllerV3> controllerMap = new HashMap<>();
    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        ControllerV3 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, String> paramMap = createParamMap(request);
        ModelView mv = controller.process(paramMap);
        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName);
        view.render(mv.getModel(), request, response);
    }
    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName,
                        request.getParameter(paramName)));
        return paramMap;
    }
    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
```

EX )

map에서 필요한 요청 파라미터 조회 

ModelView 생성할 때 save-result 이라는 view의 논리적인 이름 지정

모델은 단순한 map이므로 모델에 뷰에서 필요한 member 객체 담고 반환

```java
public class MemberSaveControllerV3 implements ControllerV3 {
    private MemberRepository memberRepository = MemberRepository.getInstance();
    @Override
    public ModelView process(Map<String, String> paramMap) {
        String username = **paramMap.get("username");**
        int age = Integer.parseInt(paramMap.get("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        ModelView mv = **new ModelView("save-result");**
        **mv.getModel().put("member", member);**
        return mv;
    }
}
```

**프론트 컨트롤러**

```java
@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {
    private Map<String, ControllerV3> controllerMap = new HashMap<>();
    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        ControllerV3 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, String> paramMap = createParamMap(request);
        ModelView mv = controller.process(paramMap);
        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName);
        view.render(mv.getModel(), request, response);
    }
    private Map<String, String> **createParamMap**(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName,
                        request.getParameter(paramName)));
        return paramMap;
    }
    private MyView **viewResolver**(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
```

- createParamMap()
HttpServletRequest에서 파라미터 정보를 꺼내서 Map으로 변환
    
    → 해당 Map( paramMap )을 컨트롤러 전달 및 호출
    
- 뷰 리졸버
컨트롤러가 반환한 논리 뷰 이름을 실제 물리 뷰 경로로 변경
    
    → 실제 물리 경로가 있는 MyView 객체 반환
    

---

## V4 : 단순하고 실용적인 컨트롤러

개발자 입장에서 항상 ModelView 객체를 생성하고 반환해야 하는 부분 번거로움

![Untitled 4](https://github.com/LAB-2023/LAB_study/assets/125250173/1a8452b8-aaca-4513-866f-bff803647a97)

: 컨트롤러가 ModelView를 반환하지 않고 ViewName만 반환

**컨트롤러**

```java
public interface ControllerV4 {
    /**
     * @param paramMap
     * @param model
     * @return viewName
     */
    String process(Map<String, String> paramMap, Map<String, Object> model);
}
```

model 객체는 파라미터로 전달되기 때문에 그냥 사용, 뷰의 이름만 반환

EX)

모델이 파라미터로 전달되기 때문에, 모델을 직접 생성하지 않아도 됨

뷰의 논리 이름만 반환

```java
public class MemberSaveControllerV4 implements ControllerV4 {
    private MemberRepository memberRepository = MemberRepository.getInstance();
    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        String username = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));
        Member member = new Member(username, age);
        memberRepository.save(member);
        **model.put("member", member);**
        **return "save-result";**
    }
}
```

**프론트 컨트롤러**

```java
@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {
    private Map<String, ControllerV4> controllerMap = new HashMap<>();
    public FrontControllerServletV4() {
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());
    }
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        ControllerV4 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Map<String, String> paramMap = createParamMap(request);
        **Map<String, Object> model = new HashMap<>(); //추가**

        **String viewName = controller.process(paramMap, model);

        MyView view = viewResolver(viewName);**
        view.render(model, request, response);
    }
    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName,
                        request.getParameter(paramName)));
        return paramMap;
    }
    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
```

- 모델 객체 전달
모델 객체를 프론트 컨트롤러에서 생성해서 넘겨줌
    
    컨트롤러에서 모델 객체에 값을 담으면 여기에 그대로 담김
    
- 뷰의 논리 이름을 직접 반환
컨트롤러가 직접 뷰의 논리 이름을 반환, 이 값을 사용해서 실제 물리 뷰를 찾을 수 있음

---

## V5 : 유연한 컨트롤러

![Untitled 5](https://github.com/LAB-2023/LAB_study/assets/125250173/8f291459-91ee-4c23-9953-dd05041a199a)

**어댑터 패턴**

- 프론트 컨트롤러가 다양한 방식의 컨트롤러를 처리할 수 있도록 함
    - **핸들러 어댑터**
    
    : 어댑터를 통해서 다양한 종류의 컨트롤러 호출
    
    - **핸들러**
    
    : 컨트롤러의 이름을 더 넓은 범위인 핸들러로 변경
    
    → 이제 어댑터가 있기 때문에 어떠한 것이든 해당하는 종류의 어댑터만 있으면 처리 가능
    

**핸들러 어댑터**

```java
public interface MyHandlerAdapter {
	boolean supports(Object handler);
	ModelView handle(HttpServletRequest request, HttpServletResponse response,Object handler) throws ServletException, IOException;
}
```

- boolean supports(Object handler)
    - handler = 컨트롤러.
    - 어댑터가 해당 컨트롤러를 처리할 수 있는지 판단하는 메서드
- ModelView handle(HttpServletRequest request, HttpServletResponse response, Objecthandler)
    - 어댑터는 실제 컨트롤러를 호출하고, 그 결과로 ModelView 반환

**ControllerV3 핸들러 어댑터**

```java
public class ControllerV3HandlerAdapter implements MyHandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        **return (handler instanceof ControllerV3);**
    }
    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        **ControllerV3 controller = (ControllerV3) handler;
        Map<String, String> paramMap = createParamMap(request);
        ModelView mv = controller.process(paramMap);
        return mv;**
    }
    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName,
                        request.getParameter(paramName)));
        return paramMap;
    }
}
```

- ControllerV3 을 처리할 수 있는 어댑터를 뜻함
- handler를 컨트롤러 V3로 변환한 다음에 V3 형식에 맞도록 호출→ ControllerV3는 ModelView를 반환하므로 그대로 ModelView를 반환

**프론트 컨트롤러**

컨트롤러(Controller) = 핸들러(Handler)

```java
@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {
    private final Map<String, **Object**> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();
    
		public FrontControllerServletV5() {
        initHandlerMappingMap(); // 핸들러 매핑 초기화
        initHandlerAdapters(); // 어댑터 초기화
    }
    
		private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        //V4 추가
        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());

    }
    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter()); //V4 추가
    }
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object handler = getHandler(request);
        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        MyHandlerAdapter adapter = getHandlerAdapter(handler);

				**// 3.어댑터 호출**
        ModelView mv = adapter.handle(request, response, handler);
        MyView view = viewResolver(mv.getViewName());
        view.render(mv.getModel(), request, response);
    }
    **//1.handleMappingMap에서 URL에 매핑된 핸들러 객체 찾아서 반환**
		**private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }
		// 2.핸들러를 처리할 수 있는 어댑터 조회**
    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        **for (MyHandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }**
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다.handler=" + handler);
    }
    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}

```