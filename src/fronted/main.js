//fetch("localhost:8000/data")
//    .then((response) => response.json())
//    .then((data) => console.log(data));

fetch("localhost:8000", {
    method:"GET",
    headers:{
        "Content-Type" : "applicaiton/json",
    }
    .then((response) => response.json())
    .then((data) => console.log(data));
