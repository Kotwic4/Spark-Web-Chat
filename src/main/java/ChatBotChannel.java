import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

class ChatBotChannel extends AbstractChannel{

    private static final String[] dayOfWeekNames = {"poniedzialek","wtorek","sroda","czwartek","piątek","sobota","niedziela"};

    private WeatherGetter weatherGetter = new WeatherGetter();

    ChatBotChannel() {
        super("chatbot");
    }

    @Override
    public void broadcastMessage(Session session, String message) {
        User user = findUser(session);
        broadcastMessage(user.username,session,message);
        broadcastBotMessege(session, chatbotAnswer(message));
    }

    @Override
    public void addUser(Session session, User user) {
        super.addUser(session, user);
        broadcastBotMessege(session, chatbotAnswer("Hi"));
    }

    private String chatbotAnswer(String message){
        switch (message.trim()){
            case "Hi":
            case "hi":
                return "Hi :)";
            case "Która godzina?":
                return getHour();
            case "Jaki dziś dzień tygodnia?":
                return getDay();
            case "Jaka jest pogoda w Krakowie?":
                return weatherGetter.getWeather();
            case "Help":
            case "help":
                return "Available options :\n" +
                        " 'Hi',\n" +
                        " 'Która godzina?',\n" +
                        " 'Jaki dziś dzień tygodnia?',\n" +
                        " 'Jaka jest pogoda w Krakowie?'";
            default:
                return "Unknown command, write 'Help' for avaiable options";
        }
    }

    private String getHour(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        return "Jest godzina : " + sdf.format(cal.getTime());
    }

    private String getDay(){
        Calendar cal = Calendar.getInstance();
        System.out.println(cal.getTime());
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)-Calendar.MONDAY;
        String day = dayOfWeekNames[dayOfWeek];
        return "Dzisiaj jest " + day;
    }

    private void broadcastBotMessege(Session session, String message){

        broadcastMessage(this.getChannelName(),session,message);
    }

    @Override
    public boolean canDelete() {
        return false;
    }

    private void broadcastMessage(String sender,Session session,String message){
        User user = findUser(session);
        try {
            session.getRemote().sendString(String.valueOf(new JSONObject()
                    .put("userMessage", this.createHtmlMessageFromSender(sender, message))
                    .put("userlist", new String[]{user.username}))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
