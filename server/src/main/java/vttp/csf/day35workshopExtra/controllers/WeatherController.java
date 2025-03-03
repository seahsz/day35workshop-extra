package vttp.csf.day35workshopExtra.controllers;

import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vttp.csf.day35workshopExtra.models.Weather;
import vttp.csf.day35workshopExtra.services.WeatherService;

@Controller
@RequestMapping("/api")
public class WeatherController {

    @Autowired
    private WeatherService weatherSvc;

    Logger logger = Logger.getLogger(WeatherController.class.getName());

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<String> getWeather(
        @RequestParam String city
    ) {
        city = city.toLowerCase();
        logger.info("City received is: " + city);
        Optional<String> optString = weatherSvc.getCache(city);
        if (optString.isPresent()) {
            logger.info(">>>> Returning info from CACHE");
            return ResponseEntity.ok(optString.get());
        }

        Optional<Weather> opt = weatherSvc.getApi(city);
        if (opt.isEmpty())
            return ResponseEntity.status(404).body("{No city found}");
        logger.info(">>>> Returning info from API");
        return ResponseEntity.ok(opt.get().toJson().toString());
    }
    
    
}
