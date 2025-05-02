package src.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherInfoConverter {
    private List<Map<String,String>> extractedItems;
    public WeatherInfoConverter(String responsedAPIJSON){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
           WeatherDTO deserializedDto = objectMapper.readValue(responsedAPIJSON,WeatherDTO.class);
           extractedItems = new ArrayList<>();//basetime,category,fcsttime,fcstvalue만 추출
           List<Item> items = deserializedDto.getResponse().getBody().getItems().getItem();
           for(Item i:items){
               Map<String,String> temp = new HashMap<>();
               temp.put("baseTime", changeTimeFormat(i.getBaseTime()));
               temp.put("category", categoryCodeToReal(i.getCategory()));
               temp.put("fcstTime", changeTimeFormat(i.getFsctTime()));
               temp.put("fcstValue", fcstValueCodeToReal(i.getFsctValue(),i.getCategory()));
               extractedItems.add(temp);
           }
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }


    }
    private String categoryCodeToReal(String category){
        switch(category){
            case "T1H":
                return "기온";
            case "RN1":
                return "1시간 강수량";
            case "UUU":
                return "동서바람성분";
            case "VVV":
                return "남북바람성분";
            case "REH":
                return "습도";
            case "PTY":
                return "강수형태";
            case "VEC":
                return "풍향";
            case "WSD":
                return "풍속";
        }
        return "category error";
    }
    private String fcstValueCodeToReal(String fcstValue,String category) {
        int intValue = Integer.parseInt(fcstValue);
        switch (category) {
            case "T1H":
                return fcstValue + "°C";
            case "RN1":
                return fcstValue + "mm";
            case "UUU":
                if (intValue > 0)
                    return "동풍 " + Math.abs(intValue) + "m/s";
                else if (intValue < 0)
                    return "서풍 " + Math.abs(intValue) + "m/s";
                else
                    return intValue + "m/s";
            case "VVV":
                if (intValue > 0)
                    return Math.abs(intValue) + "m/s";
                else if (intValue < 0)
                    return Math.abs(intValue) + "m/s";
                else
                    return "남북 " + intValue + "m/s";
            case "REH":
                return fcstValue + "%";
            case "PTY":
                return convertPYTCode(fcstValue);
            case "VEC":
                return fcstValue + "deg";
            case "WSD":
                return fcstValue + "m/s";
        }
        return "값 변환 오류";
    }
    private String changeTimeFormat(String time){
        if(!time.matches(".*00$")){
            SimpleDateFormat oclock = new SimpleDateFormat("hh시 a");
            oclock.format(time);
            return oclock.toString();
        }else{
            SimpleDateFormat formatedTime = new SimpleDateFormat("hh시 mm분 a");
            formatedTime.format(time);
            return formatedTime.toString();
        }
    }

    private String convertPYTCode(String codeValue){
        switch(codeValue){
            case "0":
                return "없음";
            case "1":
                return "비";
            case "2":
                return "비/눈";
            case "3":
                return "눈";
            case "4":
                return "소나기";
        }
        return "PYT 변환 에러";
    }

    public List<Map<String,String>> getExtractedItems(){
        return extractedItems;
    }
}
