package src.function;

import com.sun.tools.javac.Main;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class WeatherApi {
    private String key;
    public URL requestToAPI(String date, String time, String nx, String ny) throws MalformedURLException { //전체 지역명 받아오기
        System.out.println("api가 받을 정보:"+date+"/"+time+"/"+nx+"/"+ny);
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst"); /*URL*/
        urlBuilder.append("?" + "serviceKey" + "=" + readAPIKey()); /*Service Key*/
        urlBuilder.append("&" + "numOfRows" + "=" + "60"); /*한 페이지 결과 수*/
        urlBuilder.append("&" + "pageNo" + "=" + "1"); /*페이지번호*/
        urlBuilder.append("&" + "dataType" + "=" + "JSON"); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + "base_date" + "=" + date); /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + "base_time" + "=" + time); /*06시 발표(정시단위) */
        urlBuilder.append("&" + "nx" + "=" + nx); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + "ny" + "=" + ny); /*예보지점의 Y 좌표값*/

        System.out.println("요청 URL: " + new URL(urlBuilder.toString())); // 디버그용 출력
        return new URL(urlBuilder.toString());
    }

    public String responseFromAPI(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        return sb.toString();
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
