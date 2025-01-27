package de.teampb.soco.llm.guitester.service;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.models.chat.*;
import io.github.ollama4j.models.generate.OllamaStreamHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ChatService implements Serializable {

    private List<OllamaChatMessage> messages = new ArrayList<>();

    @Inject
    private OllamaService ollamaService;

    public String sendChat(String message, OllamaStreamHandler streamHandler){
        OllamaAPI api = ollamaService.getOllamaAPIInstance();
        OllamaChatRequestBuilder builder = OllamaChatRequestBuilder.getInstance(ollamaService.getModel());

        OllamaChatRequest ollamaChatRequestModel = builder.withMessages(messages).withMessage(OllamaChatMessageRole.USER, message).build();

        return callOllamaChat(streamHandler, api, ollamaChatRequestModel);
    }


    public String sendChatWithImages(String message, List<byte[]> images, OllamaStreamHandler streamHandler){
        OllamaAPI api = ollamaService.getOllamaAPIInstance();

        OllamaChatRequestBuilder builder = OllamaChatRequestBuilder.getInstance(ollamaService.getModel());

        OllamaChatRequest ollamaChatRequestModel = builder.withMessages(messages).build();

        OllamaChatMessage userMessage = new OllamaChatMessage(OllamaChatMessageRole.USER, message,images);

        ollamaChatRequestModel.getMessages().add(userMessage);

        return callOllamaChat(streamHandler, api, ollamaChatRequestModel);
    }




    private String callOllamaChat(OllamaStreamHandler streamHandler, OllamaAPI api,
                                  OllamaChatRequest ollamaChatRequestModel) {
        try {
            OllamaChatResult chat = api.chat(ollamaChatRequestModel,streamHandler);
            messages = chat.getChatHistory();
            return chat.getResponse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}