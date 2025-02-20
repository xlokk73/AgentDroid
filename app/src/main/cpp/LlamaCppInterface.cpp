#include <jni.h>
#include <string>
#include "llama.h" // Include the llama.cpp header

extern "C" JNIEXPORT void JNICALL
Java_com_chatbot_LlamaCppInterface_initModel(JNIEnv *env, jobject obj, jstring modelPath) {
    const char *model_path = env->GetStringUTFChars(modelPath, nullptr);
    // Initialize the llama model with the provided path
    llama_init(model_path);
    env->ReleaseStringUTFChars(modelPath, model_path);
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_chatbot_LlamaCppInterface_generateResponse(JNIEnv *env, jobject obj, jstring input) {
    const char *input_str = env->GetStringUTFChars(input, nullptr);
    // Generate a response using the llama model
    std::string response = llama_generate_response(input_str);
    env->ReleaseStringUTFChars(input, input_str);
    return env->NewStringUTF(response.c_str());
}