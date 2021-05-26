package sk.fri.uniza.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Data {
    @JsonProperty("dateTime")
    private String dateTime;
    @JsonProperty("value")
    private String value;
    @JsonProperty("type")
    private String type;

    @JsonIgnore
    private long id;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dataTime) {
        this.dateTime = dataTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "\nData{" +
                "\"dataTime\": \"" + dateTime + "\"" +
                ", \"value\": \"" + value + "\"" +
                ", \"type\": \"" + type + "\"}";
    }
}
