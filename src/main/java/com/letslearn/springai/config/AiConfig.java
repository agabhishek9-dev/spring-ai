package com.letslearn.springai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    /**
     * Creates a reusable ChatClient bean with default settings.
     *
     * These defaults are applied to all requests made through this client
     * unless overridden at request level.
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                /*
                 * defaultSystem(...)
                 * Sets a default system instruction for every request.
                 *
                 * This is like a base behavior rule for the model.
                 * It tells the model how to behave globally.
                 */
                .defaultSystem("You are helpful assistant")

                /*
                 * defaultOptions(...)
                 * Sets model-specific configuration.
                 */
                .defaultOptions(OpenAiChatOptions.builder()
                        /*
                         * model(...)
                         * Selects which OpenAI model should handle requests.
                         */
                        .model("gpt-4o-mini")

                        /*
                         * temperature(...)
                         * Lower value -> more predictable output
                         * Higher value -> more creative/random output
                         */
                        .temperature(0.3)

                        /*
                         * maxTokens(...)
                         * Limits the maximum length of generated output.
                         */
                        .maxTokens(200)
                        .build())

                // Finish builder and create ChatClient bean.
                .build();
    }
}