package de.teampb.soco.llm.guitester.view.generate;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.StreamResource;
import de.teampb.soco.llm.guitester.service.LangchainService;
import de.teampb.soco.llm.guitester.template.MainLayout;
import jakarta.inject.Inject;

import java.io.ByteArrayInputStream;
import java.net.URI;

@Route(value = "generate/image", layout = MainLayout.class)
@PageTitle("Generate Image")
public class GenerateImageView extends VerticalLayout {

    @Inject
    private LangchainService langchainService;
    private TextArea questionTextField = new TextArea();
    private TextArea sentQuestionTextArea = new TextArea();
    private Image answerImage = new Image();

    public GenerateImageView() {
        H2 header = new H2("Generate Image sample (OpenAI - DALL-E 3)");

        questionTextField.setWidthFull();
        questionTextField.setLabel("Image description");
        questionTextField.setPlaceholder("Please enter a description for the image to be generated");

        sentQuestionTextArea.setWidthFull();
        sentQuestionTextArea.setVisible(false);
        sentQuestionTextArea.setLabel("Sent question");
        sentQuestionTextArea.setReadOnly(true);

        answerImage.setWidthFull();
        answerImage.setVisible(false);

        Button askButton = new Button("Ask model", e -> sendQuestion());
        askButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(header, questionTextField, askButton,sentQuestionTextArea, answerImage);
    }

    private void sendQuestion() {
        sentQuestionTextArea.setVisible(true);
        answerImage.setVisible(true);
        sentQuestionTextArea.setValue(questionTextField.getValue());
        questionTextField.clear();
        final URI url = langchainService.generateImageToPrompt(sentQuestionTextArea.getValue());
        answerImage.setSrc(url.toASCIIString());
    }

    private AbstractStreamResource createStreamResource(byte[] imageBytes) {
        return new StreamResource("genImage.jpg",()-> new ByteArrayInputStream(imageBytes));
    }

}