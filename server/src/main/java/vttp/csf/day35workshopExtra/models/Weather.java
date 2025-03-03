package vttp.csf.day35workshopExtra.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Weather {

    private String description;
    private String icon;
    private String name;
    private String country;
    private double temp;
    private double feelsLike;
    private int humidity;
    private double windSpeed;

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public double getTemp() { return temp; }
    public void setTemp(double temp) { this.temp = temp; }
    
    public double getFeelsLike() { return feelsLike; }
    public void setFeelsLike(double feelsLike) { this.feelsLike = feelsLike; }
    
    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }  

    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("description", getDescription())
            .add("icon", getIcon())
            .add("name", getName())
            .add("country", getCountry())
            .add("temp", getTemp())
            .add("feels_like", getFeelsLike())
            .add("humidity", getHumidity())
            .add("wind_speed", getWindSpeed())
            .build();
    }
    
}
