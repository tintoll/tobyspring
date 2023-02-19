package tobyspring.helloboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class HelloController {
    private final HelloService helloService;
//    private final ApplicationContext applicationContext;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
//        this.applicationContext = applicationContext;
//        System.out.println(applicationContext);
    }

    @GetMapping("/hello")
//    @ResponseBody  // 이걸 안주면 view로 사용할 부분을 찾지 못해 에러가 난다.
    public String hello(String name) {
        return helloService.sayHello(Objects.requireNonNull(name));
    }
}
