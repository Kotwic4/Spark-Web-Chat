package chat;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

class UserChannel extends AbstractChannel{

    UserChannel(String channelName) {
        super(channelName);
    }

    @Override
    public void addUser(Session session, User user){
        super.addUser(session,user);
        this.broadcastServerMessage(user.username + " joined the chat " + this.getChannelName());
    }

    @Override
    public User removeUser(Session session){
        User user = super.removeUser(session);
        this.broadcastServerMessage(user.username + " left the chat " + this.getChannelName());
        return user;
    }

    @Override
    public void broadcastMessage(Session session, String message) {
        String sender = this.findUser(session).username;
        this.broadcastMessage(sender,message);
    }

    private void broadcastServerMessage(String message) {
        this.broadcastMessage("Server",message);
    }

    private void broadcastMessage(String sender,String message){
        this.usersMap.keySet().stream().filter(Session::isOpen).forEach(mapSession -> {
            try {
                mapSession.getRemote().sendString(String.valueOf(new JSONObject()
                        .put("userMessage", this.createHtmlMessageFromSender(sender, message))
                        .put("userlist", this.usersMap.values()))
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }




}
