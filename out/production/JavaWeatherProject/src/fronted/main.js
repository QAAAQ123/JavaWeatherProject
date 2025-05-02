const level1 = new Set();
const level2 = new Set();
const level3 = [];

const isRequesting = { value: false };

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

    const button = document.getElementById('btn');
    if (button) {
        // 중복 이벤트 방지를 위해 onclick 사용
        button.onclick = handleClick;
    }
});

function handleClick(e) {
    e.preventDefault();

    const button = e.currentTarget;

    // 중복 클릭 방지
    if (isRequesting.value) return;
    isRequesting.value = true;
    button.disabled = true;

    const params = new URLSearchParams({
        level1: getValue('HTMLLevel1'),
        level2: getValue('HTMLLevel2'),
        level3: getValue('HTMLLevel3')
    });
    const queryString = params.toString();
    console.log("전송된 쿼리 " + queryString);

    fetch(`http://localhost:8000/result?${queryString}`)
        .then((response) => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then((data) => {
            console.log("결과 데이터 받기 완료", data);
            // TODO: 데이터 처리 로직 추가 가능
        })
        .catch((error) => {
            console.error("에러 발생:", error);
        })
        .finally(() => {
            isRequesting.value = false;
            button.disabled = false;
        });
}

function getValue(id) {
    const select = document.getElementById(id);
    return select.options[select.selectedIndex]?.value || '';
}
