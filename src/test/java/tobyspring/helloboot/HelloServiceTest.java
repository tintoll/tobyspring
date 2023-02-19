package tobyspring.helloboot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloServiceTest {

    @Test
    void helloServiceTest() {
        SimpleHelloService simpleHelloService = new SimpleHelloService();
        String ret = simpleHelloService.sayHello("Test");
        Assertions.assertThat(ret).isEqualTo("Hello Test");
    }
}