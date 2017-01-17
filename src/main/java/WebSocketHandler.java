import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;

@WebSocket
public class WebSocketHandler {

    private CookieHandle cookieHandle = new CookieHandle();
    @OnWebSocketConnect
    public void onConnect(Session session) throws Exception {
        String username = cookieHandle.getUsername(session.getUpgradeRequest().getCookies());
        String channelName = cookieHandle.getChannelName(session.getUpgradeRequest().getCookies());
        if(username.isEmpty() || channelName.isEmpty()){
            session.close();
        }
        else{
            User user = new User(username);
            if(!Main.channels.containsKey(channelName)){
                Main.channels.put(channelName,new UserChannel(channelName));
            }
            Main.channels.get(channelName).addUser(session,user);
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        Main.channels.values().stream()
                .filter(iChannel -> iChannel.userExist(session))
                .forEach(iChannel -> {
                            iChannel.removeUser(session);
                            if(iChannel.canDelete()){
                                Main.channels.remove(iChannel.getChannelName());
                            }
                        }
                        );
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        Main.channels.values().stream()
                .filter(iChannel -> iChannel.userExist(session))
                .forEach(iChannel -> iChannel.broadcastMessage(session,message));
    }

}