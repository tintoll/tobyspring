package tobyspring.helloboot;

import org.springframework.stereotype.Repository;


public interface HelloRepository {
    Hello findHello(String name);
    void increaseCount(String name);

    default int countOf(String name) {
        Hello hello = findHello(name);
        return hello == null ? 0 : hello.getCount();
    }
}
