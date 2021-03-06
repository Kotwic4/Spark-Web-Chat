package chat;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

class WeatherGetter {

    private final static long WEATHER_DURATION = 1000*60*10;

    private final static String URL = "http://api.openweathermap.org/data/2.5/weather?q=Krakow&appid=ba9e48e5be1545310bcd7406e776e033&units=metric&lang=pl";

    private String weather;

    private Date weatherDate;

    WeatherGetter() {
        this.getCurrentWeather();
    }

    String getWeather(){
        if(new Date().getTime() - this.weatherDate.getTime() > WEATHER_DURATION){
            this.getCurrentWeather();
        }
        return this.weather;
    }

    private void getCurrentWeather() {
        try {
            JsonObject page = this.getJsonFromUrl(URL);
            int temp = page.get("main").getAsJsonObject()
                    .get("temp").getAsInt();
            String description = page.get("weather").getAsJsonArray()
                    .get(0).getAsJsonObject()
                    .get("description").getAsString();
            this.weather = "Pogoda w Krakowie \n" +
                    "Temperatura : " + temp + "\u00b0C\n" +
                    "Opis : " + description;
        } catch (Exception e) {
            e.printStackTrace();
            this.weather = "Error with getting weather information";
        }
        this.weatherDate = new Date();

    }
    private JsonObject getJsonFromUrl(String urlString) throws IOException {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[4096];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return (JsonObject) new JsonParser().parse(buffer.toString());
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}
