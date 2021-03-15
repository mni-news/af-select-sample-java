package com.alphaflash.select;

import com.alphaflash.select.service.AuthService;
import com.alphaflash.select.service.SelectDataService;
import com.alphaflash.select.service.SelectRealTimeDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.alphaflash.select.dto.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import com.alphaflash.select.stomp.StompClient;
import com.alphaflash.select.stomp.StompMessage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getCanonicalName());


    private static final String AUTH_URL = "https://api.alphaflash.com/api/auth/alphaflash-client/token";
    private static final String EVENTS_URL = "https://api.alphaflash.com/api/select/calendar/events";
    private static final String REALTIME_HOST = "select.alphaflash.com";
    private static final int REALTIME_PORT= 61613;
    public static final String TOPIC_OBSERVATIONS = "/topic/observations";

    public static void main(String[] args) throws IOException {

        Properties properties = new Properties();
        properties.load(new FileReader("credentials.properties"));

        HttpClient httpClient = HttpClients.createDefault();

        AuthService authService = new AuthService(httpClient);

        AuthResponse authResponse = authService.authenticate(
                properties.getProperty("username"),
                properties.getProperty("password")
        );

        SelectDataService selectDataService = new SelectDataService(httpClient,authResponse.getAccessToken());


        selectDataService.getAllDataSeries().forEach(dataSeries -> {
            System.out.println(dataSeries.getId() + " " + dataSeries.getDisplay());
        });

        selectDataService.getAllEventsBetween(
                new Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000L),
                new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L)
        ).forEach(event -> {
            System.out.println(event.getDate() + " | " + event.getCountry() + " | " + event.getTitle());
        });


        SelectRealTimeDataService realTimeDataService = new SelectRealTimeDataService(
                authResponse.getAccessToken(),
                observations -> {
                    System.out.println("Received observations: " + observations);
                }
        );

        new Thread(realTimeDataService).start();

        System.out.println("Waiting for observations, press enter to exit");
        System.in.read();

        System.out.println("Shutting down");

        realTimeDataService.stop();



    }
}
