import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class AgeProvider {

    public static void doConfigParsing(String userID, String message) throws IOException {
        int pos = Commands.CONFIG_ALIAS.length() + 1;
        StringBuilder sb = new StringBuilder();
        while (pos < message.length()) {
            char c = message.charAt(pos);
            //support num in var: m1, t800
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
                sb.append(c);
            }
            else if ((c == ' ' || c == '=') && sb.length() > 0) {
                break;
            }
            pos++;
        }
        String token = sb.toString();

        int beg = message.indexOf("=");
        if (beg == -1) {
            throw new RuntimeException("Invalid syntax at " + pos + ". '=' is not found");
        }

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

    public static String doShowUserData(String userID, String message) {
        AgeTokenProvider atp = new AgeTokenProvider(userID);
        String key;
        int pos = Commands.CONFIG_ALIAS_SHOW.length() + 1;
        StringBuilder sb = new StringBuilder();
        while (pos < message.length()) {
            char c = message.charAt(pos);
            if (c != ' ') {
                sb.append(c);
            }
            pos++;
        }
        key = sb.toString().toLowerCase();

        if (key.isEmpty() || atp.containsKey(key)) {
            String result = atp.showTokens(key);
            if (result.isEmpty()) {
                throw new RuntimeException("Ну не шмогла я, не шмогла... Там ничего нет. Сначала сохраните что-то используя команду:\n" + Commands.CONFIG_ALIAS + " <Name> = <Expression> m\"Description\"");
            }
            return result;
        }
        else {
            throw new RuntimeException("Unknown token: " + key);
        }
    }

    public static String startCommandReply() {
        String neoAge = "???";
        try {
            neoAge = AgeDefiner.Age(new SimpleDateFormat("dd.MM.yyyy").parse("02.09.1964")).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "1) Переменные задаются так\n" +
                "_" + Commands.CONFIG_ALIAS + " Neo = Age(02.09.1964) -m\"Возраст Киану Ривза\"_\n" +
                "_" + Commands.CONFIG_ALIAS + " T800 = Age(30.07.1947) -m\"Возраст Арнольда\"_\n" +
                "_" + Commands.CONFIG_ALIAS + " aDiff = Diff(02.09.1964, 30.07.1947) -m\"Разница между Арнольдом и Киану\"_\n" +
                "*Neo* - это переменная (только символы 'a..Z' и цифры)\n" +
                "*Age(02.09.1964)* - функция (формат даты только такой как тут)\n" +
                "*-m\"Возраст Киану Ривза\"* - описание (обязательно в кавычках)\n" +
                "\n" +
                "2) Можно вызывать переменные или функции\n" +
                "_> Neo_\n" +
                "Возраст Киану Ривза:\n" +
                "  " + neoAge + "\n" +
                "\n" +
                "_> aDiff_\n" +
                "Разница между Шварцом и Киану:\n" +
                "  17 years 1 month 3 days\n" +
                "\n" +
                "_> Diff(31.03.1999, 02.09.1964) -m\"Возраст Киану на момент релиза Матрицы\"_\n" +
                "Возраст Киану на момент релиза Матрицы:\n" +
                "    34 years 6 months 29 days\n" +
                "\n" +
                "3) Можно складывать и вычитать. Поддерживается многострочность\n" +
                "_> Diff(11.11.1918, 28.07.1914) + Diff(02.09.1945, 01.09.1939) -m\"Длительность мировых войн\"_\n" +
                "_> T800 - Neo -m\"Разница между Арнольдом и Киану\"_\n" +
                "Длительность мировых войн\n" +
                "  10 years 3 months 15 days\n" +
                "Разница между Арнольдом и Киану:\n" +
                "  17 years 1 months 3 days\n" +
                "\n" +
                "4) Дополнительные возможности\n" +
                "_" + Commands.CONFIG_ALIAS_SHOW + " Neo_ - показать ключ Neo\n" +
                "_" + Commands.CONFIG_ALIAS_SHOW + "_ - показать все ключи\n" +
                "_" + Commands.CONFIG_ALIAS_DELETE + " aDiff_ - удалить ключ aDiff\n" +
                "_" + Commands.CONFIG_ALIAS_DELETE + " -all_ - удалить все ключи\n" +
                "\n" +
                "_P.S. Бот 'клал' такси Bolt на GDPR. Он собирает и использует все данные что Вы ему отдаете_";
    }

}
