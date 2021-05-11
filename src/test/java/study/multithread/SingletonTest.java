package study.multithread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SingletonTest {
    @Test
    @DisplayName("싱글톤 객체 생성 확인")
    void singletonTest() {
        Singleton object1 = Singleton.getInstance();
        Singleton object2 = Singleton.getInstance();

        Assertions.assertTrue(object1 == object2);
    }
}
