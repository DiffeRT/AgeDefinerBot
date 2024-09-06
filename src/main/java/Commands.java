public class Commands {
    public static final String START = "/start";
    public static final String CONFIG_ALIAS = "-config";
    public static final String CONFIG_ALIAS_SHOW = "-show";
    public static final String CONFIG_ALIAS_DELETE = "-delete";

    public static boolean isCommand(String command, String message) {
        return message.contains(command);
    }
}
