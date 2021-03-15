package com.alphaflash.select.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;
import java.util.Date;

/**
 *
 *
 * Example JSON:
 * {
 *     "id": "67956",
 *     "dataReleaseId": 44,
 *     "date": "2020-12-30T14:45:00Z",
 *     "country": "US",
 *     "title": "MNI Chicago PMI",
 *     "rating": 2,
 *     "reportingPeriod": "Dec",
 *     "dataSeriesEntries": []
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

    private String id;
    private long dataReleaseId;
    private Date date;
    private String country;
    private String title;
    private int rating;
    private String reportingPeriod;
    private Collection<DataSeriesEntry> dataSeriesEntries;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDataReleaseId() {
        return dataReleaseId;
    }

    public void setDataReleaseId(long dataReleaseId) {
        this.dataReleaseId = dataReleaseId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReportingPeriod() {
        return reportingPeriod;
    }

    public void setReportingPeriod(String reportingPeriod) {
        this.reportingPeriod = reportingPeriod;
    }

    public Collection<DataSeriesEntry> getDataSeriesEntries() {
        return dataSeriesEntries;
    }

    public void setDataSeriesEntries(Collection<DataSeriesEntry> dataSeriesEntries) {
        this.dataSeriesEntries = dataSeriesEntries;
    }
}
