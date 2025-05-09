let level1 = new Set();
let level2 = new Set();
let level3 = new Array();
let isRequesting = false;
window.addEventListener("DOMContentLoaded", () => {
    fetch("http://localhost:8000/data")
        .then((response) => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            console.log("데이터 요청 응답 완료");
            return response.json();
        })
        .then((data) => {
            for (let i = 3; i < data.length; i += 3) level1.add(data[i]);
            for (let i = 4; i < data.length; i += 3) level2.add(data[i]);
            for (let i = 5; i < data.length; i += 3) level3.push(data[i]);

            const HTMLLevel1 = document.getElementById('HTMLLevel1');
            level1.forEach(item => {
                const option = document.createElement('option');
                option.value = item;
                option.textContent = item;
                HTMLLevel1.appendChild(option);
            });

            const HTMLLevel2 = document.getElementById('HTMLLevel2');
            level2.forEach(item => {
                const option = document.createElement('option');
                option.value = item;
                option.textContent = item;
                HTMLLevel2.appendChild(option);
            });

            const HTMLLevel3 = document.getElementById('HTMLLevel3');
            level3.forEach(item => {
                const option = document.createElement('option');
                option.value = item;
                option.textContent = item;
                HTMLLevel3.appendChild(option);
            });
            console.log("데이터 select에 넣기 완료");
        })
        .catch((error) => {
            console.error("에러 발생:", error);
        });
});



var button = document.getElementById('btn');
if (button) {
    button.addEventListener("click", (e) => {
        e.preventDefault();

        // 중복 클릭 방지
        if (isRequesting) return;
        isRequesting = true;
        button.disabled = true;

        const weatherInfo = document.getElementById('weather-info');
        weatherInfo.innerHTML = "로딩 중...";

        const params = new URLSearchParams({
            level1: getValue('HTMLLevel1'),
            level2: getValue('HTMLLevel2'),
            level3: getValue('HTMLLevel3')
        });
        const queryString = params.toString();
        console.log("전송된 쿼리 " + queryString);

        fetch(`http://localhost:8000/result?${queryString}`)
            .then((response) => {
                if (response.ok) {
                    return response.json();
                } else {
                    return response.text().then(text => { throw new Error(text); });
                }
            })
            .then((data) => {
                console.log("결과 데이터 받기 완료", data);
                const level1 = getValue('HTMLLevel1');
                const level2 = getValue('HTMLLevel2');
                const level3 = getValue('HTMLLevel3');
                const region = `${level1} ${level2} ${level3}`.trim();

                const weatherByTime = {};
                data.forEach(item => {
                    if(!weatherByTime[item.fcstTime]){
                        weatherByTime[item.fcstTime] = [];
                    }
                    weatherByTime[item.fcstTime].push(`${item.category}: ${item.fcstValue}`);

                });
                let weatherText = "";
                    for (const [time, infos] of Object.entries(weatherByTime)) {
                        weatherText += `${time}:<br>`;
                        infos.forEach(info => {
                        weatherText += ` - ${info}<br>`;
                    });
                }

                // p 태그 업데이트
                weatherInfo.innerHTML = `선택한 지역: ${region}<br>날씨 정보:<br>${weatherText}`;

            })
            .catch((error) => {
                console.error("에러 발생:", error);
            })
            .finally(() => {
                isRequesting = false;
                button.disabled = false;
            });
    });
}
function getValue(level) {
    const selected = document.getElementById(level);
    return selected.options[selected.selectedIndex].value;
}