package tobyspring.helloboot;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 어노테이션을 만들때 2개는 필수다
@Retention(RetentionPolicy.RUNTIME) //실행중, 컴파이일시 사용할것인지 지정
@Target(ElementType.TYPE) // 클래스냐 메소드냐 사용할것인지를 지정
@Component
public @interface MyComponent {
}
