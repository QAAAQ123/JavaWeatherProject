package src;

import src.server.Server;
import src.server.TempServer;
import java.io.IOException;

/*
1. 서버 만들기
2. 프론트 엔드 지역 선택 및 검색 기능 작성
    1.프론트에서 get 요청이 오면 서버에서 API로 지역명 받아오는 기능 작성
    2.받아온 정보를 프론트엔드가 받아 지역 선택 및 검색 기능 만듦
3. 서버에서 JSON으로 받은 데이터 서버에서 API에서 사용되는 형태로 정리
4. 정리된 데이터를 가지고 서버에서 API로 요청 보내기
5. API로 받은 데이터(jackson으로 받아) 서버에서 가공하기
6. 가공된 데이터 JSON으로 변환해 프론트로 보내기
7. 프론트에서 데이터 띄워줌
 */
public class WeatherProject {
    public static void main(String[] args) {
        Server Server = new Server();
        try{
            Server.run();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

