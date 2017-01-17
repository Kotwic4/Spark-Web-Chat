import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static spark.Spark.*;

public class Main {

    public static Map<String,IChannel> channels = new ConcurrentHashMap<>();
    public static String defalutChannel = "Default";

    public static void main(String[] args) {
        channels.put(defalutChannel,new UserChannel(defalutChannel));
        staticFiles.location("/public");
        staticFiles.expireTime(6);
        webSocket("/chat", WebSocketHandler.class);
        get("/channels", (req, res) -> {
            return String.valueOf(new JSONObject().put("channelList", Main.channels.values()));
        });
    }

}