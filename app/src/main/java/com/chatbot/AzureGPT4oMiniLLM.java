package com.chatbot;

import android.content.Context;
import android.util.Log;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

// TODO: Add prompt to LLM

public class AzureGPT4oMiniLLM implements LLMInterface {
    private static final String TAG = "AzureGPT4oMiniLLM";
    private static final String URI = "https://gen-openai-demo-sweeden.openai.azure.com/openai/deployments/gpt-4o-mini/chat/completions?api-version=2024-08-01-preview";
    private static final String API_KEY_FILE_PATH = "api_key.txt";

    private final OkHttpClient client;
    private final String apiKey;

    public AzureGPT4oMiniLLM(Context context) throws IOException {
        this.client = new OkHttpClient();
        this.apiKey = loadApiKeyFromAssets(context);
        Log.d(TAG, "Loaded API Key: " + apiKey);
    }

    private String loadApiKeyFromAssets(Context context) throws IOException {
        try (InputStream is = context.getAssets().open(API_KEY_FILE_PATH)) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            return new String(buffer, StandardCharsets.UTF_8).trim();
        }
    }

    @Override
    public String generateResponse(String input) throws JSONException {
        Log.d(TAG, "Generating response for input: " + input);
        JSONObject json = new JSONObject();
        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "user").put("content", input));
        json.put("messages", messages);
        json.put("temperature", 0.7);
        json.put("top_p", 0.95);
        json.put("max_tokens", 800);

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(URI)
                .addHeader("Content-Type", "application/json")
                .addHeader("api-key", apiKey)
                .post(body)
                .build();

        Log.d(TAG, "Request: " + request.toString());
        Log.d(TAG, "Request Body: " + json.toString());

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                Log.e(TAG, "Unexpected code " + response);
                throw new IOException("Unexpected code " + response);
            }

            JSONObject responseBody = new JSONObject(response.body().string());
            String llmResponse = responseBody.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
            Log.d(TAG, "LLM Response: " + llmResponse);
            return llmResponse;
        } catch (IOException e) {
            Log.e(TAG, "Error during LLM response generation", e);
            return "Error: " + e.getMessage();
        } catch (JSONException e) {
            Log.e(TAG, "JSON Error during LLM response generation", e);
            throw new RuntimeException(e);
        }
    }
}