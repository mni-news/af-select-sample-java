package com.alphaflash.select.service;

import com.alphaflash.select.Main;
import com.alphaflash.select.SelectConstants;
import com.alphaflash.select.dto.Observation;
import com.alphaflash.select.stomp.StompClient;
import com.alphaflash.select.stomp.StompMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class SelectRealTimeDataService implements Runnable{


    private static final Logger LOG = Logger.getLogger(Main.class.getCanonicalName());
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final StompClient stompClient = new StompClient(
            SelectConstants.REALTIME_HOST,SelectConstants.REALTIME_PORT
    );

    private final AtomicReference<String> accessToken;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final ObservationHandler observationHandler;

    public SelectRealTimeDataService(String accessToken, ObservationHandler observationHandler) {
        this.accessToken = new AtomicReference<>(accessToken);
        this.observationHandler = observationHandler;
    }

    @Override
    public void run() {

        LOG.info("Connecting to STOMP bus at: " + SelectConstants.REALTIME_HOST);

        try {

            StompMessage connectionResponse = stompClient.connect(accessToken.get());

            LOG.info("Connection result: " + connectionResponse);
            LOG.info("Subscribing to: " + SelectConstants.TOPIC_OBSERVATIONS);

            stompClient.subscribe(SelectConstants.TOPIC_OBSERVATIONS);

            while (running.get()) {
                StompMessage stompMessage = stompClient.nextMessage();

                LOG.info("Received message: " + stompMessage);

                if (SelectConstants.TOPIC_OBSERVATIONS.equals(stompMessage.getFirstHeader("destination"))) {
                    Collection<Observation> observations = objectMapper.readValue(
                            stompMessage.getBody(), new TypeReference<Collection<Observation>>() {}
                            );
                    observationHandler.handleObservations(observations);
                }
            }

        }catch (Exception e){

            stompClient.shutdown();

            if (running.get()) {
                LOG.severe("Stomp connection closed, reconnecting in 10s");
                try {
                    Thread.sleep(10_000L);
                } catch (InterruptedException ie) {
                    LOG.warning("Interrupted while sleeping");
                }
            }
        }

    }

    public void setAccessToken(String s){
        accessToken.set(s);
    }

    public void stop(){
        running.set(false);
        stompClient.shutdown();
    }
}
