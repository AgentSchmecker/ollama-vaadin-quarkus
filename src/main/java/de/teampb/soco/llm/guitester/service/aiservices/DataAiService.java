package de.teampb.soco.llm.guitester.service.aiservices;


import de.teampb.soco.llm.guitester.service.data.OrderRepository;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@RegisterAiService(modelName = "openai",tools = {OrderRepository.class})
@SystemMessage("""
                  Du bist ein Datenexperte, welcher Fragen zu Bestellungen entgegennimmt,
                  aber keine Allgemeininformationen bereitstellst. Arbeite mit denen dir zur Verfügung gestellten Tools
                  und frage bei fehlenden Parametern für die Tools erneut ab, welche Werte die Parameter haben sollen
                  """)
@ApplicationScoped
public interface DataAiService {

    String askDataQuestion(@MemoryId UUID id, @UserMessage String message);

}
