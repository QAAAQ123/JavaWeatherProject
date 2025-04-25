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
    public URL requestAllDistrictName() throws MalformedURLException { //전체 지역명 받아오기
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"); /*URL*/

        urlBuilder.append("?" + "serviceKey" + "=" + readAPIKey()); /*Service Key*/
        urlBuilder.append("&" + "service" + "=" + "1"); /*페이지번호*/
        urlBuilder.append("&" + "numOfRows" + "=" + "1000"); /*한 페이지 결과 수*/
        urlBuilder.append("&" + "dataType" + "=" + "JSON"); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + "base_date" + "=" + "20250425"); /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + "base_time" + "=" + "0600"); /*06시 발표(정시단위) */
        urlBuilder.append("&" + "nx" + "=" + "55"); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + "ny" + "=" + "127"); /*예보지점의 Y 좌표값*/

        System.out.println("Request URL: " + new URL(urlBuilder.toString())); // 디버그용 출력
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
