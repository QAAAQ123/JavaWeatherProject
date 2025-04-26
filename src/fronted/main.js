let regionData = [];

fetch('http://localhost:8000')
    .then(response => {
    if(!response)
        throw new Error('네트워크 응답이 올바르지 않습니다.');

        return response.json();
    })
    .then(data => {
        regionData = data;
        console.log('서버에서 받은 데이터',regionData);
    })
    .catch(error => {
        console.error('데이터를 불러오는 중 에러 발생: ',error);
    })
