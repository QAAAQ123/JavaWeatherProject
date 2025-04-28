package src.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

public class Server {
    static int  count = 0;
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
            exchange.sendResponseHeaders(200,HTMLBytes.length);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(HTMLBytes);
            }
        }));

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
        httpServer.createContext("/data",exchange -> {
            String clienRequestHttpMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            System.out.println("request method:" + clienRequestHttpMethod + " Path:" + path + " Count:" + count++);

            //get region data from csv
            CoordinateToRegionMapper coordinateToRegionMapper = new CoordinateToRegionMapper();
            List<String> result = coordinateToRegionMapper.getRegionList();

            //jackson
            ObjectMapper objectMapper = new ObjectMapper();
            String JSON = objectMapper.writeValueAsString(result);
            byte[] ByteJSON = JSON.getBytes();

            exchange.sendResponseHeaders(200,ByteJSON.length);
            exchange.getResponseHeaders().set("Content-Type","application/json");
            try(OutputStream os = exchange.getResponseBody()){
                os.write(ByteJSON);
            }
        });

        //검색 결과 확인
        httpServer.createContext("/result",exchange -> {
            //메소드,경로
            String clientRequestHttpMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            System.out.println("request method:" + clientRequestHttpMethod + " Path:" + path + " Count:" + count);


            //클라이언트 파라미터로 지역명 데이터 받아오기
            String regionsParams = exchange.getRequestURI().getQuery();
            String[] temp = regionsParams.split("=|&");
            String[] regions = new String[3];
            int j = 0;
            for(int i = 1; i < temp.length; i += 2){
                regions[j] = temp[i];
                j++;
            }

            //지역명 -> 좌표 변환(지역명이 맞지 않으면 get response로 오류 전송)
        });

        httpServer.start();
    }

}
