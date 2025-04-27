let test = "테스트입니다."
window.addEventListener("DOMContentLoaded", () => {
    fetch("http://localhost:8000/data")
        .then((response) => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then((data) => {
            console.log("받은 지역명 데이터:", data);
        })
        .catch((error) => {
            console.error("데이터를 가져오는 중 에러 발생:", error);
        });
});


