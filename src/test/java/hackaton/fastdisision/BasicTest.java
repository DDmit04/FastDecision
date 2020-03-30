package hackaton.fastdisision;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Superclass for all test classes
 * @author Dmitrochencov Daniil
 * @version 1.0
 */
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@RunWith(SpringRunner.class)
@SpringBootTest
public class BasicTest {

    @Test
    public void basicTest() {
        System.out.println("This is basic test class message!");
    }

}
