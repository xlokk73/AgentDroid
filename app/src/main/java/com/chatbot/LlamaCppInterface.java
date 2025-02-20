package com.chatbot;

public class LlamaCppInterface {
    static {
        System.loadLibrary("llama"); // Replace with the actual name of your .so file
    }

    // Declare native methods
    public native void initModel(String modelPath);
    public native String generateResponse(String input);
}