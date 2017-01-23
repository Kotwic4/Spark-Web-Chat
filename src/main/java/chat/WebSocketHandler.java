package chat;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;

@WebSocket
public class WebSocketHandler {

    Chat chat = new Chat();

    @OnWebSocketConnect
    public void onConnect(Session session) throws Exception {
        this.chat.addSession(session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        this.chat.removeSession(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        this.chat.onMessage(session,message);
    }

}