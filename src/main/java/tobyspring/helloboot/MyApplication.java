package tobyspring.helloboot;

import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.web.servlet.DispatcherServlet;

public class MyApplication {

    public static void runSpringAnnotationContext(Class<?> applicationClass, String... args) {

        // @Bean 으로 빈들을 만들어 놓고 @Configuration으로 어떤클래스에서 빈들을 생성할 것인지 지정해 줘야한다.
        AnnotationConfigServletWebApplicationContext applicationContext = new AnnotationConfigServletWebApplicationContext() {
            // refresh()는 템플릿 메서드 패턴으로 되어 있는데 있대 onRefresh가 호출된다.
            @Override
            protected void onRefresh() {
                super.onRefresh();

                ServletWebServerFactory servletWebServerFactory = this.getBean(ServletWebServerFactory.class);
                DispatcherServlet dispatcherServlet = this.getBean(DispatcherServlet.class);

                WebServer webServer = servletWebServerFactory.getWebServer(servletContext -> {
                    //new DispatcherServlet(this) // 인자를 안넘겨도 spring에서 알아서 주입해준다.
                    servletContext.addServlet("dispatchServlet",
                            dispatcherServlet
                    ).addMapping("/*");
                });
                webServer.start();
            }
        };
        applicationContext.register(applicationClass);
        applicationContext.refresh();

    }
}
