# 프론트 컨트롤러 패턴

![](https://velog.velcdn.com/images/yseo14/post/c1bb8e52-81af-4d58-b28e-a2e6b741c21c/image.png)


>
특징
- 프론트 컨트롤러 서블릿 하나로 클라이언트의 요청을 받음
- 프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출
- 공통 처리 가능
- 프론트 컨트롤러를 제외한 나머지 컨트롤러는 서블릿을 사용하지 않아도 됨

# V1
## 컨트롤러 인터페이스 도입

![](https://velog.velcdn.com/images/yseo14/post/8bb34c68-0fa0-4b69-807b-9fa8334bd353/image.png)

```
public interface ControllerV1 {

    void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
```

## 프론트 컨트롤러
```
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
urlPatterns
-  ```urlPatterns = "/front-controller/v1/*"``` : ```/front-controller/v1``` 를 포함한 하위 모든 요청은 이 서블릿에서 받아들인다.
 
controllerMap
- key : url 매핑
- value : 호출될 컨트롤러

sevice
- 먼저 ```requestURI``` 를 조회해서 실제 호출할 컨트롤러를 ```controllerMap``` 에서 찾는다. 만약 없다면 404(SC_NOT_FOUND) 상태 코드를 반환한다.
컨트롤러를 찾고 ```controller.process(request, response);``` 을 호출해서 해당 컨트롤러를 실행한다.

---
# V2 - View 분리
```
String viewPath = "/WEB-INF/views/save-result.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
```
V1 또한 모든 컨트롤러에서 뷰로 이동하는 부분에 중복이 발생. 
이 부분을 객체로 만들어서 별도로 처리하자. 

![](https://velog.velcdn.com/images/yseo14/post/ab0deab9-1ea4-4aff-aac4-bfb34ac41c84/image.png)

## MyView

```
public class MyView {
    private String viewPath;

    public MyView(String viewPath) {
        this.viewPath = viewPath;
    }

    public void render(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request,response);
    }
}
```

## V2 컨트롤러 인터페이스
```
public interface ControllerV2 {

    MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}

```

## MyView 객체를 이용한 컨트롤러 예시 - 회원 등록 폼
```
public class MemberFormControllerV2 implements ControllerV2 {

    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return new MyView("/WEB-INF/views/new-form.jsp");
    }
}
```

---
## V2 프론트 컨트롤러
```
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
        view.render(request, response);
    }
}
```
컨트롤러 V2의 반환 타입이 ```MyView```이므로 프론트 컨트롤러는 컨트롤러의 호출 결과로 ```MyView```를 반환 받음. ```view.render()```를 호출하면 ```forward``` 로직을 수행하여 JSP가 실행됨.

---
# V3 - 모델 추가

![](https://velog.velcdn.com/images/yseo14/post/16b7eed2-1ced-4f83-be2e-085ae8645acf/image.png)

1. 서블릿 종속성 제거
	컨트롤러 입장에서  HttpServletRequest, HttpServletResponse가 필요 없음. 요청 파라미터 정보는 자바의 Map으로 대신 넘기도록 하면 컨트롤러가 서블릿 기술을 몰라도 작동 가능. 
2. 뷰 이름 종속성 제거
	컨트롤러는 뷰의 논리 이름을 반환하고, 실제 물리 위치의 이름은 프론트 컨트롤러에서 처리하도록 단순화. 
   
---
## ModelView
지금까지는 컨트롤러에서 서블릿에 종속적인 HttpServletRequest를 사용했음. Model 또한 ```request.setAttribute()```을 통해 데이터를 저장하고 뷰에 전달했음. 서블릿 종속성을 제거하기 위해 Model을 직접 만들고, 추가로 View 이름까지 전달하는 객체를 만들자. 

```
public class
ModelView {
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
뷰 이름과  뷰를 렌더링할 때 필요한 model 객체를 가지고 있음. 

## ControllerV3 인터페이스
```
public interface ControllerV3 {
    ModelView process(Map<String, String> paraMap);
}
```
서블릿 기술을 전혀 사용하지 않기 때문에 구현이 단순해지고 테스트 코드 작성이 쉬워짐.
HttpServletRequest가 제공하는 파라미터는 프론트 컨트롤러가 paramMap에 담아서 호출하면 됨. 응답 결과로 뷰 이름과 뷰에 전달할 모델 데이터를 포함하는 ModelView 객체를 반환하면 됨.

## MemberFormControllerV3 - 회원 등록 폼

```
public class MemberFormControllerV3 implements ControllerV3 {
    @Override
    public ModelView process(Map<String, String> paraMap) {
        return new ModelView("new-form");
    }
}
```
ModelView를 생성할 때 ```new-form```이라는 뷰의 논리적인 이름을 지정. 실제 물리적인 이름은 프론트 컨트롤러에서 처리함.

## MemberSaveControllerV3 - 회원 저장
```
public class MemberSaveControllerV3 implements ControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {
        String username = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        ModelView mv = new ModelView("save-result");
        mv.getModel().put("member", member);
        return mv;
    }
}
```

```paramMap.get("username");```	: 파라미터 정보는 map에 담겨있음. map에서 필요한 요청 파라미터를 조회하면 된다. 
```mv.getModel().put("member", member);```: 모델은 단순한 map이므로 모델에 뷰에서 필요한 member 객체를 담고 반환한다.

## FrontControllerServletV3
```
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
```createParamMap()```: HttpServletRequest에서 파라미터 정보를 꺼내서 Map으로 변환한다. 그리고 해당 Map을 컨트롤러에 전달하면서 호출함. 

### 뷰 리졸버
```MyView view = viewResolver(viewName)```

컨트롤러가 반환한 논리 뷰 이름을 실제 물리 뷰 경로로 변경. 그리고 실제 물리 경로가 있는 MyView 객체를 반환한다. 

```view.render(mv.getModel(), request, response)```
뷰를 통해 HTML화면을 렌더링. 

# V4 단순하고 실용적인 컨트롤러
![](https://velog.velcdn.com/images/yseo14/post/ef0b9c1c-98c0-46c7-9af1-c79829ef9a38/image.png)
컨트롤러가 ModelView를 반환하지 않고 ViewName만 반환한다. 
## ControllerV4 인터페이스
```
public interface ControllerV4 {
    /**
     *
     * @param paramMap
     * @param model
     * @return viewName
     */
    String process(Map<String, String> paramMap, Map<String, Object> model);
}
```
이번 버전은 인터페이스에 ModelView가 없어서 뷰 이름만 반환해줌

## FrontControllerServletV4

```
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
        Map<String, Object> model = new HashMap<>();

        String viewName = controller.process(paramMap, model);

        MyView view = viewResolver(viewName);
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
---

# V5 - 유연한 컨트롤러
![](https://velog.velcdn.com/images/yseo14/post/363ecf06-5ee9-4371-9a98-2c4ee19b9ea2/image.png)
- 어댑터 도입
- 어댑터를 통해 프레임워크를 유연하고 확장성 있게 설계