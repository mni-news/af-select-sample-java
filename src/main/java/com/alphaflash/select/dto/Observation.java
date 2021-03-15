package com.alphaflash.select.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Observation {
    private long dataSeriesId;
    private Object value;
    private Date eventDate;
    private String source;


    public long getDataSeriesId() {
        return dataSeriesId;
    }

    public void setDataSeriesId(long dataSeriesId) {
        this.dataSeriesId = dataSeriesId;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Observation{" +
                "dataSeriesId=" + dataSeriesId +
                ", value=" + value +
                ", eventDate=" + eventDate +
                ", source='" + source + '\'' +
                '}';
    }
}
