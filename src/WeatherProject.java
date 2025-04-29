package src;


import src.server.Server;

import java.io.IOException;

/*
1. 서버 만들기
2. 프론트 엔드 지역 선택 및 검색 기능 작성
    1.클라이언트에서 get 요청이 오면 서버에서 CSV의 지역명 읽어서 보냄
    2.받아온 정보를 프론트엔드가 받아 지역 선택 및 검색 기능 만듦
3. 클라이언트에서 JSON으로 받은 데이터 서버에서 API에서 사용되는 형태로 정리
    1. 행정구역 명 JSON받아서 거기에 맞는 X,Y 좌표값 으로 변환
4. 현재 년월일시분과 좌표값을 API 기준에 맞게 URL요청으로 보냄
5. API로 받은 데이터(jackson으로 받아) 서버에서 가공하기
6. 가공된 데이터 JSON으로 변환해 프론트로 보내기
7. 프론트에 데이터 띄워줌
 */
public class WeatherProject {
    public static void main(String[] args) {
        Server Server = new Server();
        try{
            Server.run();
        }catch(IOException e) {
            e.printStackTrace();

        }
    }
}

