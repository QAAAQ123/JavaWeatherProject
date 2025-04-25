package src.server;

import com.sun.net.httpserver.HttpServer;
import src.api.WeatherApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.List;

public class Server {
    public void run() throws IOException{
        InetSocketAddress address = new InetSocketAddress(8000);
        HttpServer httpServer = HttpServer.create(address,0);

        httpServer.createContext("/",(exchange -> {
            String clienRequestHttpMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            System.out.println("request method:"+clienRequestHttpMethod + " Path:" + path);

            CoordinateToRegionMapper coordinateToRegionMapper = new CoordinateToRegionMapper();
            List<String> result = coordinateToRegionMapper.getRegionList();
            exchange.sendResponseHeaders();//보낼 데이터 길이를 넣어야함
            try(OutputStream os = exchange.getResponseBody()){
                os.write(); //byte[] or int로 데이터를 보내야함(주로 JSON으로 보냄)
            }


        }));

        httpServer.start();
    }

}
