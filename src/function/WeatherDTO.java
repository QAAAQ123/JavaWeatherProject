package src.function;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WeatherDTO {
    @JsonProperty("response")
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}

class Response {
    @JsonProperty("header")
    private Header header;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    @JsonProperty("body")
    private Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}

class Header {
    @JsonProperty("resultCode")
    private String resultCode;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    @JsonProperty("resultMsg")
    private String resultMsg;

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}

class Body {
    @JsonProperty("dataType")
    private String dataType;
    @JsonProperty("items")
    private Items items;
    @JsonProperty("numOfRows")
    private int numOfRows;
    @JsonProperty("pageNo")
    private int pageNo;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    @JsonProperty("totalCount")
    private int totalCount;
}

class Items {
    @JsonProperty("item")
    private List<Item> item;

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }
}

class Item {
    @JsonProperty("baseDate")
    private String baseDate;
    @JsonProperty("baseTime")
    private String baseTime;
    @JsonProperty("category")
    private String category;

    public String getFsctDate() {
        return fsctDate;
    }

    public void setFsctDate(String fsctDate) {
        this.fsctDate = fsctDate;
    }

    public String getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(String baseDate) {
        this.baseDate = baseDate;
    }

    public int getNx() {
        return nx;
    }

    public void setNx(int nx) {
        this.nx = nx;
    }

    public int getNy() {
        return ny;
    }

    public void setNy(int ny) {
        this.ny = ny;
    }

    public String getFsctValue() {
        return fsctValue;
    }

    public void setFsctValue(String fsctValue) {
        this.fsctValue = fsctValue;
    }

    public String getFsctTime() {
        return fsctTime;
    }

    public void setFsctTime(String fsctTime) {
        this.fsctTime = fsctTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(String baseTime) {
        this.baseTime = baseTime;
    }

    @JsonProperty("fcstDate")
    private String fsctDate;
    @JsonProperty("fcstTime")
    private String fsctTime;
    @JsonProperty("fcstValue")
    private String fsctValue;
    @JsonProperty("nx")
    private int nx;
    @JsonProperty("ny")
    private int ny;
}

