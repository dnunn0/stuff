package com.whatgameapps.firefly.com.whatgameapps.firefly.helper;

import com.google.gson.Gson;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class HttpUtils {
    private static Gson gson = new Gson();

    public static void postObjectAsJson(String url, Object object) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createMinimal()) {
            StringEntity myEntity = new StringEntity(gson.toJson(object), ContentType.APPLICATION_JSON);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(myEntity);
            httpClient.execute(httpPost);
        }
    }
}
