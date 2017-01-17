import org.eclipse.jetty.websocket.api.Session;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static j2html.TagCreator.*;

abstract class AbstractChannel implements IChannel{


    private final String channelName;

    Map<Session, User> usersMap = new ConcurrentHashMap<>();

    AbstractChannel(String channelName) {
        this.channelName = channelName;
    }

    String createHtmlMessageFromSender(String sender, String message) {
        return article().with(
                b(sender + " says:"),
                p(message),
                span().withClass("timestamp").withText(new SimpleDateFormat("HH:mm:ss").format(new Date()))
        ).render();
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
    public boolean userExist(Session session){
        return this.usersMap.containsKey(session);
    }

    User findUser(Session session){
        return this.usersMap.get(session);
    }

    @Override
    public void addUser(Session session, User user){
        this.usersMap.put(session, user);
    }

    @Override
    public User removeUser(Session session){
        User user = findUser(session);
        this.usersMap.remove(session);
        return user;
    }

    @Override
    public boolean canDelete() {
        return this.usersMap.isEmpty();
    }


}
