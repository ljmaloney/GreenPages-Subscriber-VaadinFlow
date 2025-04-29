package com.green.yp.subscriber.ui.components;

import com.green.yp.subscriber.ui.account.RegisterAccountView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinService;

public class MenuBarDiv extends Div {

    private static final String REGISTRATION_FLOW = "registrationFlow";

    public MenuBarDiv() {

        HorizontalLayout layout = new HorizontalLayout();
        add(layout);
        layout.setWidthFull();

        HorizontalLayout leftMenuLayout = new HorizontalLayout();
        leftMenuLayout.setAlignItems(FlexComponent.Alignment.START);
        layout.add(leftMenuLayout);

        MenuBar leftMenu = new CommonMenuBar();
        leftMenuLayout.add(leftMenu);
        leftMenuLayout.setWidth(65, Unit.PERCENTAGE);

        setWidth(100, Unit.PERCENTAGE);

        if (!Boolean.TRUE.equals(isRegistrationFlow())) {

            MenuBar loginMenuBar = new MenuBar();
            loginMenuBar.addThemeVariants(MenuBarVariant.LUMO_END_ALIGNED);
            loginMenuBar.addItem("Login", "Click here to login to an existing subscription",
                    this::loginClickListener);
            loginMenuBar.addItem("Register", "Click here to register your green business",
                    this::registerClickListener);

            HorizontalLayout loginLayout = new HorizontalLayout();
            loginLayout.add(loginMenuBar);
            loginLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
            loginLayout.setAlignSelf(FlexComponent.Alignment.END, loginMenuBar);
            loginLayout.setAlignItems(FlexComponent.Alignment.END);
            loginLayout.setWidthFull();

            layout.add(loginLayout);

            loginMenuBar.getItems().forEach(item -> item.getStyle().setColor("#106478"));
        }
    }

    public Boolean isRegistrationFlow() {
        if (VaadinService.getCurrentRequest() == null
                || VaadinService.getCurrentRequest().getWrappedSession() == null)
            return false;

        return (Boolean) VaadinService.getCurrentRequest()
                .getWrappedSession()
                .getAttribute(REGISTRATION_FLOW);
    }

    private void registerClickListener(ClickEvent<MenuItem> event) {
        VaadinService.getCurrentRequest().getWrappedSession().setAttribute(REGISTRATION_FLOW, Boolean.TRUE);
        event.getSource()
                .getUI()
                .ifPresent(ui -> ui.navigate(RegisterAccountView.class));
    }

    private void loginClickListener(ClickEvent<MenuItem> event) {

    }
}
