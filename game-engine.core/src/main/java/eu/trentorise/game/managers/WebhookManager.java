package eu.trentorise.game.managers;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.repo.NotificationPersistence;
import okhttp3.*;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;

@Component
public class WebhookManager {
    private OkHttpClient client;
    
    private Boolean webHookEnabled;

    private String webHookEndpoint;

    private String webHookSecretKey;
    private ObjectMapper mapper = new ObjectMapper();
    
    @Autowired
    private Environment env;

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(WebhookManager.class);
    private boolean initialized = false;

    @PostConstruct
    private synchronized void init() {
        try {
            webHookEnabled = Boolean.parseBoolean(env.getProperty("webhook.enabled"));
            if (webHookEnabled) {
                if (this.client == null) {
                    this.client = new OkHttpClient();

                }
                this.webHookEndpoint = env.getProperty("webhook.endpoint");
                this.webHookSecretKey = env.getProperty("webhook.secretKey");

                logger.info("Connected to webhook endpoint: " + this.webHookEndpoint);
                initialized = true;
            }
        } catch(Exception e) {
            logger.error("Problems on webhook settings: " + e.getMessage());
        }
    }

    public void sendMessage(NotificationPersistence notification) {
        try {
            if (!this.webHookEnabled) {
                return;
            }
            if (!initialized) {
                init();
                if (!initialized) {
                    return;
                }
            }
            final MediaType JSON
                    = MediaType.parse("application/json; charset=utf-8");
            String message = mapper.writeValueAsString(notification);
            RequestBody body = RequestBody.create(JSON, message);
            String webHookToken = this.buildWebHookToken(message);
            if (webHookToken.isEmpty()) {
                return;
            }
            Request request = new Request.Builder()
                    .header("Authorization", webHookToken)
                    .url(this.webHookEndpoint)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    logger.error("Error sending message to webhook.", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        } else {
                            logger.debug("Successful send notification messages");
                        }
                    } finally {
                        response.close();
                    }
                }
            });
        } catch (Exception e) {
             logger.error("Error building message for webhook.", e);
        }
    }

    private String buildWebHookToken(String message) {
        String hash = "";
        try {

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(this.webHookSecretKey.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));

        } catch (Exception e){
            logger.error("Error building token for webhook.", e);
        } finally {
            return hash;
        }
    }
}
