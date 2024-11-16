package de.teampb.soco.llm.ollama4j.ui.app;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
@PWA(name = "Ollama UI", shortName = "Ollama UI")
@Theme(variant = Lumo.DARK)
@Push
public class AppShell implements AppShellConfigurator {
}
