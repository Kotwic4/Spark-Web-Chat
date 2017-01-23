package chat;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static spark.Spark.get;

class Chat {
    static final String defaultChannel = "Default";
    private final Map<String, IChannel> channels = new ConcurrentHashMap<>();
    private final CookieHandle cookieHandle = new CookieHandle();

    Chat() {
        initializeDefaultChannels();
        initializeRestApi();
    }

    private void initializeDefaultChannels(){
        channels.put(defaultChannel,new DefaultChannel());
        channels.put("chatbot",new ChatBotChannel());
    }

    private void initializeRestApi(){
        get("/channels", (req, res) -> getJsonChannelsList());
    }

    private String getJsonChannelsList(){
        try {
            return String.valueOf(new JSONObject().put("channelList", channels.values()));
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    void addSession(Session session) {
        String username = this.cookieHandle.getUsername(session.getUpgradeRequest().getCookies());
        String channelName = this.cookieHandle.getChannelName(session.getUpgradeRequest().getCookies());
        if(username.isEmpty() || channelName.isEmpty()){
            session.close();
        }
        else{
            User user = new User(username);
            if(!this.channels.containsKey(channelName)){
                this.channels.put(channelName,new UserChannel(channelName));
            }
            this.channels.get(channelName).addUser(session,user);
        }
    }

    void removeSession(Session session) {
        this.channels.values().stream()
                .filter(iChannel -> iChannel.userExist(session))
                .forEach(iChannel -> {
                            iChannel.removeUser(session);
                            if(iChannel.canDelete()){
                                this.channels.remove(iChannel.getChannelName());
                            }
                        }
                );
    }

    void onMessage(Session session, String message) {
        this.channels.values().stream()
                .filter(iChannel -> iChannel.userExist(session))
                .forEach(iChannel -> iChannel.broadcastMessage(session,message));
    }
}
