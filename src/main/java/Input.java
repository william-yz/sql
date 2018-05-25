public class Input implements Stream<String> {
    private String raw;
    private int pos = 0;
    private int line = 1;
    private int col = 0;

    public static final String EOL = System.getProperty("line.separator", "\n");

    public Input(String raw) {
        this.raw = raw;
    }

    public String next() {
        if (this.eof()) this.croak("EOF");
        String ch = String.valueOf(this.raw.charAt(pos ++));
        if (Input.EOL.equals(ch)) {
            this.line ++;
            this.col = 0;
        } else {
            this.col ++;
        }
        if ("".equals(ch)) {
        }
        return ch;
    }

    public String peek(int n) {
        if (this.eof(n)) this.croak("EOF");
        String ch = String.valueOf(this.raw.charAt(pos + n));
        return ch;
    }

    public boolean eof(int n) {
        return pos + n >= this.raw.length();
    }
    public void croak(String msg) {
        throw new CustomException(String.format("%s(line: %d, col: %d)", msg, this.line, this.col));
    }
}

