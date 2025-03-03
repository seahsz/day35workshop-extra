package vttp.csf.day35workshopExtra.services;

import java.io.StringReader;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;

import vttp.csf.day35workshopExtra.models.Weather;
import vttp.csf.day35workshopExtra.repositories.WeatherRepository;

@Service
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Autowired
    private WeatherRepository weatherRepo;

    public static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public Optional<Weather> getApi(String city) {
        String url = UriComponentsBuilder.fromUriString(BASE_URL)
            .queryParam("appid", apiKey)
            .queryParam("q", city.replaceAll(" ", "+"))
            .queryParam("units", "metric")
            .toUriString();

        RequestEntity<Void> req = RequestEntity.get(url)
            .accept(MediaType.APPLICATION_JSON)
            .build();

        RestTemplate template = new RestTemplate();

        try {
            ResponseEntity<String> response = template.exchange(req, String.class);
            String payload = response.getBody();
            JsonObject jsonPayload = Json.createReader(new StringReader(payload)).readObject();
            Weather weather = jsonToWeather(jsonPayload);
            weatherRepo.cacheWeather(city, weather.toJson().toString());
            return Optional.of(weather);

        } catch (Exception ex) {
            System.err.printf("Error while retrieving API\n");
            ex.printStackTrace();
            return Optional.empty();
        }
    }

    public void cacheWeather(String city, String json) {
        weatherRepo.cacheWeather(city, json);
    }

    public Optional<String> getCache(String city) {
        return weatherRepo.getWeather(city);
    }

    private Weather jsonToWeather(JsonObject json) {
        Weather w = new Weather();
        JsonObject weather = json.getJsonArray("weather").getJsonObject(0);
        JsonObject main = json.getJsonObject("main");

        String iconUrl = "https://openweathermap.org/img/wn/" + weather.getString("icon") + "@2x.png";

        w.setCountry(json.getJsonObject("sys").getString("country"));
        w.setDescription(weather.getString("description"));
        w.setFeelsLike(main.getJsonNumber("feels_like").doubleValue());
        w.setHumidity(main.getInt("humidity"));
        w.setIcon(iconUrl);
        w.setName(json.getString("name"));
        w.setTemp(main.getJsonNumber("temp").doubleValue());
        w.setWindSpeed(json.getJsonObject("wind").getJsonNumber("speed").doubleValue());

        return w;
    }
    
}
