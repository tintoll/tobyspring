package tobyspring.helloboot;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelloServiceTest {

    @Test
    void helloServiceTest() {
        SimpleHelloService simpleHelloService = new SimpleHelloService(getHelloRepository());
        String ret = simpleHelloService.sayHello("Test");
        assertThat(ret).isEqualTo("Hello Test");
    }

    private static HelloRepository getHelloRepository() {
        return new HelloRepository() {
            @Override
            public Hello findHello(String name) {
                return null;
            }

            @Override
            public void increaseCount(String name) {

            }
        };
    }

    @Test
    void helloDecoratorTest() {
        HelloDecorator helloDecorator = new HelloDecorator(name -> name);
        String ret = helloDecorator.sayHello("Test");
        assertThat(ret).isEqualTo("*Test*");
    }
}