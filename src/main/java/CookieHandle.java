import java.net.HttpCookie;
import java.util.List;

class CookieHandle {

    String getUsername(List<HttpCookie> cookies){
        return getValue(cookies,"username");
    }

    String getChannelName(List<HttpCookie> cookies) {return  getValue(cookies,"channelName");}

    String getValue(List<HttpCookie> cookies, String name){
        for(HttpCookie cookie : cookies){
            if(cookie.getName().equals(name)){
                return cookie.getValue();
            }
        }
        return "";
    }
}
