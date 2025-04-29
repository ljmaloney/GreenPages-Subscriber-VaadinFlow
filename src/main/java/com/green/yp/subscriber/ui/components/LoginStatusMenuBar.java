package com.green.yp.subscriber.ui.components;

import com.green.yp.subscriber.ui.account.RegisterAccountView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinService;

public class LoginStatusMenuBar extends MenuBar {

    private static final String REGISTRATION_FLOW = "registrationFlow";

    public LoginStatusMenuBar() {
        Boolean registrationFlow = (Boolean) VaadinService.getCurrentRequest()
                .getWrappedSession()
                .getAttribute(REGISTRATION_FLOW);
        HorizontalLayout layout = new HorizontalLayout();
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        if (!Boolean.TRUE.equals(registrationFlow)) {

        }
    }

    private ComponentEventListener<ClickEvent<Button>> loginEventListener() {
        return new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                getUI().get().getPage().setLocation("http://www.google.com");
            }
        };
    }

    private Button addRegisterButton(HorizontalLayout layout) {
        Button button = new Button("Register");
        button.addClickListener(e -> {
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute(REGISTRATION_FLOW, Boolean.TRUE);
            button.getUI().ifPresent(ui -> ui.navigate(RegisterAccountView.class));
        });
        return button;
    }
}
