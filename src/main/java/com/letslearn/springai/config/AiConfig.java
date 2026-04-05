package com.letslearn.springai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AiConfig {

    /**
     * Creates and configures a reusable ChatClient bean for the application.
     *
     * This bean centralizes all common AI-related configuration such as:
     * - advisors
     * - default system prompt
     * - default model options
     *
     * Why this is useful:
     * Instead of repeating the same setup in multiple services,
     * we configure one shared ChatClient and inject it wherever needed.
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {

        return builder

                /*
                 * ----------------------------------------------------------
                 * CONCEPT 1: defaultAdvisors(...)
                 * ----------------------------------------------------------
                 *
                 * Advisors work like interceptors in the ChatClient pipeline.
                 * They can inspect, log, modify, enrich, or even block requests
                 * before or after the model call.
                 *
                 * Think of them like AOP-style helpers for AI request flow.
                 * Spring AI documents advisors as a way to intercept and enhance
                 * interactions around prompt execution. [web:48][web:49][web:53]
                 */
                .defaultAdvisors(

                        /*
                         * SimpleLoggerAdvisor
                         *
                         * Purpose:
                         * Logs the request and response flowing through ChatClient.
                         *
                         * Why useful:
                         * - helps during debugging
                         * - helps understand what prompt is actually being sent
                         * - helps inspect what response is coming back
                         *
                         * This is especially helpful while learning Spring AI
                         * because you can observe request/response behavior. [web:48][web:53]
                         */
                        new SimpleLoggerAdvisor(),

                        /*
                         * SafeGuardAdvisor
                         *
                         * Purpose:
                         * Prevents model execution if the user input contains
                         * restricted or unsafe keywords.
                         *
                         * In this example, the restricted words are:
                         * - cheat
                         * - fraud
                         * - hack
                         *
                         * If one of these appears in the prompt, the advisor can
                         * stop the request before it reaches the LLM. [web:50][web:56]
                         *
                         * Why useful:
                         * - adds a basic safety layer
                         * - helps enforce input restrictions
                         * - demonstrates that advisors can control behavior,
                         *   not just observe it
                         */
                        new SafeGuardAdvisor(List.of("cheat", "fraud", "hack"))
                )

                /*
                 * ----------------------------------------------------------
                 * CONCEPT 2: defaultSystem(...)
                 * ----------------------------------------------------------
                 *
                 * Sets a default system message for all requests made using
                 * this ChatClient bean.
                 *
                 * A system prompt defines the model’s general behavior,
                 * personality, or instruction context.
                 *
                 * Example meaning here:
                 * "You are helpful assistant"
                 * -> the model should respond in a helpful way by default
                 *
                 * This system instruction is applied automatically unless
                 * overridden for a specific request.
                 */
                .defaultSystem("You are helpful assistant")

                /*
                 * ----------------------------------------------------------
                 * CONCEPT 3: defaultOptions(...)
                 * ----------------------------------------------------------
                 *
                 * Defines default model-specific settings for all requests
                 * sent through this ChatClient.
                 *
                 * These options apply globally unless a request overrides them.
                 */
                .defaultOptions(
                        OpenAiChatOptions.builder()

                                /*
                                 * model(...)
                                 *
                                 * Selects the OpenAI chat model to use.
                                 *
                                 * Here:
                                 * gpt-4o-mini
                                 *
                                 * This controls which underlying LLM processes
                                 * the prompt.
                                 */
                                .model("gpt-4o-mini")

                                /*
                                 * temperature(...)
                                 *
                                 * Controls randomness in the generated output.
                                 *
                                 * Lower temperature:
                                 * - more focused
                                 * - more deterministic
                                 * - better for factual or predictable answers
                                 *
                                 * Higher temperature:
                                 * - more creative
                                 * - more varied
                                 * - better for brainstorming or open-ended text
                                 */
                                .temperature(0.3)

                                /*
                                 * maxTokens(...)
                                 *
                                 * Limits how long the generated response can be.
                                 *
                                 * Why useful:
                                 * - controls response size
                                 * - controls API cost
                                 * - prevents overly long output
                                 */
                                .maxTokens(200)

                                // Finish building OpenAI chat options.
                                .build()
                )

                /*
                 * build()
                 *
                 * Final step that creates the ChatClient instance
                 * from all the configuration defined above.
                 */
                .build();
    }
}