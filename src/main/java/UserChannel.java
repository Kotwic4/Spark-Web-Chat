import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static j2html.TagCreator.*;

@WebSocket
class UserChannel implements IChannel{

    private final String channelName;

    private Map<Session, User> usersMap = new ConcurrentHashMap<>();

    UserChannel(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String getChannelName() {
        return this.channelName;
    }

    @Override
    public String toString(){
        return this.channelName;
    }

    @Override
    public void addUser(Session session, User user){
        this.usersMap.put(session, user);
        this.broadcastServerMessage(user.username + " joined the chat");
    }

    @Override
    public User removeUser(Session session){
        User user = findUser(session);
        this.usersMap.remove(session);
        this.broadcastServerMessage(user.username + " left the chat");
        return user;
    }

    @Override
    public boolean userExist(Session session){
        return this.usersMap.containsKey(session);
    }

    private User findUser(Session session){
        return this.usersMap.get(session);
    }

    public void broadcastMessage(Session session, String message) {
        String sender = this.findUser(session).username;
        this.usersMap.keySet().stream().filter(Session::isOpen).forEach(mapSession -> {
            try {
                mapSession.getRemote().sendString(String.valueOf(new JSONObject()
                        .put("userMessage", this.createHtmlMessageFromSender(sender, message))
                        .put("userlist", this.usersMap.values())
                        .put("channelList", Main.channels.values()))
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void broadcastServerMessage(String message) {
        String sender = "Server";
        this.usersMap.keySet().stream().filter(Session::isOpen).forEach(mapSession -> {
            try {
                mapSession.getRemote().sendString(String.valueOf(new JSONObject()
                        .put("userMessage", this.createHtmlMessageFromSender(sender, message))
                        .put("userlist", this.usersMap.values())
                        .put("channelList", Main.channels.values()))
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //Builds a HTML element with a sender-name, a message, and a timestamp,
    private String createHtmlMessageFromSender(String sender, String message) {
        return article().with(
                b(sender + " says:"),
                p(message),
                span().withClass("timestamp").withText(new SimpleDateFormat("HH:mm:ss").format(new Date()))
        ).render();
    }

}
