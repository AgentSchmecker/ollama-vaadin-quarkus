package de.teampb.soco.llm.ollama4j.ui.service;

import io.github.ollama4j.OllamaAPI;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class OllamaService {

    private OllamaAPI instance;

    @Inject
    @ConfigProperty(name="de.teampb.soco.llm.ollama4j.ui.ollama.url",defaultValue = "ö")
    private String url;
    @Inject @ConfigProperty(name="de.teampb.soco.llm.ollama4j.ui.ollama.defaultmodel",defaultValue = "llama2")
    private String model;
    @Inject @ConfigProperty(name="de.teampb.soco.llm.ollama4j.ui.ollama.timeout",defaultValue = "120")
    private  Integer requestTimeout;

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public int getRequestTimeout() {
        return requestTimeout;
    }
    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public OllamaAPI getOllamaAPIInstance(){
        if(instance == null) {
            instance = new OllamaAPI(url);
            instance.setRequestTimeoutSeconds(requestTimeout);
        }
        return instance;
    }

    public boolean getConnectionActive(){
        try {
            return getOllamaAPIInstance().ping();
        }
        catch (Exception e){
            return false;
        }
    }
}
