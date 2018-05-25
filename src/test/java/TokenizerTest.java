import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenizerTest {

    @Test
    void testStreamOperator() {
        Tokenizer tokenizer = new Tokenizer(new Input("SELECT A,C FROM B;"));
        assertEquals(Token.createKeyword("SELECT"), tokenizer.next());
        assertEquals(Token.createToken("A"), tokenizer.next());
        assertEquals(Token.createPunctuation(","), tokenizer.peek());
        assertEquals(Token.createPunctuation(","), tokenizer.next());
        assertEquals(Token.createToken("C"), tokenizer.next());
        assertEquals(Token.createToken("B"), tokenizer.peek(1));
        assertEquals(Token.createPunctuation(";"), tokenizer.peek(2));
        assertTrue(tokenizer.eof(3));
        assertEquals(Token.createKeyword("FROM"), tokenizer.next());
        assertEquals(Token.createToken("B"), tokenizer.next());
        assertEquals(Token.createPunctuation(";"), tokenizer.next());
    }

    @Test
    void testToken() {
        Tokenizer tokenizer = new Tokenizer(new Input("AAA"));
        assertEquals(Token.createToken("AAA"), tokenizer.next());
    }

    @Test
    void testDot() {
        Tokenizer tokenizer = new Tokenizer(new Input("AAA.BBB"));
        assertEquals(Token.createToken("AAA"), tokenizer.next());
        assertEquals(Token.createOperator("."), tokenizer.next());
        assertEquals(Token.createToken("BBB"), tokenizer.next());
    }

    @Test
    void testComma() {
        Tokenizer tokenizer = new Tokenizer(new Input("aaa, bbb"));
        assertEquals(Token.createToken("aaa"), tokenizer.next());
        assertEquals(Token.createPunctuation(","), tokenizer.next());
        assertEquals(Token.createToken("bbb"), tokenizer.next());
    }
}
