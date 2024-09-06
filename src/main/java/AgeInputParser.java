public class AgeInputParser {
    private static boolean isMessage(String msg) {
        //В Маке кавычки какого-то хрена заменяются на открывающие, закрывающие и всякую такую лабудень
        return msg.equals("-m\u201C") || msg.equals("-m\u201D") || msg.equals("-m\"");
    }

    //-m"The message is here" -> The message is here
    private static String getMessage(String msg) {
        if (!msg.isEmpty()) {
            return msg.substring(3, msg.length()-1);
        }
        else {
            return msg;
        }
    }

    public static AgeInputContainer parseInputs(String inputText) {
        AgeInputContainer aiResult = new AgeInputContainer();
        String[] lines = inputText.split("\n");
        for (String line : lines) {
            int pos = 0;
            int posBuff;
            StringBuilder sbExp = new StringBuilder();
            StringBuilder sbMsg = new StringBuilder();
            while (pos < line.length()) {
                char c = line.charAt(pos);
                if (c == '-') {
                    // ? -m" pattern
                    boolean isMSG = true;
                    posBuff = pos;
                    do {
                        sbMsg.append(c);
                        pos++;
                        // Not -m" pattern
                        if ( sbMsg.length() == 3 && !isMessage(sbMsg.toString()) ) {
                            pos = posBuff;
                            isMSG = false;
                            sbMsg.setLength(0);
                            break;
                        }
                        if (pos >= line.length()) {
                            break;
                        }
                        c = line.charAt(pos);
                    } while (true);

                    // Just operand '-'
                    if (!isMSG) {
                        c = line.charAt(pos);
                        sbExp.append(c);
                        pos++;
                    }
                } else if (c == ' ') {
                    // trim " "
                    pos++;
                } else {
                    sbExp.append(c);
                    pos++;
                }
            }
            aiResult.addExpressionWithDescription(sbExp.toString().toLowerCase(), getMessage(sbMsg.toString()));
        }
        return aiResult;
    }
}
