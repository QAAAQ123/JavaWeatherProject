package src.api;

import com.sun.tools.javac.Main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

public class WeatherApi {
    private String key;
    public URL requestAllDistrictName() throws MalformedURLException, UnsupportedEncodingException { //전체 지역명 받아오기
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode(readAPIKey(),"UTF-8") + "=서비스키"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("XML", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/

        return new URL(urlBuilder.toString());
    }

    private String readAPIKey(){
        String resource = "src/application.properties";
        Properties properties = new Properties();
        try{
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(resource);

            if(inputStream != null){
                properties.load(inputStream);
            } else{
                throw new FileNotFoundException("file not found");
            }
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        key = properties.getProperty("weather.key");
        return key;
    }
}
