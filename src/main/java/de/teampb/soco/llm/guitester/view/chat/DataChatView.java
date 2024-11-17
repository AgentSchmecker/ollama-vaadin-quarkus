package de.teampb.soco.llm.guitester.view.chat;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.teampb.soco.llm.guitester.entities.Order;
import de.teampb.soco.llm.guitester.service.LangchainService;
import de.teampb.soco.llm.guitester.service.ModelService;
import de.teampb.soco.llm.guitester.service.aiservices.DataAiService;
import de.teampb.soco.llm.guitester.service.data.OrderRepository;
import de.teampb.soco.llm.guitester.template.MainLayout;
import io.github.ollama4j.models.chat.OllamaChatMessageRole;
import io.github.ollama4j.models.response.Model;
import io.github.ollama4j.models.response.ModelDetail;
import io.quarkus.panache.common.Sort;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Route(value = "chat/data", layout = MainLayout.class)
@PageTitle("Chat about Data")
public class DataChatView extends VerticalLayout {

    private static final Logger LOG = LoggerFactory.getLogger(DataChatView.class);

    private Grid<Order> grid = new Grid<>(Order.class, false);

    private TextField memoryId = new TextField("Memory ID","1","Memory-ID für den Chatverlauf");

    private MessageList chat;
    private MessageInput input;
    private List<MessageListItem> chatEntries  = new ArrayList<>();

    @Inject
    private OrderRepository orderRepository;

    @Inject
    LangchainService langchainService;

    @PostConstruct
    public void init() {
        H2 header = new H2("Bestellliste");
        grid.addColumn(Order::getOrderId).setHeader("Order ID");
        grid.addColumn(Order::getOrderStart).setHeader("Bestelldatum");
        grid.addColumn(Order::getCustomerId).setHeader("Kundennummer");
        grid.addColumn(Order::getStatus).setHeader("Bestellstatus");
        grid.addColumn(Order::getOpenItemQty).setHeader("Offene Positionen");
        grid.addColumn(Order::getOrderShippingDate).setHeader("Lieferdatum");

        grid.setItems(orderRepository.listAll(Sort.by("customerId")));

        grid.setWidthFull();

        // chat
        chat = new MessageList();
        input = new MessageInput();
        chat.setItems(new MessageListItem[0]);
        input.addSubmitListener(this::onSubmit);

        chat.setSizeFull(); // Chat takes most of the space
        input.setWidthFull(); // Full width only
        chat.setMaxWidth("1200px"); // Limit the width
        input.setMaxWidth("1200px");

        this.add(header,memoryId, grid, chat, input);
        this.setHorizontalComponentAlignment(Alignment.CENTER,
                chat, input);
        this.setPadding(true); // Leave some white space
        this.setHeightFull(); // We maximize to window
    }

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
        Thread t = new Thread(()-> {
            final String response = langchainService.requestDataWithTools(Integer.parseInt(memoryId.getValue()), submitEvent.getValue());
            getUI().ifPresent(ui -> ui.access(
                                () -> {
                                    answer.setText(response);
                                }
                        ));
        });
        t.start();
    }
}
