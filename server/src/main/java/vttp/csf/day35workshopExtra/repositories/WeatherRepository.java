package vttp.csf.day35workshopExtra.repositories;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WeatherRepository {

    @Autowired
    @Qualifier("redis")
    private RedisTemplate<String, String> template;

    // SET city json
    public void cacheWeather(String city, String json) {
        template.opsForValue().set(city, json);
        template.expire(city, Duration.ofMinutes(15));
    }

    // GET city
    public Optional<String> getWeather(String city) {
        return Optional.ofNullable(template.opsForValue().get(city));
    }
    
}
