package com.green.yp.subscriber.ui;

import com.green.yp.api.apitype.reference.SubscriptionApi;
import com.green.yp.subscriber.service.reference.ReferenceService;
import com.green.yp.subscriber.ui.components.SubscriptionDiv;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/subscriptions")
@Slf4j
@AnonymousAllowed
@PermitAll
public class SubscriptionView extends AbstractVerticalLayout {

    private static final String SUBSCRIPTIONS_LOADED = "subscriptionLoaded";

    @NonNull
    private final ReferenceService referenceService;

    private final Div subscriptionDiv = new Div();
    private VerticalLayout verticalLayout = new VerticalLayout();

    public SubscriptionView(@NonNull @NotNull @Autowired ReferenceService referenceService) {
        this.referenceService = referenceService;
    }

    @Override
    protected String getHeaderText() {
        return "Available Subscriptions";
    }

    @Override
    public void initializePage(Div pageContentsDiv) {
        Paragraph paragraph = new Paragraph();
        add(paragraph);

        add(subscriptionDiv);
        subscriptionDiv.add(verticalLayout);

        log.debug("Created home page view");
    }

    public void onAttach(AttachEvent attachEvent) {
        log.debug("SubscriptionView:OnAttach called ... show Lines of Business from DB");

        Optional<List<SubscriptionApi>> subscriptionOptional =
                (Optional<List<SubscriptionApi>>) getAttribute("linesOfBusiness");

        List<SubscriptionApi> lineOfBusinessList = subscriptionOptional.orElseGet(() -> {
            List<SubscriptionApi> subscriptions = referenceService.getSubscriptions();
            setAttribute("subscriptions", subscriptions);
            return subscriptions;
        });

        List<SubscriptionApi> subscriptions = referenceService.getSubscriptions();
        subscriptionDiv.remove(verticalLayout);
        VerticalLayout subsLayout = new VerticalLayout();

        int i = 0;
        HorizontalLayout layout = null;
        for (SubscriptionApi subscription : subscriptions) {
            if (i % 3 == 0) {
                layout = new HorizontalLayout();
                layout.setPadding(true);
                layout.setJustifyContentMode(JustifyContentMode.CENTER);
                subsLayout.add(layout);
            }
            layout.add(new SubscriptionDiv(subscription));
            i++;
        }

        subscriptionDiv.add(subsLayout);
        verticalLayout = subsLayout;
        setAttribute(SUBSCRIPTIONS_LOADED, true);
    }
}
