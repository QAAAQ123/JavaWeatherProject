package src.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.List;

public class Server {
    public void run() throws IOException {
        InetSocketAddress address = new InetSocketAddress(8000);
        HttpServer httpServer = HttpServer.create(address, 0);

        //root html파일 get 요청시
        httpServer.createContext("/", (exchange -> {
            //http method,path print
            String clienRequestHttpMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            System.out.println("request method:" + clienRequestHttpMethod + " Path:" + path);

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
            System.out.println("request method:" + clienRequestHttpMethod + " Path:" + path);

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
            System.out.println("request method:" + clienRequestHttpMethod + " Path:" + path);

            //out js file to 8000/main.js from here
            File mainjs = new File("src/fronted/main.js");
            byte[] jsBytes = Files.readAllBytes(mainjs.toPath());
            exchange.sendResponseHeaders(200, jsBytes.length);
            exchange.getResponseHeaders().set("Content-Type", "application/javascript; charset=utf-8");
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(jsBytes);
            }
        }));

        httpServer.createContext("/data",exchange -> {
            String clientRequestMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            System.out.println("request method:" + clientRequestMethod + "Path:" + path);

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

        httpServer.start();
    }

}
