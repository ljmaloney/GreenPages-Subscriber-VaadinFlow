package com.green.yp.subscriber.ui.account;

import com.green.yp.subscriber.ui.AbstractVerticalLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;

@Route("/register/confirmation")
@Slf4j
@AnonymousAllowed
@PermitAll
public class RegistrationConfirmationView extends AbstractVerticalLayout {

    @Override
    protected String getHeaderText() {
        return "Registration Successful";
    }

    @Override
    public void initializePage(Div pageContentsDiv) {

    }
}
