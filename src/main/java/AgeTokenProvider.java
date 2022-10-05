import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class AgeTokenProvider {
    private Map<String, TokenValue> tokensTable;
    private final String userID;
    private Path atpDBPath;

    public AgeTokenProvider(String userID) {
        this.userID = userID;
        tokensTable = new HashMap<>();
        initDBPath();

        if (hasUserDefinedFunc()) {
            loadFromFile();
        }
    }

    public boolean containsKey(String key) {
        return tokensTable.containsKey(key);
    }

    public String getTokenExpression(String key) {
        TokenValue obj = (TokenValue)tokensTable.get(key);
        return obj.getFunc();
    }

    public String getTokenDescription(String key) {
        TokenValue obj = (TokenValue)tokensTable.get(key);
        return obj.getDescr();
    }

    public void setToken(String key, String func, String descr) throws IOException {
        TokenValue tv = new TokenValue(func, descr);
        tokensTable.put(key.toLowerCase(), tv);
        saveToFile();
    }

    private void initDBPath() {
        Path path = Paths.get(System.getProperty("user.dir")).resolve("db");
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        atpDBPath = path;
    }

    private void loadFromFile() {
        Path filePath = atpDBPath.resolve(userID + ".json");
        String jsonText = "";
        try {
            jsonText = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, TokenValue> map = mapper.readValue(jsonText, new TypeReference<Map<String, TokenValue>>() {});
            tokensTable = map;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private boolean hasUserDefinedFunc() {
        Path filePath = atpDBPath.resolve(userID + ".json");
        return  Files.exists(filePath);
    }

    private void saveToFile() throws IOException {
        Path filePath = atpDBPath.resolve(userID + ".json");
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = mapper.writeValueAsString(tokensTable);
        //System.out.println(jsonResult);

        FileWriter file = new FileWriter(filePath.toString());
        file.write(jsonResult);
        file.close();
    }
}

