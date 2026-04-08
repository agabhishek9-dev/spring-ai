package com.letslearn.springai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatAdvisorServiceImpl implements ChatAdvisorService {

    /*
     * ChatClient is the main Spring AI entry point used to build prompts,
     * send them to the LLM, and read the response.
     *
     * In this project, the ChatClient is already configured in AiConfig
     * with default advisors such as:
     * - SimpleLoggerAdvisor
     * - SafeGuardAdvisor
     *
     * So this service does not manually invoke advisors.
     * Advisors are applied automatically when .call() is executed. [web:48][web:64]
     */
    private final ChatClient chatClient;

    /*
     * External system prompt file.
     *
     * This file usually contains high-level behavior instructions,
     * for example:
     * - who the assistant is
     * - how it should respond
     * - tone or domain expertise
     *
     * Spring supports injecting prompt files as Resource objects,
     * and Spring AI can use them directly in prompt text methods. [web:14][web:32]
     */
    @Value("classpath:prompts/system-message.st")
    private Resource systemMessage;

    /*
     * External user prompt file.
     *
     * This file usually contains the main user-facing prompt template
     * with placeholders such as {concept}.
     */
    @Value("classpath:prompts/user-message.st")
    private Resource userMessage;

    public ChatAdvisorServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /*
     * --------------------------------------------------------------
     * CONCEPT 1: Advisor execution through ChatClient pipeline
     * --------------------------------------------------------------
     *
     * This method demonstrates that advisors are applied automatically
     * when a ChatClient request is executed.
     *
     * Flow:
     * 1. Build prompt using external system and user template files
     * 2. Fill placeholder values in the user template
     * 3. Execute the request using .call()
     * 4. Advisors intercept the request/response flow automatically
     * 5. Return the final text response
     *
     * Important learning point:
     * The advisor logic is not written inside this method.
     * It is attached earlier in AiConfig using defaultAdvisors(...). [web:48][web:64]
     *
     * Also note:
     * This method currently ignores the incoming query parameter and instead
     * hardcodes "java hack to cheat" into the prompt template.
     * That is useful for testing SafeGuardAdvisor because the configured
     * blocked words include "hack" and "cheat". SafeGuardAdvisor blocks the
     * call if sensitive words are present in the user input. [web:64][web:65]
     */
    @Override
    public String chatTemplate(String query) {

        return chatClient
                .prompt()   // Start building a new prompt request

                .system(system -> system
                        .text(this.systemMessage))
                /*
                 * Load the system prompt from external file.
                 *
                 * This becomes the instruction-level message that guides
                 * the model’s overall behavior.
                 */

                .user(user -> user
                        .text(this.userMessage)
                        .param("concept", "java"))
                /*
                 * Load the user prompt template from external file and replace
                 * the {concept} placeholder with a test value.
                 *
                 * The string "java hack to cheat" is intentionally sensitive
                 * for this advisor demo because it contains words blocked
                 * by SafeGuardAdvisor. [web:64][web:65]
                 */

                .call()
                /*
                 * Execute the chat request.
                 *
                 * This is the point where configured advisors become active:
                 * - logger advisor can log request/response
                 * - safeguard advisor can inspect and block sensitive input
                 */

                .content();
        /*
         * Return only the plain text response.
         *
         * If the request is blocked by SafeGuardAdvisor,
         * the returned content may be a safeguard message instead
         * of a model-generated answer. [web:64]
         */
    }

    @Override
    public Flux<String> streamChat(String query) {
        return chatClient
                .prompt()
                .system(system->system.text(systemMessage))
                .user(user->user.text(userMessage).param("concept", "Optional class"))
                .stream()
                .content();
    }
}