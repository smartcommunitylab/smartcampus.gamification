package it.smartcommunitylab.gamification.log2elastic;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ESHelper {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final Logger logger = Logger.getLogger(ESHelper.class);
    static OkHttpClient client;
    static {
        client = new OkHttpClient();
    }

    private static final String ELASTIC_URL = "http://localhost:9200";
    private static final String INDEX_PREFIX = "gamification-stats-";

    private Config config;

    public ESHelper(Config config) {
        this.config = config;
    }

    private String pushToElastic(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.warn("Record not corrected pushed in elasticsearch: " + json);
            }
            String out = response.body().string();
            response.close();
            return out;
        }

    }

    private String pushToElastic(String url, Map<String, String> fields) throws IOException {
        String json = toJsonString(fields);
        return pushToElastic(url, json);
    }

    private String getIndexName(String gameId, long timestamp) {
        String indexSuffix = DateTimeFormatter.ofPattern("YYYY-MM-'w'w")
                .withZone(ZoneOffset.systemDefault()).format(Instant.ofEpochMilli(timestamp));
        return INDEX_PREFIX + gameId + "-" + indexSuffix;

    }

    public void saveRecord(RecordType recordType, String gameId, long creationTimestamp,
            Map<String, String> recordFields) throws IOException {
        String elasticResponse = "";
        if (config.pushToElastic7()) {
            elasticResponse = pushToElastic(
                    ELASTIC_URL + "/" + getIndexName(gameId, creationTimestamp) + "/" + "_doc",
                    recordFields);
        } else {
            elasticResponse =
                    pushToElastic(ELASTIC_URL + "/" + getIndexName(gameId, creationTimestamp) + "/"
                            + recordType.getRepresentation(), recordFields);
        }
        logger.info(elasticResponse);
    }

    private String toJsonString(Map<String, String> fields) {
        Gson gson = new Gson();
        String jsonContent = gson.toJson(fields);
        return jsonContent;
    }

}
