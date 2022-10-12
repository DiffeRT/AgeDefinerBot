import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;

public class AgeDefinerBot extends TelegramLongPollingBot {

    private final String BOT_USER_NAME;
    private final String BOT_TOKEN;
    private final AgeDeleteProvider deleteProvider;

    public AgeDefinerBot() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("Age.config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BOT_USER_NAME = props.getProperty("BOT_USER_NAME");
        BOT_TOKEN = props.getProperty("BOT_TOKEN");
        deleteProvider = new AgeDeleteProvider();
    }

    @Override
    public String getBotUsername() {
        return BOT_USER_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        String chatID = update.getMessage().getChatId().toString();
        String userID = update.getMessage().getFrom().getId().toString();
        String reply;

        if (message.equals("/start")) {
            reply = AgeProvider.startCommandReply();
            sendMessage(chatID, reply, true);
        }
        else if (message.contains("--config-alias-delete") || deleteProvider.waitingConfirmation(userID)) {
            if (deleteProvider.waitingConfirmation(userID)) {
                try {
                    reply = deleteProvider.confirmDeletion(userID, message);
                } catch (IOException e) {
                    reply = e.getMessage();
                    e.printStackTrace();
                }
                sendMessage(chatID, reply, true);
            }
            else {
                reply = deleteProvider.requestDeletion(userID, message);
                sendMessage(chatID, reply, false);
            }
        }
        else if (message.contains("--config-alias-show")) {
            try {
                reply = AgeProvider.doShowUserData(userID, message);
                sendMessage(chatID, reply, false);
            } catch (Exception e) {
                reply = e.getMessage();
                sendMessage(chatID, reply, false);
            }
        }
        else if (message.contains("--config-alias") || message.contains("--config-utc")) {
            try {
                AgeProvider.doConfigParsing(userID, message);
                sendMessage(chatID, "_Done!_", true);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                reply = e.getMessage();
                sendMessage(chatID, reply, false);
                throw e;
            }
        }
        else {
            //doParsing and reply
            try {
                reply = AgeProvider.doParsingAndCalc(userID, message);
            } catch (ParseException e) {
                e.printStackTrace();
                reply = e.getMessage();
            } catch (Exception e) {
                reply = e.getMessage();
                sendMessage(chatID, reply, false);
                throw e;
            }

            sendMessage(chatID, reply, false);
        }
    }

    private void sendMessage(String chatID, String reply, boolean enableMarkdown) {
        SendMessage message = new SendMessage();
        if (enableMarkdown) {
            message.enableMarkdown(true);
        }
        message.setChatId(chatID);
        message.setText(reply);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
