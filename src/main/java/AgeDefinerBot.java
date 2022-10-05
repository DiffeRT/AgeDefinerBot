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
        //Debug: return "DebugAppRustBot";
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
        //Debug return "5765430991:AAEAftF48zI2Mx-jzWNyVcPoTPO6-55_f8g";
    }

    @Override
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        String chatID = update.getMessage().getChatId().toString();
        String userID = update.getMessage().getFrom().getId().toString();

        if (message.contains("--config-alias") || message.contains("--config-utc")) {
            //doConfigParsing
            try {
                AgeProvider.doConfigParsing(userID, message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            //doParsing and reply
            String reply = null;
            try {
                reply = AgeProvider.doParsingAndCalc(userID, message);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                reply = e.getMessage();
                try {
                    sendMessage(chatID, reply);
                } catch (TelegramApiException ex) {
                    ex.printStackTrace();
                }
                throw e;
            }
            try {
                sendMessage(chatID, reply);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(String chatID, String reply) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatID);
        message.setText(reply);
        execute(message);
    }
}
