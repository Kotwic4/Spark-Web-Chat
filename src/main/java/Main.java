import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static spark.Spark.*;

public class Main {

    public static Map<String,IChannel> channels = new ConcurrentHashMap<>();
    public static String defaultChannel = "Default";

    public static void main(String[] args) {
        channels.put(defaultChannel,new DefaultChannel());
        channels.put("chatbot",new ChatBotChannel());
        staticFiles.location("/public");
        staticFiles.expireTime(600);
        webSocket("/chat", WebSocketHandler.class);
        get("/channels", (req, res) -> {
            return String.valueOf(new JSONObject().put("channelList", Main.channels.values()));
        });
    }

}