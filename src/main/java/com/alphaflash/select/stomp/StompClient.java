package com.alphaflash.select.stomp;

import com.alphaflash.select.Main;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class StompClient {

    private static final Logger LOG = Logger.getLogger(Main.class.getCanonicalName());

    private final String host;
    private final int port;

    private Socket socket;

    public StompClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public StompMessage connect(String passcode) throws IOException {
        socket = new Socket(host,port);

        StompMessage connectMessage = new StompMessage("CONNECT");
        connectMessage.addHeader("passcode",passcode);
        connectMessage.addHeader("heart-beat","0,30000");
        connectMessage.writeTo(socket.getOutputStream());

        StompMessage response = new StompMessage(socket.getInputStream());

        if (!response.getMessageType().equals("CONNECTED"))
            throw new RuntimeException("Unexpected response: " + response.getMessageType());

        return response;
    }

    public void subscribe(String destination) throws IOException {
        StompMessage message = new StompMessage("SUBSCRIBE");
        message.addHeader("destination",destination);

        message.writeTo(socket.getOutputStream());

    }

    public StompMessage nextMessage() throws IOException {
        return new StompMessage(socket.getInputStream());
    }

    public void shutdown(){
        try {
            socket.close();
        }catch (Exception e){
            LOG.warning("Error closing socket: " + e.getMessage());
        }
    }


}
