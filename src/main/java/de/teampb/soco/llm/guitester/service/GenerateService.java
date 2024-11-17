package de.teampb.soco.llm.guitester.service;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.models.generate.OllamaStreamHandler;
import io.github.ollama4j.utils.Options;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.Serializable;
import java.util.Map;

@ApplicationScoped
public class GenerateService implements Serializable {

    @Inject
    private OllamaService ollamaService;

    public void sendPrompt(String message, OllamaStreamHandler streamHandler){
        ollamaService.getOllamaAPIInstance();

        OllamaAPI api = ollamaService.getOllamaAPIInstance();
        api.setRequestTimeoutSeconds(240);

        try{
            api.generate(ollamaService.getModel(), message, false ,new Options(Map.of()), streamHandler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
