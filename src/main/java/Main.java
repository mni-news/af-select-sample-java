import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.AuthRequest;
import dto.AuthResponse;
import dto.Observations;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import stomp.StompClient;
import stomp.StompMessage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getCanonicalName());


    private static final String AUTH_URL = "https://api.alphaflash.com/api/auth/alphaflash-client/token";
    private static final String REALTIME_HOST = "alphaflash01.chi0.mni-news.com";
    private static final int REALTIME_PORT= 61613;
    public static final String TOPIC_OBSERVATIONS = "/topic/observations";

    public static void main(String[] args) throws IOException {

        Properties properties = new Properties();
        properties.load(new FileReader("credentials.properties"));

        HttpClient httpClient = HttpClients.createDefault();

        ObjectMapper objectMapper = new ObjectMapper();

        HttpPost post = new HttpPost(AUTH_URL);
        post.setEntity(
                new StringEntity(
                        objectMapper.writeValueAsString(
                                new AuthRequest(properties.getProperty("username"),properties.getProperty("password"))
                        ),
                        ContentType.APPLICATION_JSON
                )
        );

        LOG.info("Fetching auth token");

        HttpResponse response = httpClient.execute(post);

        if (response.getStatusLine().getStatusCode() != 200){
            LOG.severe("Authentication failed");
            return;
        }

        AuthResponse authResponse = objectMapper.readValue(response.getEntity().getContent(), AuthResponse.class);

        LOG.info("Connecting to STOMP bus at: " + REALTIME_HOST);

        StompClient stompClient = new StompClient(REALTIME_HOST,REALTIME_PORT);
        stompClient.connect(authResponse.getAccessToken());

        LOG.info("Subscribing to: " + TOPIC_OBSERVATIONS);

        stompClient.subscribe(TOPIC_OBSERVATIONS);

        while (true){
            StompMessage stompMessage = stompClient.nextMessage();

            LOG.info("Received message: " + stompMessage);

            System.out.println("TYPE " + stompMessage.getMessageType());

            stompMessage.getHeaders().forEach((s, strings) -> {
                System.out.format("HEADER %s : %s\n",s,strings);
            });

            System.out.println("BODY " + stompMessage.getBody());

            if (TOPIC_OBSERVATIONS.equals(stompMessage.getFirstHeader("destination"))){
                Observations observations = objectMapper.readValue(stompMessage.getBody(),Observations.class);

                observations.forEach(System.out::println);
            }
        }

    }
}
