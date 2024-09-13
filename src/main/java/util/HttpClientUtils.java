package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HttpClientUtils {

    private static final HttpClient client;
    private static final ObjectMapper objectMapper;

    private HttpClientUtils() {
    }

    static {
        client = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String sendGetRequest(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public static <T> List<T> deserializeJsonToList(String json, TypeReference<List<T>> type) throws JsonProcessingException {
        return objectMapper.readValue(json, type);
    }

    public static <T> T deserializeJsonToObject(String json, Class<T> type) throws JsonProcessingException {
        return objectMapper.readValue(json,type);
    }
}
