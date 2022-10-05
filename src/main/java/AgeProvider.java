import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class AgeProvider {

    public static void doConfigParsing(String userID, String message) throws IOException {
        //"--config-alias"
        int pos = 14;
        StringBuilder sb = new StringBuilder();
        while (pos < message.length()) {
            char c = message.charAt(pos);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                sb.append(c);
            }
            else if (c == ' ' && sb.length() > 0) {
                break;
            }
            pos++;
        }
        String token = sb.toString();

        int beg = message.indexOf("=");
        int end = message.indexOf("\n");
        if (end == -1) {
            end = message.length();
        }
        String configMessage = message.substring(beg+1, end);
        AgeInputContainer aiConfig = AgeInputParser.parseInputs(configMessage);
        AgeTokenProvider atp = new AgeTokenProvider(userID);
        atp.setToken(token, aiConfig.getExpression(0), aiConfig.getDescription(0));
    }

    public static String doParsingAndCalc(String userID, String message) throws ParseException {
        AgeInputContainer aiInputs = AgeInputParser.parseInputs(message);
        AgeOutputContainer aiOutputs = new AgeOutputContainer();
        AgeTokenProvider atp = new AgeTokenProvider(userID);
        String result;

        for (int i = 0; i < aiInputs.getSize(); i++) {
            String exprLine = aiInputs.getExpression(i);
            LexemeAnalyser lexemeAnalyser = new LexemeAnalyser(userID);
            List<LexemeAnalyser.Lexeme> lexemes = lexemeAnalyser.lexemeAnalyze(exprLine);
            LexemeAnalyser.LexemeBuffer lexemeBuffer = new LexemeAnalyser.LexemeBuffer(lexemes);
            String evalResult = lexemeAnalyser.expression(lexemeBuffer).toString();
            String evalDescription = aiInputs.getDescription(i);
            if (evalDescription.isEmpty() && atp.containsKey(aiInputs.getExpression(i))) {
                evalDescription = atp.getTokenDescription(aiInputs.getExpression(i));
            }
            aiOutputs.addResultWithDescription(evalResult, evalDescription);
        }
        result = aiOutputs.toString();
        return result;
    }
}
