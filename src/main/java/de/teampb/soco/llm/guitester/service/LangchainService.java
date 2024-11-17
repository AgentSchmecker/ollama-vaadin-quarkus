package de.teampb.soco.llm.guitester.service;

import de.teampb.soco.llm.guitester.service.aiservices.DataAiService;
import de.teampb.soco.llm.guitester.service.aiservices.UserAiService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.openai.OpenAiImageModelName;
import dev.langchain4j.model.output.Response;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;
import java.util.UUID;


@ApplicationScoped
public class LangchainService {

    @Inject @ConfigProperty(name = "de.teampb.soco.llm.guitester.openai.key") String openaiKey;

    @Inject
    UserAiService userAiService;

    @Inject
    DataAiService dataAiService;

    public URI generateImageToPrompt(String prompt){
        final OpenAiImageModel model = OpenAiImageModel.builder()
                .apiKey(openaiKey).modelName(OpenAiImageModelName.DALL_E_3)
                .style("natural")
                .quality("standard")
                .build();
        final Response<Image> generate = model.generate(prompt);
        return generate.content().url();
    }

    @ActivateRequestContext
    public String requestWithTools(String prompt) {
        if(prompt.contains("Markus")) {
            return userAiService.getUserInformation(1, prompt);
        }
        else if(prompt.contains("create")){
            return userAiService.createUserId(1);
        }
        else if(prompt.contains("DOAG")){
            return userAiService.answerUserQuestion(1,prompt);
        }
        else{
            return userAiService.getUserInformationWithoutUsername(1);
        }
    }

    @ActivateRequestContext
    public String requestDataWithTools(int memoryId,String prompt){
       return dataAiService.askDataQuestion(UUID.nameUUIDFromBytes(String.valueOf(memoryId).getBytes()),prompt);
    }


}
