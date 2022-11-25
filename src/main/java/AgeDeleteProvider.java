import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AgeDeleteProvider {

    public enum DeleteState{
        NONE,
        WAIT_DELETE_ONE_CONFIRMATION,
        WAIT_DELETE_ALL_CONFIRMATION
    }

    private final Map<String, DeleteState> usrDeleteState;
    private final Map<String, String> userDeleteParam;

    public AgeDeleteProvider() {
        usrDeleteState = new HashMap<>();
        userDeleteParam = new HashMap<>();
    }

    public boolean waitingConfirmation(String userID) {
        DeleteState state = usrDeleteState.get(userID);
        return state == DeleteState.WAIT_DELETE_ONE_CONFIRMATION || state == DeleteState.WAIT_DELETE_ALL_CONFIRMATION;
    }

    public String requestDeletion(String userID, String message) {
        String key;
        int pos = "--config-alias-delete".length();
        StringBuilder sb = new StringBuilder();
        while (pos < message.length()) {
            char c = message.charAt(pos);
            if (c != ' ') {
                sb.append(c);
            }
            pos++;
        }
        key = sb.toString().toLowerCase();

        if (key.equals("-all")) {
            usrDeleteState.put(userID, DeleteState.WAIT_DELETE_ALL_CONFIRMATION);
            return "You are about to delete ALL keys. Confirm? [y/n]";
        }
        else if (!key.isEmpty()) {
            usrDeleteState.put(userID, DeleteState.WAIT_DELETE_ONE_CONFIRMATION);
            userDeleteParam.put(userID, key);
            return "You are about to delete key " + key + ". Confirm? [y/n]";
        }
        else {
            return "Error! Specific key or -all required";
        }
    }

    public String confirmDeletion(String userID, String message) throws IOException {
        AgeTokenProvider atp = new AgeTokenProvider(userID);
        boolean result = false;
        String msg = message.toLowerCase();
        if (msg.equals("y") || msg.equals("yes")) {
            if (usrDeleteState.get(userID) == DeleteState.WAIT_DELETE_ALL_CONFIRMATION) {
                result = atp.deleteAllTokens();
            }
            else if (usrDeleteState.get(userID) == DeleteState.WAIT_DELETE_ONE_CONFIRMATION) {
                result = atp.deleteToken(userDeleteParam.get(userID));
                userDeleteParam.put(userID, "");
            }
        }
        else {
            usrDeleteState.put(userID, DeleteState.NONE);
            return "_Cancelled!_";
        }
        usrDeleteState.put(userID, DeleteState.NONE);
        //TODO: Neither usrDeleteState nor userDeleteParam is not cleared. Here could be potential memory consumption issue while running for a long time and serving a lot of users...
        if (result) {
            return "_Deleted!_";
        }
        else {
            return "_Ну не шмогла я, не шмогла... Нет такого ключа_";
        }
    }
}
