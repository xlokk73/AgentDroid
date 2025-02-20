package com.chatbot;

import org.json.JSONException;

public class LocalLlamaLLM implements LLMInterface {
    private final LlamaCppInterface llamaCpp;

    public LocalLlamaLLM(String modelPath) {
        llamaCpp = new LlamaCppInterface();
        llamaCpp.initModel(modelPath);
    }

    @Override
    public String generateResponse(String input) throws JSONException {
        return llamaCpp.generateResponse(input);
    }
}