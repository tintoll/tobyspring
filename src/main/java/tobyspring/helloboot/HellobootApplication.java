package tobyspring.helloboot;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@ComponentScan // @Component 찾아서 빈으로 등록해준다.
public class HellobootApplication {

//    @Bean
//    HelloController helloController(HelloService helloService) {
//        return new HelloController(helloService);
//    }
//    @Bean
//    HelloService helloService() {
//        return new SimpleHelloService();
//    }
    @Bean
    ServletWebServerFactory servletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }

    @Bean
    DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }


    public static void main(String[] args) {
        // runBasicTomcatServer();

        // 서블릿마다 해당 처리를 하다보니 공통으로 하는 부분이 너무 많아서
        // 앞단에서 FrontController에서 받아서 전달하는 형태로 변경을 하게 되었다.
        // runFrontControllerTomcatServer();

        // spring에서 frontController 역할을 해주는 dispatcherServlet 으로 변경
        // runDispatchServletTomcatServer();

        // spring context와 tomcat 서블릿 설정하는 부분을 하나로 통합
//        runSpringInTomcatServer();

        // 어노테이션 방식의 spring context를 이용
        MyApplication.runSpringAnnotationContext(HellobootApplication.class, args);

    }



    private static void runSpringInTomcatServer() {
        GenericWebApplicationContext applicationContext = new GenericWebApplicationContext() {
            // refresh()는 템플릿 메서드 패턴으로 되어 있는데 있대 onRefresh가 호출된다.
            @Override
            protected void onRefresh() {
                super.onRefresh();

                ServletWebServerFactory servletWebServerFactory = new TomcatServletWebServerFactory();
                WebServer webServer = servletWebServerFactory.getWebServer(servletContext -> {
                    servletContext.addServlet("dispatchServlet",
                            new DispatcherServlet(this)
                    ).addMapping("/*");
                });
                webServer.start();

            }
        };
        applicationContext.registerBean(HelloController.class); // 빈을 등록
        applicationContext.registerBean(SimpleHelloService.class);
        applicationContext.refresh();
    }

    private static void runDispatchServletTomcatServer() {
        // DispatcherServlet은 Web 기능이 포함되어있는 컨텍스트가 필요하가
        GenericWebApplicationContext applicationContext = new GenericWebApplicationContext();
        applicationContext.registerBean(HelloController.class); // 빈을 등록
        applicationContext.registerBean(SimpleHelloService.class);
        applicationContext.refresh();

        ServletWebServerFactory servletWebServerFactory = new TomcatServletWebServerFactory();
        WebServer webServer = servletWebServerFactory.getWebServer(servletContext -> {
            servletContext.addServlet("dispatchServlet",
                    new DispatcherServlet(applicationContext)
            ).addMapping("/*");
        });
        webServer.start();
    }

    private static void runFrontControllerTomcatServer() {
        // spring ioc container 만들어준다.
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        applicationContext.registerBean(HelloController.class); // 빈을 등록
        applicationContext.registerBean(SimpleHelloService.class);
        applicationContext.refresh();

        ServletWebServerFactory servletWebServerFactory = new TomcatServletWebServerFactory();
        WebServer webServer = servletWebServerFactory.getWebServer(servletContext -> {
            servletContext.addServlet("frontcontroller", new HttpServlet() {
                @Override
                protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                    // 인증, 보안, 다국어, 공토 기능 처리

                    // 실제로 frontcontroller 해야하는 부분에서는 매핑을 해주는 부분 데이터를 바인딩해주는 부분들이 필요하다.
                    if (req.getRequestURI().equals("/hello")
                            && req.getMethod().equals(HttpMethod.GET.name())) {
                        String name = req.getParameter("name");

                        // spring container 에서 빈을 가져오기
                        HelloController helloController = applicationContext.getBean(HelloController.class);
                        String ret = helloController.hello(name);

                        resp.setContentType(MediaType.TEXT_PLAIN_VALUE);
                        resp.getWriter().println(ret);
                    } else if (req.getRequestURI().equals("/user")) {
                        //
                    } else {
                        resp.setStatus(HttpStatus.NOT_FOUND.value());
                    }

                }
                // 모든 요청을 다 받는다.
            }).addMapping("/*");
        });
        webServer.start();
    }

    private static void runBasicTomcatServer() {
        // tomcat webserver를 쉽게 만들수 있는 factory 클래스를 이용하여 웹서버를 생성할 수 있다.
        ServletWebServerFactory servletWebServerFactory = new TomcatServletWebServerFactory();

        WebServer webServer = servletWebServerFactory.getWebServer(servletContext -> {
            // 서블릿을 하나 추가하여 준다.
            servletContext.addServlet("hello", new HttpServlet() {
                @Override
                protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                    String name = req.getParameter("name");

                    // Http Response의 구조를 여기에서 설정하여 준다.
                    resp.setStatus(HttpStatus.OK.value());
                    resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
                    resp.getWriter().println("Hello " + name);
                }
                // 만든 서블릿을 매핑하여 준다
            }).addMapping("/hello");
        });
        webServer.start();
    }

}
