package com.alphaflash.select.service;

import com.alphaflash.select.SelectConstants;
import com.alphaflash.select.dto.DataSeries;
import com.alphaflash.select.dto.Event;
import com.alphaflash.select.dto.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class SelectDataService {


    private final HttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final AtomicReference<String> accessToken;

    public SelectDataService(HttpClient httpClient, String accessToken) {
        this.httpClient = httpClient;
        this.accessToken = new AtomicReference<>(accessToken);
    }


    public Page<Event> getEventPageBetween(Date start, Date end, int page, int size) throws IOException {

        String format = String.format("%s?start=%s&end=%s&page=%s&size=%s",
                SelectConstants.EVENTS_URL, start.getTime(), end.getTime(), page, size);

        HttpGet request = getHttpGet(format);

        return objectMapper.readValue(
                httpClient.execute(request).getEntity().getContent(),
                new TypeReference<Page<Event>>() {}
        );
    }

    public Page<DataSeries> getDataSeriesPage(int page, int size) throws IOException {

        String format = String.format("%s?page=%s&size=%s",
                SelectConstants.DATA_SERIES_URL,  page, size);

        HttpGet request = getHttpGet(format);

        return objectMapper.readValue(
                httpClient.execute(request).getEntity().getContent(),
                new TypeReference<Page<DataSeries>>() {}
        );
    }

    private HttpGet getHttpGet(String format) {
        HttpGet calendarRequest = new HttpGet(format);
        calendarRequest.addHeader("Authorization", String.format("Bearer %s", accessToken.get()));
        return calendarRequest;
    }

    public Collection<Event> getAllEventsBetween(Date start, Date end) throws IOException {

        return depaginate(pageNumber -> getEventPageBetween(start,end,pageNumber, SelectConstants.PAGE_SIZE));
    }

    public Collection<DataSeries> getAllDataSeries() throws IOException {
        return depaginate(pageNumber -> getDataSeriesPage(pageNumber,SelectConstants.PAGE_SIZE));
    }

    public void setAccessToken(String s){
        accessToken.set(s);
    }


    interface PageSupplier<T> {
        Page<T> getPage(int pageNumber) throws IOException;
    }

    public <T>  Collection<T> depaginate(PageSupplier<T> pageSupplier) throws IOException {

        long pageCount = 1;
        ArrayList<T> result = new ArrayList<>();

        for (int i = 0; i < pageCount; i++){
            Page<T> currentPage = pageSupplier.getPage(i);

            System.out.println("getting page: " + i);
            pageCount = currentPage.getTotalPages();

            result.addAll(currentPage.getContent());
        }

        return result;
    }

}
