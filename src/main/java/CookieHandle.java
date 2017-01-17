import java.net.HttpCookie;
import java.util.List;

public class CookieHandle {

    public String getUsername(List<HttpCookie> cookies){
        return getValue(cookies,"username");
    }

    public String getChannelName(List<HttpCookie> cookies) {return  getValue(cookies,"channelName");}

    public String getValue(List<HttpCookie> cookies, String name){
        for(HttpCookie cookie : cookies){
            if(cookie.getName().equals(name)){
                return cookie.getValue();
            }
        }
        return "";
    }
}
