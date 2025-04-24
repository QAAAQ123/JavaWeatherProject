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

public class Server {
    public void run() throws IOException{
        InetSocketAddress address = new InetSocketAddress(8000);
        HttpServer httpServer = HttpServer.create(address,0);

        httpServer.createContext("/",(exchange -> {
            String clienRequestHttpMethod = exchange.getRequestMethod();
            System.out.println("request method: "+clienRequestHttpMethod + "\n");

            WeatherApi weatherApi = new WeatherApi();
            URL url = weatherApi.requestAllDistrictName();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type","application/json");
            BufferedReader bufferedReader;
            if (connection.getResponseCode() >= 200 && connection.getResponseCode() <= 300) {
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }else {
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            StringBuilder resultBuilder = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null) resultBuilder.append(line);
            String result = resultBuilder.toString();

            exchange.sendResponseHeaders(200,result.length());
            try(OutputStream os = exchange.getResponseBody()){
                os.write(result.getBytes());
            }

            bufferedReader.close();
            connection.disconnect();

        }));

        httpServer.start();
    }

}
