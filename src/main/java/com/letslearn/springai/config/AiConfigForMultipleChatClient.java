//package com.letslearn.springai.config;
//
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.openai.OpenAiChatModel;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class AiConfigForMultipleChatClient {


/*    This is to configure OpenAiChatModel type of ChatClient
    OpenAiChatModel is there in classpath since POM have OpenAiChatModel dependency */
//    @Bean(name="openAiChatClient")
//    public ChatClient openAiChatModel(OpenAiChatModel chatModel){
//        return ChatClient.builder(chatModel).build();
//    }

/*    This is to configure OllamaChatModel type of ChatClient
    OllamaChatModel not there in classpath since POM doesnt have Ollama dependency like OpenAIChatModel*/
//    @Bean(name="ollamaChatClient")
//    public ChatClient ollamaChatModel(OllamaChatModel chatModel){       // OllamaChatModel not
//        return ChatClient.builder(chatModel).build();
//    }
//}


/*
and in controller file, can use these beans

private ChatClient openAiChatClient;
private ChatClient ollamaChatClient;

        public ChatController(@Qualifier("openAiChatClient") ChatClient openAiChatClient,
                              @Qualifier("ollamaChatClient") ChatClient ollamaChatClient){
            this. openAiChatClient = openAiChatClient;
            this.ollamaChatClient = ollamaChatClient;
        }*/
