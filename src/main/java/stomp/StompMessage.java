package stomp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StompMessage {


    private final static byte DELIMITER = '\n';
    private final static byte NULL = 0;

    private final String messageType;
    private final String body;

    private final Map<String,List<String>> headers = new HashMap<>();

    public StompMessage(String messageType, String body) {
        this.messageType = messageType;
        this.body = body;
    }
    public StompMessage(String messageType) {
        this.messageType = messageType;
        this.body  = "";
    }

    public StompMessage(InputStream inputStream) throws IOException {
        String type = readUntil(inputStream, DELIMITER);

        if (type.equals("")) {
            this.messageType = "HEARTBEAT";
            this.body = "";
            return;
        }
        this.messageType = type;

        String header = "";

        while (!(header = readUntil(inputStream,DELIMITER)).equals("")){
            String[] tokens = header.split(":");
            addHeader(tokens[0],tokens[1]);
        }

        this.body = readUntil(inputStream,NULL);


    }

    private String readUntil(InputStream inputStream, byte t) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();


        int i = 0;

        while ((i = inputStream.read()) >= 0){

            byte b = (byte)i;

            if (b == t || b == NULL)
                return byteArrayOutputStream.toString(StandardCharsets.UTF_8);

            byteArrayOutputStream.write((byte)i);
        }

        throw new RuntimeException("Unexpected end of stream");

    }



    public void addHeader(String key, String value){
        if (!this.headers.containsKey(key))
            this.headers.put(key, new ArrayList<>());

        this.headers.get(key).add(value);

    }


    public void writeTo(OutputStream outputStream) throws IOException {

        outputStream.write(messageType.getBytes(StandardCharsets.UTF_8));
        outputStream.write(DELIMITER);

        for (String key : headers.keySet()){
            for (String value : headers.get(key)){
                outputStream.write(key.getBytes(StandardCharsets.UTF_8));
                outputStream.write(':');
                outputStream.write(value.getBytes(StandardCharsets.UTF_8));
                outputStream.write(DELIMITER);
            }
        }

        outputStream.write(DELIMITER);
        outputStream.write(body.getBytes(StandardCharsets.UTF_8));
        outputStream.write(NULL);
    }

    public String getBody() {
        return body;
    }

    public String getMessageType() {
        return messageType;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public String getFirstHeader(String s){
        if (headers.containsKey(s) && headers.get(s).size() >= 1)
            return headers.get(s).get(0);
        return null;
    }

    @Override
    public String toString() {
        return "StompMessage{" +
                "messageType='" + messageType + '\'' +
                ", headers=" + headers +
                '}';
    }
}
