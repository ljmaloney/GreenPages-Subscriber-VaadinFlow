package com.green.yp.subscriber.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;

@Route("about")
@Slf4j
@AnonymousAllowed
@PermitAll
public class AboutUsPageView extends AbstractVerticalLayout {

    public AboutUsPageView() {
        super();
    }

    @Override
    protected String getHeaderText() {
        return "About Us";
    }

    @Override
    public void initializePage(Div pageConentsDiv) {
        Paragraph paragraph = new Paragraph();
        add(paragraph);

        Button button = new Button("Do not press this button");
        add(button);
    }
}
