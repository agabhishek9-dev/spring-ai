package com.letslearn.springai.advisors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import reactor.core.publisher.Flux;

public class TokenPrintAdvisor implements CallAdvisor, StreamAdvisor {

    private static final Logger logger = LoggerFactory.getLogger(TokenPrintAdvisor.class);

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        logger.info("--- My token print advisor is called");
        logger.info("--- Request from TPA : "+chatClientRequest
                .prompt()
                .getContents()
        );
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        logger.info("--- Response recieved from LLM");
        logger.info("--- Response from TPA : "+ chatClientResponse
                .chatResponse()
                .getResult()
                .getOutput()
                .getText()
        );
        logger.info("--- Total token consumed "+chatClientResponse
                .chatResponse()
                .getMetadata()
                .getUsage()
                .getTotalTokens()
        );

        return null;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        Flux<ChatClientResponse> chatClientResponseFlux = streamAdvisorChain.nextStream(chatClientRequest);
        return chatClientResponseFlux;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
