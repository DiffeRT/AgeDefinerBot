import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LexemeAnalyser {
//    expression : factor ( ( '+' | '-' ) factor )* EOF;
//    factor     : var | func
//    func       : NAME '(' date (, date) ')'
//    var        : TOKEN -> expression
//    date       : DD.MM.YYYY

    private final String userID;

    public enum LexemeType {
        LEFT_BRACKET, RIGHT_BRACKET,
        OP_PLUS, OP_MINUS,
        DATE, NAME, TOKEN, COMMA,
        EOF
    }

    public static class Lexeme {
        LexemeType type;
        String value;

        public Lexeme(LexemeType type, String value) {
            this.type = type;
            this.value = value;
        }

        public Lexeme(LexemeType type, Character value) {
            this.type = type;
            this.value = value.toString();
        }

        @Override
        public String toString() {
            return "LE {type = " + type + ", value = '" + value + "'}";
        }
    }

    public static class LexemeBuffer {
        public List<Lexeme> lexemes;
        private int pos;

        public LexemeBuffer(List<Lexeme> lexemes) {
            this.lexemes = lexemes;
        }

        public Lexeme next() {
            return lexemes.get(pos++);
        }

        public void back() {
            pos--;
        }

        public int getPos() {
            return pos;
        }
    }

    public LexemeAnalyser(String userID) {
        this.userID = userID;
    }

    public List<Lexeme> lexemeAnalyze(String expText) {
        AgeTokenProvider atp = new AgeTokenProvider(userID);

        ArrayList<Lexeme> lexemes = new ArrayList<>();
        int pos = 0;
        while (pos < expText.length()) {
            char c = expText.charAt(pos);
            switch (c) {
                case '(':
                    lexemes.add(new Lexeme(LexemeType.LEFT_BRACKET, c));
                    pos++;
                    continue;
                case ')':
                    lexemes.add(new Lexeme(LexemeType.RIGHT_BRACKET, c));
                    pos++;
                    continue;
                case '+':
                    lexemes.add(new Lexeme(LexemeType.OP_PLUS, c));
                    pos++;
                    continue;
                case '-':
                    lexemes.add(new Lexeme(LexemeType.OP_MINUS, c));
                    pos++;
                    continue;
                case ',':
                    lexemes.add(new Lexeme(LexemeType.COMMA, c));
                    pos++;
                    continue;
                default:
                    if (c >= '0' && c <= '9' || c == '.') {
                        StringBuilder sb = new StringBuilder();
                        do {
                            sb.append(c);
                            pos++;
                            if (pos >= expText.length()) {
                                break;
                            }
                            c = expText.charAt(pos);
                        } while (c >= '0' && c <= '9' || c == '.');
                        lexemes.add(new Lexeme(LexemeType.DATE, sb.toString()));
                    }
                    else if (c >= 'a' && c <= 'z') {
                        StringBuilder sb = new StringBuilder();
                        boolean isFunc = false;
                        do {
                            sb.append(c);
                            pos++;
                            if (pos >= expText.length()) {
                                break;
                            }
                            c = expText.charAt(pos);
                        } while ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')); //support num in var: m1, t800
                        if (pos < expText.length() && c == '(') {
                            isFunc = true;
                        }

                        if (isFunc && ("age".equals(sb.toString()) || "diff".equals(sb.toString()))) {
                            lexemes.add(new Lexeme(LexemeType.NAME, sb.toString()));
                        }
                        else if (atp.containsKey(sb.toString())) {
                            lexemes.add(new Lexeme(LexemeType.TOKEN, sb.toString()));
                        }
                        else {
                            throw new RuntimeException("Unexpected identifier: " + sb + " {class LexemeAnalyser}");
                        }
                    }
                    else {
                        throw new RuntimeException("Unexpected char: " + c + " {class LexemeAnalyser}");
                    }
            }
        }
        lexemes.add(new Lexeme(LexemeType.EOF, ""));
        return lexemes;
    }

    public AgeDuration expression(LexemeBuffer lexemes) throws ParseException {
        AgeDuration result = factor(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case OP_PLUS:
                    AgeDuration arg = factor(lexemes);
                    result = result.Add(arg);
                    break;
                case OP_MINUS:
                    arg = factor(lexemes);
                    result = result.Sub(arg);
                    break;
                default:
                    lexemes.back();
                    return result;
            }
        }
    }

    public AgeDuration factor(LexemeBuffer lexemes) throws ParseException {
        Lexeme lexeme = lexemes.next();
        AgeDuration result;
        switch (lexeme.type) {
            case NAME:
                lexemes.back();
                result = func(lexemes);
                break;
            case TOKEN:
                lexemes.back();
                result = var(lexemes);
                break;
            default:
                throw new RuntimeException("Unexpected token: " + lexeme.value + " at position: " + lexemes.getPos());
        }
        return result;
    }

    public AgeDuration func(LexemeBuffer lexemes) throws ParseException {
        String name = lexemes.next().value;
        AgeDuration result = null;

        if ("age".equals(name)) {
            Lexeme funcBeg = lexemes.next();
            Lexeme arg = lexemes.next();
            Lexeme funcEnd = lexemes.next();
            if (funcBeg.type != LexemeType.LEFT_BRACKET || arg.type != LexemeType.DATE || funcEnd.type != LexemeType.RIGHT_BRACKET) {
                throw new RuntimeException("Wrong function call syntax at: " + name + funcBeg.value + arg.value + funcEnd.value);
            }
            result = AgeDefiner.Age(date(arg));
        }
        else if ("diff".equals(name)) {
            Lexeme funcBeg = lexemes.next();
            Lexeme arg1 = lexemes.next();
            Lexeme comma = lexemes.next();
            Lexeme arg2 = lexemes.next();
            Lexeme funcEnd = lexemes.next();
            if (funcBeg.type != LexemeType.LEFT_BRACKET || arg1.type != LexemeType.DATE || arg2.type != LexemeType.DATE || comma.type != LexemeType.COMMA || funcEnd.type != LexemeType.RIGHT_BRACKET) {
                throw new RuntimeException("Wrong function call syntax at: " + name + funcBeg.value + arg1.value + comma.value + arg1.value + funcEnd.value);
            }
            result = AgeDefiner.Diff(date(arg1), date(arg2));
        }
        return result;
    }

    public AgeDuration var(LexemeBuffer lexemes) throws ParseException {
        String name = lexemes.next().value;
        AgeDuration result;
        //extract token
        AgeTokenProvider atpVar = new AgeTokenProvider(userID);
        String expToken = atpVar.getTokenExpression(name);
        //lexemeAnalyze(token)
        LexemeAnalyser la = new LexemeAnalyser(userID);
        List<LexemeAnalyser.Lexeme> userDefinedLexemes = la.lexemeAnalyze(expToken);
        LexemeBuffer udfBuffer = new LexemeBuffer(userDefinedLexemes);
        result = expression(udfBuffer);
        return result;
    }

    public static Date date(Lexeme lexeme) throws ParseException {
        if (lexeme.type == LexemeType.DATE) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            sdf.setLenient(false);
            return sdf.parse(lexeme.value);
        }
        else {
            throw new RuntimeException("Date format at: " + lexeme.value);
        }
    }

}
