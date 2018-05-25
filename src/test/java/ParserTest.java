import ast.Ast;
import org.junit.jupiter.api.Test;

import static ast.Ast.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {
    @Test
    void testParseSELECTThrow() {
        Parser parser = new Parser(new Tokenizer(new Input("FROM SELECT")));
        assertThrows(CustomException.class, parser::parseQuery);
    }
    @Test
    void testParseFieldSingle() {
        Parser parser = new Parser(new Tokenizer(new Input("AAA")));
        assertEquals(createToken("AAA", null, null), parser.parseToken());
    }

    @Test
    void testParseFieldAlias() {
        Parser parser = new Parser(new Tokenizer(new Input("AAA BBB")));
        assertEquals(createToken("AAA", null, "BBB"), parser.parseToken());
        parser = new Parser(new Tokenizer(new Input("AAA as BBB")));
        assertEquals(createToken("AAA", null, "BBB"), parser.parseToken());
    }

    @Test
    void testParseFieldDot() {
        Parser parser = new Parser(new Tokenizer(new Input("AAA.BBB")));
        assertEquals(createToken("AAA", "BBB", null), parser.parseToken());
    }
    @Test
    void testParseFieldDotAlisa() {
        Parser parser = new Parser(new Tokenizer(new Input("AAA.BBB CCC")));
        assertEquals(createToken("AAA", "BBB", "CCC"), parser.parseToken());
        parser = new Parser(new Tokenizer(new Input("AAA.BBB as CCC")));
        assertEquals(createToken("AAA", "BBB", "CCC"), parser.parseToken());
    }

    @Test
    void testParseSelect() {
        Parser parser = new Parser(new Tokenizer(new Input("select AAA.BBB CCC from DDD")));
        assertEquals(
                createQuery(
                        createSelect(createAstList(createToken("AAA", "BBB", "CCC"))),
                        createFrom(createAstList(createTable(createToken("DDD", null, null),null))),
                        null
                        ),
                parser.parseQuery()
        );
        parser = new Parser(new Tokenizer(new Input("select AAA.BBB CCC,\n DDD,\n AAA.CCC as fff from GGG")));
        assertEquals(
                createQuery(
                        createSelect(createAstList(
                                createToken("AAA", "BBB", "CCC"),
                                createToken("DDD", null, null),
                                createToken("AAA", "CCC", "fff")
                        )),
                        createFrom(createAstList(
                                createTable(createToken("GGG", null, null), null)
                        )),
                        null
                ),
                    parser.parseQuery()
                );
    }

    @Test
    void testParseSubSelect() {
        Parser parser = new Parser(new Tokenizer(new Input("select AAA.BBB CCC, (SELECT B FROM DD) C from DDD")));
        assertEquals(
                createQuery(
                        createSelect(createAstList(
                                createToken("AAA", "BBB", "CCC"),
                                createSubQuery(
                                        createQuery(
                                            createSelect(
                                                createAstList(
                                                createToken("B", null, null)
                                            )
                                        ),
                                                createFrom(createAstList(createTable(createToken("DD", null, null), null))),
                                                null
                                    ),
                                "C"
                                )
                        )),
                        createFrom(createAstList(
                                createTable(
                                        createToken("DDD", null, null),
                                        null
                                )

                        )),
                        null
                )
                ,
                parser.parseQuery()
        );
    }
    
    @Test
    void testFrom1() {
        Parser parser = new Parser(new Tokenizer(new Input("SELECT A FROM B")));
        assertEquals(
                createQuery(
                        createSelect(createAstList(createToken("A", null, null))),
                        createFrom(createAstList(createTable(createToken("B", null, null), null))),
                        null
                ),
            parser.parse()
        );
    }

    @Test
    void testFrom2() {
        Parser parser = new Parser(new Tokenizer(new Input("SELECT A FROM B C")));
        assertEquals(
                createQuery(
                        createSelect(createAstList(createToken("A", null, null))),
                        createFrom(createAstList(createTable(createToken("B", null, "C"), null))),
                        null
                ),
                parser.parse()
        );
    }

    @Test
    void testFrom3() {
        Parser parser = new Parser(new Tokenizer(new Input("SELECT A FROM B C left join D ON D.D = 123")));
        assertEquals(
                createQuery(
                        createSelect(createAstList(createToken("A", null, null))),
                        createFrom(createAstList(createTable(createToken("B", null, "C"), createJoin(createToken("D", null, null), null)))),
                        null
                ),
                parser.parse()
        );
    }

    @Test
    void testParseWhere1() {
        Parser parser = new Parser(new Tokenizer(new Input("D = 123")));
        assertEquals(createCondition(createToken("D", null, null), "=", createAtom(123.0d)),
                parser.parseWhere(parser.parseAtom()));
    }

    @Test
    void testParseWhere2() {
        Parser parser = new Parser(new Tokenizer(new Input("D = 123 and E = '123'")));
        assertEquals(createCondition(createToken("D", null, null), "=", createAtom(123.0d)),
                parser.parseWhere(parser.parseAtom()));
    }

}
