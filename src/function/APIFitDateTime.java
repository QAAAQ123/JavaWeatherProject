package src.function;

import java.text.SimpleDateFormat;
import java.util.Date;

public class APIFitDateTime {
    private String apiFitTime;
    private String apiFitDate;

    public APIFitDateTime() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");

        // 현재 시간과 날짜 추출
        String currentTime = timeFormat.format(now);
        apiFitDate = dateFormat.format(now);

        // 현재 시간(시, 분) 추출
        int hours = Integer.parseInt(currentTime.substring(0, 2));
        int minutes = Integer.parseInt(currentTime.substring(2, 4));

        // API 제공 시간(45분 이후) 확인
        if (minutes >= 45) {
            // 45분 이후: 현재 시간의 30분 데이터
            apiFitTime = String.format("%02d30", hours);
        } else {
            // 45분 이전: 이전 시간의 30분 데이터
            if (hours == 0) {
                // 자정 이전: 전날 23:30 데이터
                now = new Date(now.getTime() - 24 * 60 * 60 * 1000); // 전날
                apiFitDate = dateFormat.format(now);
                apiFitTime = "2330";
            } else {
                apiFitTime = String.format("%02d30", hours - 1);
            }
        }

        System.out.println("API 요청 날짜: " + apiFitDate);
        System.out.println("API 요청 시간: " + apiFitTime);
    }

    public String getApiFitDate() {
        return apiFitDate;
    }

    public String getApiFitTime() {
        return apiFitTime;
    }
}