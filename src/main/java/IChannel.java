import org.eclipse.jetty.websocket.api.Session;

interface IChannel {
    String getChannelName();
    void addUser(Session session, User user);
    User removeUser(Session session);
    boolean userExist(Session session);
    void broadcastMessage(Session session, String message);
    void broadcastServerMessage(String message);
    boolean canDelete();
}
