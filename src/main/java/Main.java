import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static spark.Spark.*;

public class Main {

    public static Map<String,IChannel> channels = new ConcurrentHashMap<>();
    public static String defaultChannel = "Default";

    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        channels.put(defaultChannel,new DefaultChannel());
        channels.put("chatbot",new ChatBotChannel());
        staticFiles.location("/public");
        staticFiles.expireTime(600);
        webSocket("/chat", WebSocketHandler.class);
        get("/channels", (req, res) -> {
            return String.valueOf(new JSONObject().put("channelList", Main.channels.values()));
        });
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

}