package src.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherInfoConverter {
    private List<Map<String, String>> extractedItems;
    public WeatherInfoConverter(String responsedAPIJSON) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            WeatherDTO deserializedDto = objectMapper.readValue(responsedAPIJSON, WeatherDTO.class);
            System.out.println("가공전 데이터: " + objectMapper.writeValueAsString(deserializedDto));
            extractedItems = new ArrayList<>();//basetime,category,fcsttime,fcstvalue만 추출
            List<Item> items = deserializedDto.getResponse().getBody().getItems().getItem();
            for (Item i : items) {
                Map<String, String> temp = new HashMap<>();
                Map<String,String> test2 = new HashMap<>();
                temp.put("fcstTime", changeTimeFormat(i.getFcstTime()));
                temp.put("fcstValue", fcstValueCodeToReal(i.getFcstValue(), i.getCategory()));
                temp.put("category", categoryCodeToReal(i.getCategory()));
                temp.put("baseTime", changeTimeFormat(i.getBaseTime()));
                extractedItems.add(temp);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    private String changeTimeFormat(String time) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("HHmm");
            SimpleDateFormat outputFormat;

            // 입력값을 Date 객체로 변환
            java.util.Date date = inputFormat.parse(time);

            if (time.endsWith("00")) {
                outputFormat = new SimpleDateFormat("a hh시", java.util.Locale.KOREAN);
            } else {
                outputFormat = new SimpleDateFormat("a hh시 mm분", java.util.Locale.KOREAN);
            }
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "시간 변환 오류";
        }
    }

    public List<Map<String, String>> getExtractedItems() {
        return extractedItems;
    }

    private String fcstValueCodeToReal(String fcstValue, String category) {

        switch(category){
            case "T1H":
                fcstValue = fcstValue + "℃";
                break;
            case "RN1":
                fcstValue = fcstValue + "mm";
                break;
            case "SKY":
                int code = Integer.parseInt(fcstValue);
                if(code == 1) fcstValue = "맑음";
                else if(code == 3) fcstValue = "구름 많음";
                else if(code == 4) fcstValue = "흐림";
                break;
            case "UUU":
                fcstValue = fcstValue + "m/s";
                break;
            case "VVV":
                fcstValue = fcstValue + "m/s";
                break;
            case "REH":
                fcstValue = fcstValue + "%";
                break;
            case "PTY":
                code = Integer.parseInt(fcstValue);
                if(code == 0) fcstValue = "없음";
                else if(code == 1) fcstValue = "비";
                else if(code == 2) fcstValue = "비/눈";
                else if(code == 5) fcstValue = "빗방울";
                else if(code == 6) fcstValue = "빗방울눈날림";
                else if(code == 7) fcstValue = "눈날림";
                break;
            case "LGT":
                fcstValue = fcstValue + "kA";
                break;
            case "VEC":
                fcstValue = fcstValue + "deg";
                break;
            case "WSD":
                fcstValue = fcstValue + "m/s";
                break;
            default:
                fcstValue = "데이터 변환 오류";
        }
        return fcstValue;
    }

    private String categoryCodeToReal(String category) {
        switch(category){
            case "T1H":
                category = "기온";
                break;
            case "RN1":
                category = "1시간 강수량";
                break;
            case "SKY":
                category = "하늘 상태";
                break;
            case "UUU":
                category = "동서바람";
                break;
            case "VVV":
                category = "남북바람";
                break;
            case "REH":
                category = "습도";
                break;
            case "PTY":
                category = "강수형태";
                break;
            case "LGT":
                category = "낙뢰";
                break;
            case "VEC":
                category = "풍향";
                break;
            case "WSD":
                category = "풍속";
                break;
            default:
                category = "카테고리 에러";
        }
        return category;
    }
}
