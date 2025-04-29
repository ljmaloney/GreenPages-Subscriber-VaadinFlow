package com.green.yp.subscriber.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;

@Route("")
@Slf4j
@AnonymousAllowed
@PermitAll
public class SubscriberHomePageView extends AbstractVerticalLayout {
    public SubscriberHomePageView() {
        super();
    }

    @Override
    protected String getHeaderText() {
        return "";
    }

    @Override
    public void initializePage(Div pageContentsDiv) {

        Paragraph paragraph = new Paragraph();
        add(paragraph);
        log.debug("Created home page view");
    }
}
