package tobyspring.study;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class ConfigurationTest {

    @Test
    void configuration() {
//        Common common = new Common();
//        Assertions.assertThat(common).isSameAs(common); // success

//        MyConfig myConfig = new MyConfig();
//        Bean1 bean1 = myConfig.bean1();
//        Bean2 bean2 = myConfig.bean2();
//        Assertions.assertThat(bean1.common).isSameAs(bean2.common); // fail


        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(MyConfig.class);
        applicationContext.refresh();

        Bean1 bean1 = applicationContext.getBean(Bean1.class);
        Bean2 bean2 = applicationContext.getBean(Bean2.class);

        Assertions.assertThat(bean1.common).isSameAs(bean2.common); // success
    }

    @Test
    void proxyCommonMethod() {
        MyConfigProxy myConfig = new MyConfigProxy();
        Bean1 bean1 = myConfig.bean1();
        Bean2 bean2 = myConfig.bean2();
        Assertions.assertThat(bean1.common).isSameAs(bean2.common); // success
    }

    static class MyConfigProxy extends MyConfig {

        private Common common;

        @Override
        Common common() {
            if (this.common == null) this.common = super.common();
            return this.common;
        }
    }

    @Configuration
    static class MyConfig {

        @Bean
        Common common() {
            return new Common();
        }

        @Bean
        Bean1 bean1() {
            return new Bean1(common());
        }

        @Bean
        Bean2 bean2() {
            return new Bean2(common());
        }

    }

    static class Bean2 {
        private Common common;

        public Bean2(Common common) {
            this.common = common;
        }
    }

    static class Bean1 {
        private Common common;

        public Bean1(Common common) {
            this.common = common;
        }
    }

    static class Common {

    }
}
