package src.server;



import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;

public class TempServer {
    public void start() throws IOException{
        InetSocketAddress address = new InetSocketAddress(8080);
        HttpServer httpServer = HttpServer.create(address,0);

        httpServer.createContext("/",(exchange -> {
            //요청 받기
            String method = exchange.getRequestMethod();
            System.out.println("클라이언트 요청: " + method);
            
            //path 정보 얻기
            URI uri = exchange.getRequestURI();
            String path = uri.getPath();
            System.out.println("Path: " + path);
            
            //header 정보 얻기
            System.out.println("헤더");
            Headers headers = exchange.getRequestHeaders();
            for(String key: headers.keySet()){
                List<String> values = headers.get(key);
                System.out.println(key + ": " + values);
            }
            
            //body 정보 얻기
            System.out.println("바디");
            InputStream inputStream = exchange.getRequestBody();
            byte[] bytes = inputStream.readAllBytes();
            String body = new String(bytes);
            System.out.println(body);

            //응답(response) 작성
            System.out.println("응답");
            String response = "Hello";
            exchange.sendResponseHeaders(200,response.length());
            //HTTP 상태코드와(OK)와 응답 바디의 길이 설정(response.length())
            try(OutputStream os = exchange.getResponseBody()){
                os.write(response.getBytes());
            }
            //응답 바디에 Hello를 작성하고 스트림을 자동으로 닫음(try문이 끝나면 자동으로 스트림 닫힘)
        }));

        httpServer.start();
    }

}