package com.alphaflash.select.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Topic {
    private String name;
    private String qcode;
    private String broader;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQcode() {
        return qcode;
    }

    public void setQcode(String qcode) {
        this.qcode = qcode;
    }

    public String getBroader() {
        return broader;
    }

    public void setBroader(String broader) {
        this.broader = broader;
    }
}
