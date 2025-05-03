package src.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import src.function.APIFitDateTime;
import src.function.ReadCSVData;
import src.function.WeatherApi;
import src.function.WeatherInfoConverter;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;


public class Server {
    static int count = 0;

    public void run() throws IOException {
        InetSocketAddress address = new InetSocketAddress(8000);
        HttpServer httpServer = HttpServer.create(address, 0);

        //root html파일 get 요청시
        httpServer.createContext("/", (exchange -> {
            //http method,path print
            String clienRequestHttpMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            System.out.println("request method:" + clienRequestHttpMethod + " Path:" + path + " Count:" + count++);

            //out html file to 8000port from here
            File mainHTML = new File("src/fronted/main.html");
            byte[] HTMLBytes = Files.readAllBytes(mainHTML.toPath());
            exchange.sendResponseHeaders(200, HTMLBytes.length);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(HTMLBytes);
            }
        }));

        httpServer.createContext("/favicon.ico", exchange -> {
            exchange.sendResponseHeaders(204, -1); // No Content
            exchange.close();
        });

        // /main.css 요청시
        httpServer.createContext("/main.css", (exchange -> {
            //http method,path print
            String clienRequestHttpMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            System.out.println("request method:" + clienRequestHttpMethod + " Path:" + path + " Count:" + count++);

            //out css file to 8000/main.css from here
            File mainCSS = new File("src/fronted/main.css");
            byte[] CSSBytes = Files.readAllBytes(mainCSS.toPath());
            exchange.sendResponseHeaders(200, CSSBytes.length);
            exchange.getResponseHeaders().set("Content-Type", "text/css; charset=utf-8");
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(CSSBytes);
            }
        }));

        // /main.js 요청시
        httpServer.createContext("/main.js", (exchange -> {
            //http method,path print
            String clienRequestHttpMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            System.out.println("request method:" + clienRequestHttpMethod + " Path:" + path + " Count:" + count++);

            //out js file to 8000/main.js from here
            File mainjs = new File("src/fronted/main.js");
            byte[] jsBytes = Files.readAllBytes(mainjs.toPath());
            exchange.sendResponseHeaders(200, jsBytes.length);
            exchange.getResponseHeaders().set("Content-Type", "application/javascript; charset=utf-8");
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(jsBytes);
            }
        }));

        //지역명 데이터 요청시
        httpServer.createContext("/data", exchange -> {
            String clienRequestHttpMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            System.out.println("request method:" + clienRequestHttpMethod + " Path:" + path + " Count:" + count++);

            //get region data from csv
            ReadCSVData readCSVData = new ReadCSVData();
            List<String> result = readCSVData.getRegionList();

            //jackson
            ObjectMapper objectMapper = new ObjectMapper();
            String JSON = objectMapper.writeValueAsString(result);
            byte[] ByteJSON = JSON.getBytes();

            exchange.sendResponseHeaders(200, ByteJSON.length);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(ByteJSON);
            }
        });

        //검색 결과 확인
        httpServer.createContext("/result", exchange -> {
            //메소드,경로
            String clientRequestHttpMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            System.out.println("request method:" + clientRequestHttpMethod + " Path:" + path + " Count:" + count++);

            try {
                //클라이언트 파라미터로 지역명 데이터 받아오기 regions에 저장됨
                String regionsParams = exchange.getRequestURI().getQuery();
                String[] temp = regionsParams.split("=|&");
                String[] extendedTemp = Arrays.copyOf(temp, 6);
                extendedTemp[5] = extendedTemp[5] == null ? "" : extendedTemp[5];
                String[] regions = new String[3];
                int w = 0;
                for (int i = 1; i <= extendedTemp.length; i += 2) {
                    regions[w] = extendedTemp[i];
                    w++;
                }
                System.out.println("서버가 받은 지역명:" + Arrays.toString(regions));

                //지역명 -> 좌표 변환(지역명이 맞지 않으면 get response로 오류 전송)
                ReadCSVData t = new ReadCSVData();

                //지역명,2차원 배열 [[시,구,동],[시,구,동],....] -> 3행xn열
                String[][] allRegions = new String[t.getRegionList().size() / 3][3];
                List<String> a = t.getRegionList();
                for (int i = 0; i < a.size() / 3; i++) {
                    for (int k = 0; k < 3; k++) {
                        allRegions[i][k] = a.get(3 * i + k);
                    }
                }
                //지역 좌표,2차원 배열
                String[][] allCoordinate = new String[t.getCoordinateList().size() / 2][2];
                List<String> b = t.getCoordinateList();
                for (int i = 0; i < b.size() / 2; i++) {
                    for (int k = 0; k < 2; k++) {
                        allCoordinate[i][k] = b.get(2 * i + k);
                    }
                }

                //클라이언트가 보낸 지역명이 CSV파일에 존재하는 지역명인지 확인
                boolean validRegion = false;
                int selectedIndex = 0;
                for (; selectedIndex < allRegions.length; selectedIndex++) {
                    if (Arrays.toString(allRegions[selectedIndex]).equals(Arrays.toString(regions))) {
                        System.out.println("동일한 지역명 존재");
                        validRegion = true;
                        break;
                    }
                }
                if (validRegion) {
                    //지역명을 좌표로 바꿔서 api전송 -> api에서 받은 데이터를 서버에서 가공하여 json으로 클라이언트에게 전송
                    WeatherApi weatherApi = new WeatherApi();
                    //data(yyyyMMdd),time(24단위,1시간 간격),nx,ny값(selectedIndex) 값 구하기
                    APIFitDateTime apiFitDateTime = new APIFitDateTime();

                    String responsedAPIJSON = weatherApi.responseFromAPI(
                            weatherApi.requestToAPI(
                                    apiFitDateTime.getApiFitDate(), apiFitDateTime.getApiFitTime(), allCoordinate[selectedIndex][0], allCoordinate[selectedIndex][1])
                    );
                    WeatherInfoConverter weatherInfoConverter = new WeatherInfoConverter(responsedAPIJSON);
                    ObjectMapper objectMapper = new ObjectMapper();
                    String resultJSON = objectMapper.writeValueAsString(weatherInfoConverter.getExtractedItems());
                    byte[] resultBytes = resultJSON.getBytes();

                    exchange.sendResponseHeaders(200, resultBytes.length);
                    exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");

                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(resultBytes);
                    }
                    System.out.println("전송된 데이터: " + new String(resultBytes, StandardCharsets.UTF_8));
                } else if (!validRegion) {
                    System.out.println("동일한 지역명이 존재하지 않음");
                    exchange.sendResponseHeaders(404, 0);
                    exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
                    try (OutputStream os = exchange.getResponseBody()) {
                        String message = "404 Not Found: 해당하는 지역명이 존재하지 않습니다.";
                        os.write(message.getBytes("UTF-8"));
                    }
                }
            }catch(Exception e){
                System.err.println("예외 발생:" + e.getMessage());
                e.printStackTrace();
                exchange.sendResponseHeaders(500,0);
                exchange.getResponseHeaders().set("Content-Type","text/plain; charset=utf-8");
                try(OutputStream os = exchange.getResponseBody()){
                    os.write("서버 오류 발생".getBytes(StandardCharsets.UTF_8));
                }
            }finally {
                exchange.close();
            }
        });

        httpServer.start();
    }

}
