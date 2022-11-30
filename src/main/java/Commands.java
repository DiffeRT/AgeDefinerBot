public class Commands {
    public static final String START = "/start";
    public static final String CONFIG_ALIAS = "-config-alias";
    public static final String CONFIG_ALIAS_SHOW = "-config-alias-show";
    public static final String CONFIG_ALIAS_DELETE = "-config-alias-delete";

    public static boolean isCommand(String command, String message) {
        return message.contains(command);
    }
}
