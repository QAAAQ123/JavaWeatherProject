package src.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import src.api.WeatherApi;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

public class Server {
    public void run() throws IOException {
        InetSocketAddress address = new InetSocketAddress(8000);
        HttpServer httpServer = HttpServer.create(address, 0);

        httpServer.createContext("/", (exchange -> {
            //http method,path print
            String clienRequestHttpMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            System.out.println("request mdethod:" + clienRequestHttpMethod + " Path:" + path);

            //get region data from csv
            CoordinateToRegionMapper coordinateToRegionMapper = new CoordinateToRegionMapper();
            List<String> result = coordinateToRegionMapper.getRegionList();

            //jackson
            ObjectMapper objectMapper = new ObjectMapper();
            String JSON = objectMapper.writeValueAsString(result);

            //out html file to 8000port from here
            File mainHTML = new File("src/fronted/main.html");
            byte[] HTMLBytes = Files.readAllBytes(mainHTML.toPath());
            exchange.sendResponseHeaders(200,HTMLBytes.length);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(HTMLBytes);
            }
        }));

        httpServer.start();
    }

}
