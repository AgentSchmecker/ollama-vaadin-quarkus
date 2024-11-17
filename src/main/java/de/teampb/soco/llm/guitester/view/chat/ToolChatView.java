package de.teampb.soco.llm.guitester.view.chat;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.teampb.soco.llm.guitester.service.ChatService;
import de.teampb.soco.llm.guitester.service.LangchainService;
import de.teampb.soco.llm.guitester.template.MainLayout;
import de.teampb.soco.llm.guitester.view.generate.SimpleGenerateView;
import io.github.ollama4j.models.chat.OllamaChatMessageRole;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * The main view contains a text field for getting the user name and a button
 * that shows a greeting message in a notification.
 */
@Route(value = "chat/tools",layout = MainLayout.class)
@PageTitle("Tool chat")
public class ToolChatView extends VerticalLayout {
    private static final Logger LOG = LoggerFactory.getLogger(ToolChatView.class);

    private MessageList chat;
    private MessageInput input;
    private List<MessageListItem> chatEntries  = new ArrayList<>();

    @Inject
    private LangchainService langchainService;

    public ToolChatView() {

        H2 header = new H2("Tool chat sample");

        chat = new MessageList();
        input = new MessageInput();
        chat.setItems(new MessageListItem[0]);
        add(header, chat, input);
        input.addSubmitListener(this::onSubmit);
        this.setHorizontalComponentAlignment(Alignment.CENTER,
                chat, input);
        this.setPadding(true); // Leave some white space
        this.setHeightFull(); // We maximize to window
        chat.setSizeFull(); // Chat takes most of the space
        input.setWidthFull(); // Full width only
        chat.setMaxWidth("1200px"); // Limit the width
        input.setMaxWidth("1200px");
    }

    @ActivateRequestContext
    void onSubmit(MessageInput.SubmitEvent submitEvent) {
        MessageListItem question = new MessageListItem(submitEvent.getValue(), Instant.now(), OllamaChatMessageRole.USER.getRoleName());
        question.setUserAbbreviation("US");
        question.setUserColorIndex(1);
        chatEntries.add(question);
        MessageListItem answer = new MessageListItem("Thinking.....", Instant.now(), OllamaChatMessageRole.ASSISTANT.getRoleName());
        chatEntries.add(answer);
        answer.setUserAbbreviation("AS");
        answer.setUserColorIndex(2);
        chat.setItems(chatEntries);
        Thread t = new Thread(() -> {
            String res = langchainService.requestWithTools(submitEvent.getValue());
            LOG.info("Tool-Result: {}" , res);
            getUI().ifPresent(ui -> ui.access(
                            () -> {
                                answer.setText(res);
                            }));
        });
        t.start();

    }

}