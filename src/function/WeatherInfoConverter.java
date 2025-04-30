package src.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import src.server.Server;

public class WeatherInfoConverter {
    private String weatherInfo;
    public WeatherInfoConverter(String responsedAPIJSON){
        ObjectMapper objectMapper = new ObjectMapper();

        
    }


    public String getWeatherInfo(){
        return this.weatherInfo;
    }
}
