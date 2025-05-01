package src.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import src.server.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherInfoConverter {
    private String weatherInfo;
    public WeatherInfoConverter(String responsedAPIJSON){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
           WeatherDTO deserializedDto = objectMapper.readValue(responsedAPIJSON,WeatherDTO.class);

           List<Map<String,String>> extractedItems = new ArrayList<>();//basetime,category,fcsttime,fcstvalue만 추출
           List<Item> items = deserializedDto.getResponse().getBody().getItems().getItem();
           for(Item i:items){
               Map<String,String> temp = new HashMap<>();
               temp.put("baseTime", changeTimeFormat(i.getBaseTime()));
               temp.put("category", categoryCodeToReal(i.getCategory()));
               temp.put("fcstTime", changeTimeFormat(i.getFsctTime()));
               temp.put("fcstValue", fcstValueCodeToReal(i.getFsctValue()));
               extractedItems.add(temp);
           }
            System.out.println(extractedItems);
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }


    }
    public String categoryCodeToReal(String category){
        return "";
    }
    public String fcstValueCodeToReal(String category){
        return "";
    }
    public String changeTimeFormat(String time){
        return "";
    }


    public String getWeatherInfo(){
        return this.weatherInfo;
    }
}
