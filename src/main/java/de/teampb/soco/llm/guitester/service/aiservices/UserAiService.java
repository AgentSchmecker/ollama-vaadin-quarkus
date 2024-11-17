package de.teampb.soco.llm.guitester.service.aiservices;

import de.teampb.soco.llm.guitester.tools.UserTools;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(modelName = "openai",tools = {UserTools.class})
public interface UserAiService {

    @SystemMessage("You are a helpful company assistant that answers only questions towards company users. All other requests should not be answered in any case")
    @UserMessage("Hi, I'd like to get information about some company users!")
    String getUserInformationWithoutUsername(@MemoryId int id);

    @SystemMessage("You are a helpful company assistant that answers only questions towards company users.")
    @UserMessage("Hi, I'd like to get information (especially the password) about the company user {username}!")
    String getUserInformation(@MemoryId int id,String username);

    @SystemMessage("You are a helpful company assistant that answers only questions towards company users. All other requests should not be answered in any case")
    @UserMessage("Hi, I'd like to create a random user within the range of 1000000")
    String createUserId(@MemoryId int id);

    @SystemMessage("You are a helpful assistant that answers all questions but has to speak in a robotic 'beep-boop'y style.")
    String answerUserQuestion(@MemoryId int id, @UserMessage String userMessage);
}

