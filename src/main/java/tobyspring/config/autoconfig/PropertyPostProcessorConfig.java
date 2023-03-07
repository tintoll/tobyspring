package tobyspring.config.autoconfig;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import tobyspring.config.MyAutoConfiguration;
import tobyspring.config.MyConfigurationProperty;

import java.util.Map;

@MyAutoConfiguration
public class PropertyPostProcessorConfig {

    @Bean
    BeanPostProcessor propertyPostProcessor(Environment environment) {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

                // annotation 찾기
                MyConfigurationProperty annotation = AnnotationUtils.findAnnotation(bean.getClass(), MyConfigurationProperty.class);
                if (annotation == null) return bean;
                // annotation의 속성 가져오기
                Map<String, Object> annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotation);
                String prefix = (String)annotationAttributes.get("prefix");

                return Binder.get(environment).bindOrCreate(prefix, bean.getClass());
            }
        };
    }
}
