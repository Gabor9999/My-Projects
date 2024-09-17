package Week2;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostTest {
    Post p;
    @BeforeAll
    void createPost() {
        p = new Post(new PostBox(), new PostBox());
    }

    @Test
    void testPost() {

    }
}