package com.chatbot;

import org.json.JSONException;

public interface LLMInterface {
    String generateResponse(String input) throws JSONException;
}
