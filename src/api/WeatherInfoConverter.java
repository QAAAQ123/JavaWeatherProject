package src.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherInfoConverter {
    private String weatherInfo;
    public WeatherInfoConverter(String responsedAPIJSON){
        
    }


    public String getWeatherInfo(){
        return this.weatherInfo;
    }
}
