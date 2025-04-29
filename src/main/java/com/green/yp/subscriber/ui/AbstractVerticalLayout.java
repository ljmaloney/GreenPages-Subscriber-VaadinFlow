package com.green.yp.subscriber.ui;

import com.green.yp.subscriber.ui.components.CommonFooterDiv;
import com.green.yp.subscriber.ui.components.HeaderDivComponent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.WrappedHttpSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import java.util.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@AnonymousAllowed
@PermitAll
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractVerticalLayout extends VerticalLayout {

    @Value("${greenyp.page.background.color:#F7F7D8}")
    private String pageBackground;

    @PostConstruct
    public void init() {
        add(new HeaderDivComponent());
        getStyle().setWidth("1350px");
        getStyle().setBackground(pageBackground);
        setHorizontalComponentAlignment(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        getUI().ifPresent(ui -> ui.getStyle().setWidth("1350px"));
        getUI().ifPresent(ui -> ui.getStyle().setBackground(pageBackground));
        getUI().ifPresent(ui -> ui.getStyle().setTextAlign(Style.TextAlign.CENTER));

        Div pageHeaderDiv = new Div();
        pageHeaderDiv.setWidthFull();
        addPageHeader(pageHeaderDiv);
        add(pageHeaderDiv);

        Div pageContentsDiv = new Div();
        pageContentsDiv.setWidth(90, Unit.PERCENTAGE);
        initializePage(pageContentsDiv);
        add(pageContentsDiv);

        log.debug("Created home page view");
        add(new CommonFooterDiv());
        setAlignItems(Alignment.CENTER);
        setHorizontalComponentAlignment(Alignment.CENTER);

        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    public void addPageHeader(Div pageHeaderDiv) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();

        H3 headline = new H3(getHeaderText());
        headline.getStyle().setTextAlign(Style.TextAlign.LEFT);
        headline.getStyle().setColor("#106478");
        layout.add(headline);

        pageHeaderDiv.add(layout);
    }

    protected abstract String getHeaderText();

    public abstract void initializePage(Div pageContentsDiv);

    protected Optional<WrappedHttpSession> getSession() {
        if (VaadinService.getCurrentRequest() == null
                || VaadinService.getCurrentRequest().getWrappedSession() == null) {
            return Optional.empty();
        }
        return Optional.of((WrappedHttpSession) VaadinService.getCurrentRequest().getWrappedSession());
    }

    protected Boolean getBooleanAttribute(String attributeName) {
        return getSession()
                .map(session -> (Boolean) session.getAttribute(attributeName))
                .orElse(Boolean.FALSE);
    }

    protected Optional<?> getAttribute(String attributeName) {
        Optional<WrappedHttpSession> session = getSession();
        if (session.isEmpty()) {
            return Optional.empty();
        }
        return session.map(wrappedHttpSession -> wrappedHttpSession.getAttribute(attributeName));
    }

    protected void setAttribute(String attributeName, Object attributeValue) {
        getSession()
                .ifPresent(session -> session.setAttribute(attributeName, attributeValue));
    }
}
