package tobyspring.helloboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) // 톰캣서버를 미리 띄위지 않아도 자동으로 시작해서 테스트 진행
public class HelloApiTest {

    @Test
    void helloApiTest() {
        TestRestTemplate rest = new TestRestTemplate();

        ResponseEntity<String> res = rest
                .getForEntity("http://localhost:8080/app/hello?name={name}", String.class, "Spring");
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE)).startsWith(MediaType.TEXT_PLAIN_VALUE);
        assertThat(res.getBody()).isEqualTo("*Hello Spring*");
    }


    @Test
    void failHelloApiTest() {
        TestRestTemplate rest = new TestRestTemplate();

        ResponseEntity<String> res = rest
                .getForEntity("http://localhost:8080/app/hello", String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
