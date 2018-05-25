import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InputTest {

    @Test
    void nextTest() {
        Input input = new Input("abcd\nef");
        assertEquals("a", input.next());
        assertEquals("b", input.next());
        assertEquals("c", input.next());
        assertEquals("d", input.next());
        assertEquals("\n", input.peek());
        assertEquals("e", input.peek(1));
        assertEquals("\n", input.next());
        assertEquals("e", input.next());
        assertThrows(CustomException.class, () -> input.peek(1));
        assertEquals("f", input.next());
        assertThrows(CustomException.class, () -> input.peek());
        assertThrows(CustomException.class, () -> input.next());
        assertTrue(input.eof());
    }
}
