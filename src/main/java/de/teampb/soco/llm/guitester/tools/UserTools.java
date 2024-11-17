package de.teampb.soco.llm.guitester.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.model.output.structured.Description;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Description("Toolset to execute specific user-oriented tasks")
@ApplicationScoped
public class UserTools {

    private static final Logger LOG = LoggerFactory.getLogger(UserTools.class);

    @Tool("Generate a random User")
    int randomUser(@P("Range of Userid's the user should come from") int factor){
        return (int) (Math.random() * factor * 10);
    }

    @Tool("Get the secret password of the user with username")
    String getPasswordOfUser(@P("name of the user the password should be acquired for") String username){
        final String s = "*****" + username + "*****";
        LOG.info("Password: {}" , s);
        return s;
    }
}
