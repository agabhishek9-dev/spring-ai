package com.letslearn.springai.service;

import reactor.core.publisher.Flux;

public interface ChatAdvisorService {
    String chatTemplate(String query);

    Flux<String> streamChat(String query);
}
