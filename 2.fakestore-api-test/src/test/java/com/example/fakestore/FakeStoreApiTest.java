package com.example.fakestore;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FakeStoreApiTest {
    private static final String API_URL = "https://fakestoreapi.com/products";
    private final OkHttpClient client = new OkHttpClient();

    @Test
    void testFakeStoreApiData() throws IOException {
        Request request = new Request.Builder().url(API_URL).build();
        try (Response response = client.newCall(request).execute()) {
            // 1. Verify server response code
            Assertions.assertEquals(200, response.code(), "Expected HTTP 200 response");

            String body = response.body().string();
            JSONArray products = new JSONArray(body);
            List<JSONObject> defectiveProducts = new ArrayList<>();

            for (int i = 0; i < products.length(); i++) {
                JSONObject product = products.getJSONObject(i);
                boolean hasDefect = false;
                StringBuilder defectReason = new StringBuilder();

                // Check title
                if (!product.has("title") || product.getString("title").trim().isEmpty()) {
                    hasDefect = true;
                    defectReason.append("Missing or empty title. ");
                }
                // Check price
                if (!product.has("price") || product.getDouble("price") < 0) {
                    hasDefect = true;
                    defectReason.append("Negative or missing price. ");
                }
                // Check rating.rate
                if (!product.has("rating") || !product.getJSONObject("rating").has("rate") || product.getJSONObject("rating").getDouble("rate") > 5) {
                    hasDefect = true;
                    defectReason.append("Missing or invalid rating.rate. ");
                }

                if (hasDefect) {
                    product.put("defectReason", defectReason.toString());
                    defectiveProducts.add(product);
                }
            }

            // Print defective products (for demonstration)
            if (!defectiveProducts.isEmpty()) {
                System.out.println("Defective products:");
                for (JSONObject defective : defectiveProducts) {
                    System.out.println(defective.toString(2));
                }
            }

            // Assert no defects (optional, comment out if you just want to list)
            // Assertions.assertTrue(defectiveProducts.isEmpty(), "There are defective products in the API response");
        }
    }
} 
