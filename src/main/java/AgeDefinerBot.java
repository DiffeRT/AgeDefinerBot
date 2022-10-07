import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;

public class AgeDefinerBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "AgeDefinerBot";
//        return "DebugAppRustBot";//Debug
    }

    @Override
    public String getBotToken() {
        Path pathSecret = Paths.get(System.getProperty("user.dir")).resolve("Age.secret");
        String result = null;
        try {
            result = new String(Files.readAllBytes(pathSecret), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
//        return "5765430991:AAEAftF48zI2Mx-jzWNyVcPoTPO6-55_f8g"; //Debug
    }

    @Override
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        String chatID = update.getMessage().getChatId().toString();
        String userID = update.getMessage().getFrom().getId().toString();
        String reply = null;

        if (message.equals("/start")) {
            reply = AgeProvider.startCommandReply();
            sendMessage(chatID, reply, true);
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
